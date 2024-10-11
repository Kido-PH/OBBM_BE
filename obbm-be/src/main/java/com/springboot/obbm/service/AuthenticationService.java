package com.springboot.obbm.service;

import com.springboot.obbm.dto.request.AuthenticationRequest;
import com.springboot.obbm.dto.request.IntrospectRequest;
import com.springboot.obbm.dto.request.LogoutRequest;
import com.springboot.obbm.dto.request.RefreshRequest;
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

    public IntrospectResponse introspect(IntrospectRequest request)
            throws JOSEException, ParseException {
        var token = request.getToken();
        boolean isValid = true;

        try {
            verifyToken(token);
        } catch (AppException e) {
            isValid = false;
        }

        return IntrospectResponse.builder()
                .valid(isValid)
                .build();
    }

    private SignedJWT verifyToken(String token) throws ParseException, JOSEException {
        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());
        SignedJWT signedJWT = SignedJWT.parse(token);

        Date expirytime = signedJWT.getJWTClaimsSet().getExpirationTime();

        var verified = signedJWT.verify(verifier);

        if (!(verified && expirytime.after(new Date())))
            throw new AppException(ErrorCode.UNAUTHENTICATED);

        if (invalidatedTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID()))
            throw new AppException(ErrorCode.UNAUTHENTICATED);

        return signedJWT;
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        var user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());

        if (!authenticated) throw new AppException(ErrorCode.UNAUTHENTICATED);
        ;

        var token = generateToken(user);

        return AuthenticationResponse.builder()
                .token(token)
                .authenticated(true)
                .build();
    }

    public void logout(LogoutRequest request) throws ParseException, JOSEException {
        var signToken = verifyToken(request.getToken());

        String jti = signToken.getJWTClaimsSet().getJWTID();
        Date expiryTime = signToken.getJWTClaimsSet().getExpirationTime();

        invalidatedTokenRepository.save(InvalidatedToken.builder()
                .id(jti)
                .expiryTime(expiryTime)
                .build());
    }

//    public AuthenticationResponse refreshToken(RefreshRequest request) throws ParseException, JOSEException {
//        var signedJWT = verifyToken(request.getToken());
//
//        var jit = signedJWT.getJWTClaimsSet().getJWTID();
//        var expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();
//
//        InvalidatedToken invalidatedToken =
//                InvalidatedToken.builder().id(jit).expiryTime(expiryTime).build();
//
//        invalidatedTokenRepository.save(invalidatedToken);
//
//        var username = signedJWT.getJWTClaimsSet().getSubject();
//
//        var user =
//                userRepository.findByUsername(username).orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));
//
//        var token = generateToken(user);
//
//        return AuthenticationResponse.builder().token(token).authenticated(true).build();
//    }

    public String generateToken(User user) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .issuer("kido.com")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()
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

    @Scheduled(cron = "0 0 0 * * ?") // Mỗi ngày vào lúc 0:00
    @Transactional
    public void cleanExpiredTokens() {
        log.info("Cleaning expired tokens... ");

        Date now = new Date();
        int deletedCount = invalidatedTokenRepository.deleteByExpiryTimeBefore(now);

        log.info("Cleanup completed. Total expired tokens deleted: " + deletedCount);
    }
}
