package com.springboot.obbm.service;

import com.springboot.obbm.dto.request.AuthenticationRequest;
import com.springboot.obbm.dto.request.IntrospectRequest;
import com.springboot.obbm.dto.response.AuthenticationResponse;
import com.springboot.obbm.dto.response.IntrospectResponse;
import com.springboot.obbm.models.InvalidatedToken;
import com.springboot.obbm.models.User;
import com.springboot.obbm.exception.AppException;
import com.springboot.obbm.exception.ErrorCode;
import com.springboot.obbm.respository.InvalidatedTokenRepository;
import com.springboot.obbm.respository.UserRespository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Date;
import java.util.StringJoiner;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {
    UserRespository userRepository;
    InvalidatedTokenRepository invalidatedTokenRepository;

    @NonFinal
    @Value("${jwt.signerKey}")
    protected String SIGNER_KEY;

    @NonFinal
    @Value("${jwt.valid-duration}")
    protected long VALID_DURATION;

    @NonFinal
    @Value("${jwt.refreshable-duration}")
    protected long REFRESHABLE_DURATION;

    public IntrospectResponse introspect(IntrospectRequest request)
            throws JOSEException, ParseException {
        var token = request.getToken();
        boolean isValid = true;

        try {
            verifyToken(token, false);
        } catch (AppException e) {
            isValid = false;
        }

        return IntrospectResponse.builder()
                .valid(isValid)
                .build();
    }

    private SignedJWT verifyToken(String token, boolean isRefresh) throws ParseException, JOSEException {
        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());
        SignedJWT signedJWT = SignedJWT.parse(token);

        Date expiryTime = (isRefresh)
                ? new Date(signedJWT.getJWTClaimsSet().getIssueTime()
                .toInstant().plus(REFRESHABLE_DURATION, ChronoUnit.SECONDS).toEpochMilli())
                : signedJWT.getJWTClaimsSet().getExpirationTime();

        if (!signedJWT.verify(verifier) || expiryTime.before(new Date())) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        if (invalidatedTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID())) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        return signedJWT;
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request, HttpServletResponse response) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        var user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_EXISTED, "Người dùng"));

        boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());
        if (!authenticated) throw new AppException(ErrorCode.UNAUTHENTICATED);

        var accessToken = generateAccessToken(user);
        var refreshToken = generateRefreshToken(user);

        // Set refresh token vào HttpOnly Cookie
        Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(false);
        refreshTokenCookie.setPath("/obbm/auth/refresh");
        refreshTokenCookie.setMaxAge((int) REFRESHABLE_DURATION);

        response.addCookie(refreshTokenCookie);

        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .authenticated(true)
                .build();
    }

    public AuthenticationResponse refreshToken(HttpServletRequest request) throws ParseException, JOSEException {

        String refreshToken = getRefreshTokenFromCookies(request);

        var signToken = verifyToken(refreshToken, true);
        var username = signToken.getJWTClaimsSet().getSubject();
        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_EXISTED, "Người dùng"));

        var newAccessToken = generateAccessToken(user);

        return AuthenticationResponse.builder()
                .accessToken(newAccessToken)  // Trả về access token mới
                .authenticated(true)
                .build();
    }

    public void logout(HttpServletRequest request) throws ParseException, JOSEException {
        String refreshToken = getRefreshTokenFromCookies(request);
        invalidateToken(refreshToken, true);

        // Lấy access token từ header Authorization
        String accessToken = request.getHeader("Authorization");
        if (accessToken != null && accessToken.startsWith("Bearer ")) {
            accessToken = accessToken.substring(7); // Loại bỏ tiền tố "Bearer "
            invalidateToken(accessToken, false);
        }
        log.info("User logged out and both tokens (access and refresh) are invalidated.");
    }

    private String generateAccessToken(User user) {
        return generateToken(user, VALID_DURATION);
    }

    private String generateRefreshToken(User user) {
        return generateToken(user, REFRESHABLE_DURATION);
    }

    private String getRefreshTokenFromCookies(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        return Arrays.stream(cookies)
                .filter(cookie -> "refreshToken".equals(cookie.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));
    }

    private void invalidateToken(String token, boolean isRefresh) throws ParseException, JOSEException {
        var signedToken = verifyToken(token, isRefresh);

        String tokenJti = signedToken.getJWTClaimsSet().getJWTID();
        Date tokenExpiryTime = signedToken.getJWTClaimsSet().getExpirationTime();

        InvalidatedToken invalidatedToken = InvalidatedToken.builder()
                .id(tokenJti)
                .expiryTime(tokenExpiryTime)
                .build();
        invalidatedTokenRepository.save(invalidatedToken);
    }

    public String generateToken(User user, long duration) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .issuer("kido.com")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(duration, ChronoUnit.SECONDS).toEpochMilli()
                ))
                .jwtID(UUID.randomUUID().toString())
                .claim("scope", buildScope(user))  // Chứa các quyền (roles) của người dùng
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(header, payload);

        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("Can't create token", e);
            throw new RuntimeException(e);
        }
    }

    private String buildScope(User user) {
        StringJoiner stringJoiner = new StringJoiner(" ");

        // Kiểm tra người dùng có roles không, nếu có, thêm từng role vào scope
        if (!CollectionUtils.isEmpty(user.getRoles())) {
            user.getRoles().forEach(role -> {
                stringJoiner.add("ROLE_" + role.getName());
                if (!CollectionUtils.isEmpty(role.getPermissions())) {
                    role.getPermissions()
                            .forEach(permission -> stringJoiner.add(permission.getName()));
                }
            });
        }
        return stringJoiner.toString();
    }

    @Scheduled(cron = "0 0 0 * * ?")  // Clean expired tokens daily
    @Transactional
    public void cleanExpiredTokens() {
        log.info("Cleaning expired tokens...");
        Date now = new Date();
        int deletedCount = invalidatedTokenRepository.deleteByExpiryTimeBefore(now);
        log.info("Cleanup completed. Total expired tokens deleted: " + deletedCount);
    }
}
