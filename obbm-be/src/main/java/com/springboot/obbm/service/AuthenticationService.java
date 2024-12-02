package com.springboot.obbm.service;

import com.springboot.obbm.client.OutboundObbmClient;
import com.springboot.obbm.client.OutboundUserClient;
import com.springboot.obbm.constant.PredefinedRole;
import com.springboot.obbm.dto.request.*;
import com.springboot.obbm.dto.response.AuthenticationResponse;
import com.springboot.obbm.dto.response.IntrospectResponse;
import com.springboot.obbm.model.*;
import com.springboot.obbm.exception.AppException;
import com.springboot.obbm.exception.ErrorCode;
import com.springboot.obbm.respository.InvalidatedTokenRepository;
import com.springboot.obbm.respository.UserRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.springboot.obbm.respository.UserRolePermissionRepository;
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
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {
    UserRepository userRepository;
    InvalidatedTokenRepository invalidatedTokenRepository;
    UserRolePermissionRepository userRolePermissionRepository;
    OutboundObbmClient outboundObbmClient;
    OutboundUserClient outboundUserClient;

    @NonFinal
    @Value("${jwt.signerKey}")
    protected String SIGNER_KEY;

    @NonFinal
    @Value("${jwt.valid-duration}")
    protected long VALID_DURATION;

    @NonFinal
    @Value("${jwt.refreshable-duration}")
    protected long REFRESHABLE_DURATION;

    @NonFinal
    @Value("${outbound.identity.client-id}")
    protected String CLIENT_ID;

    @NonFinal
    @Value("${outbound.identity.client-secret}")
    protected String CLIENT_SECRET;

    @NonFinal
    @Value("${outbound.identity.redirect-uri}")
    protected String REDIRECT_URI;

    @NonFinal
    protected String GRANT_TYPE = "authorization_code";

    public IntrospectResponse introspect(IntrospectRequest request)
            throws JOSEException, ParseException {
        var token = request.getToken();
        boolean isValid = true;

        try {
            verifyToken(token, false); // Chỉ chấp nhận accessToken
        } catch (AppException e) {
            isValid = false;
        }

        return IntrospectResponse.builder()
                .valid(isValid)
                .build();
    }

    public AuthenticationResponse outboundAuthentication(String code) {
        ExchangeTokenRequest tokenRequest = ExchangeTokenRequest.builder()
                .code(code)
                .clientId(CLIENT_ID)
                .clientSecret(CLIENT_SECRET)
                .redirectUri(REDIRECT_URI)
                .grantType(GRANT_TYPE)
                .build();
        var response = outboundObbmClient.exchangeToken(tokenRequest);
        log.info("Received token response: {}", response);

        var userInfo = outboundUserClient.getUserInfo("json", response.getAccessToken());
        log.info("Received user info: {}", userInfo);

        Set<Role> roles = new HashSet<>();
        roles.add(Role.builder().name(PredefinedRole.USER_ROLE).build());

        var user = userRepository.findByUsername(userInfo.getEmail()).orElseGet(
                () -> userRepository.save(User.builder()
                        .username(userInfo.getEmail())
                        .fullname(userInfo.getName())
                        .email(userInfo.getEmail())
                        .image(userInfo.getPicture())
                        .createdAt(LocalDateTime.now())
                        .roles(roles)
                        .build()));

        var accessToken = generateAccessToken(user);

        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .build();
    }

    private SignedJWT verifyToken(String token, boolean isRefresh) throws ParseException, JOSEException {
        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());
        SignedJWT signedJWT = SignedJWT.parse(token);

        if (!signedJWT.verify(verifier)) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        boolean tokenIsRefresh = signedJWT.getJWTClaimsSet().getBooleanClaim("isRefresh");

        if (tokenIsRefresh != isRefresh) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();
        if (expiryTime.before(new Date())) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        if (invalidatedTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID())) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        return signedJWT;
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        var user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_USER));

        boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());
        if (!authenticated) {
            throw new AppException(ErrorCode.INVALID_USER);
        }

        var accessToken = generateAccessToken(user);
        var refreshToken = generateRefreshToken(user);

        // Trả về cả accessToken và refreshToken
        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public AuthenticationResponse refreshToken(RefreshTokenRequest request) throws ParseException, JOSEException {
        String refreshToken = request.getRefreshToken();

        var signToken = verifyToken(refreshToken, true);
        var username = signToken.getJWTClaimsSet().getSubject();
        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_EXISTED, "Người dùng"));

        var newAccessToken = generateAccessToken(user);

        return AuthenticationResponse.builder()
                .accessToken(newAccessToken)
                .build();
    }

    public void logout(LogoutRequest request) throws ParseException, JOSEException {
        String refreshToken = request.getRefreshToken();
        String accessToken = request.getAccessToken();

        if (refreshToken != null) {
            invalidateToken(refreshToken, true);
        }

        if (accessToken != null) {
            invalidateToken(accessToken, false);
        }

        log.info("User logged out and both tokens (access and refresh) are invalidated.");
    }

    private String generateAccessToken(User user) {
        return generateToken(user, VALID_DURATION, false);
    }

    private String generateRefreshToken(User user) {
        return generateToken(user, REFRESHABLE_DURATION, true);
    }

    private void invalidateToken(String token, boolean isRefresh) throws ParseException, JOSEException {
        var signedToken = verifyToken(token, isRefresh);

        String tokenJti = signedToken.getJWTClaimsSet().getJWTID();
        Date tokenExpiryTime = signedToken.getJWTClaimsSet().getExpirationTime();

        InvalidatedToken invalidatedToken = InvalidatedToken.builder()
                .id(tokenJti)
                .expiryTime(tokenExpiryTime)
                .createdAt(LocalDateTime.now())
                .build();
        invalidatedTokenRepository.save(invalidatedToken);
    }

    public String generateToken(User user, long duration, boolean isRefresh) {
        ensureUserRolePermissionExists(user);
        String scope = buildScope(user);

        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);
        JWTClaimsSet.Builder jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .issuer("kido.com")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(duration, ChronoUnit.SECONDS).toEpochMilli()
                ))
                .jwtID(UUID.randomUUID().toString())
                .claim("isRefresh", isRefresh);


        if (!isRefresh) {
            jwtClaimsSet.claim("scope", scope); // Chỉ thêm scope nếu là accessToken
        }

        Payload payload = new Payload(jwtClaimsSet.build().toJSONObject());
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
        ensureUserRolePermissionExists(user);

        Set<String> scopeSet = new LinkedHashSet<>();
        List<UserRolePermission> urpList = userRolePermissionRepository.findByUsers(user);

        urpList.forEach(urp -> {
            scopeSet.add("ROLE_" + urp.getRoles().getName());
            scopeSet.add(urp.getPermissions().getName());
        });

        return String.join(" ", scopeSet);
    }


    private void ensureUserRolePermissionExists(User user) {
        boolean hasExistingPermissions = userRolePermissionRepository.existsByUsers_UserId(user.getUserId());

        if (hasExistingPermissions) {
            // Nếu đã tồn tại dữ liệu, thoát khỏi phương thức
            return;
        }

        // Nếu chưa tồn tại, tiến hành thêm mới
        Set<Role> userRoles = user.getRoles();

        userRoles.forEach(role -> {
            List<Permission> permissions = role.getPermissions().stream().toList();

            permissions.forEach(permission -> {
                UserRolePermission urp = UserRolePermission.builder()
                        .users(user)
                        .roles(role)
                        .permissions(permission)
                        .createdAt(LocalDateTime.now())
                        .build();
                userRolePermissionRepository.save(urp);
            });
        });
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
