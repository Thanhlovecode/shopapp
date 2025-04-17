package shop.example.service.impl;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import shop.example.domain.Role;
import shop.example.domain.User;
import shop.example.enums.ErrorCode;
import shop.example.exceptions.TokenValidationException;
import shop.example.service.TokenService;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TokenServiceImpl implements TokenService {

    private static final String ISSUER = "shopApp.com";
    private static final String TOKEN_VERSION = "1";
    private static final JWSAlgorithm ALGORITHM = JWSAlgorithm.HS512;

    @Value("${SIGNER_KEY}")
    private String signerKey;

    @Override
    public String generateAccessToken(User user, Long duration) {
        try {
            JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                    .subject(String.valueOf(user.getId()))
                    .issuer(ISSUER)
                    .issueTime(new Date())
                    .expirationTime(Date.from(Instant.now().plusSeconds(duration)))
                    .claim("scope", buildScope(user))
                    .claim("token_version", TOKEN_VERSION)
                    .jwtID(UUID.randomUUID().toString())
                    .build();
            return createSignedToken(claimsSet);
        } catch (JOSEException e) {
            log.error("Failed to generate access token for user: {}", user.getId(), e);
            throw new TokenValidationException(ErrorCode.TOKEN_INVALID.getMessage());
        }
    }

    @Override
    public String generateRefreshToken(User user, Long duration) {
        try {
            JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                    .subject(user.getFullName())
                    .issuer(ISSUER)
                    .issueTime(new Date())
                    .expirationTime(Date.from(Instant.now().plus(duration, ChronoUnit.SECONDS)))
                    .claim("userId", user.getId())
                    .jwtID(UUID.randomUUID().toString())
                    .build();
            return createSignedToken(claimsSet);
        } catch (JOSEException e) {
            log.error("Failed to generate refresh token for user: {}", user.getId(), e);
            throw new TokenValidationException(ErrorCode.TOKEN_INVALID.getMessage());
        }
    }

    @Override
    public Long getUserIdFromRefreshToken(String refreshToken) {
        try {
            return parseToken(refreshToken).getJWTClaimsSet().getLongClaim("userId");
        } catch (ParseException e) {
            log.error("Failed to parse userId from refresh token: {}", refreshToken, e);
            throw new TokenValidationException(ErrorCode.TOKEN_INVALID.getMessage());
        }
    }

    @Override
    public String getUUIDFromRefreshToken(String refreshToken) {
        try {
            return parseToken(refreshToken).getJWTClaimsSet().getJWTID();
        } catch (ParseException e) {
            log.error("Failed to parse UUID from refresh token: {}", refreshToken, e);
            throw new TokenValidationException(ErrorCode.TOKEN_INVALID.getMessage());
        }
    }

    private String createSignedToken(JWTClaimsSet claimsSet) throws JOSEException {
        JWSHeader header = new JWSHeader(ALGORITHM);
        JWSObject jwsObject = new JWSObject(header, new Payload(claimsSet.toJSONObject()));
        jwsObject.sign(new MACSigner(signerKey.getBytes()));
        return jwsObject.serialize();
    }

    private SignedJWT parseToken(String token) throws ParseException {
        return SignedJWT.parse(token);
    }

    private String buildScope(User user) {
        if (CollectionUtils.isEmpty(user.getRoles())) {
            return "";
        }
        return user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.joining(" "));
    }
}