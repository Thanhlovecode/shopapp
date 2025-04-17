package shop.example.config;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;
import shop.example.enums.ErrorCode;
import shop.example.exceptions.AuthenticatedException;
import shop.example.exceptions.TokenValidationException;
import shop.example.service.RedisService;

import javax.crypto.spec.SecretKeySpec;
import java.text.ParseException;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtCustomDecoder implements JwtDecoder {

    private final RedisService redisService;

    @Value("${SIGNER_KEY}")
    protected String signerKey;

    private static final String prefixAccessToken = "access_token_userId:";

    @Override
    public Jwt decode(String token) throws JwtException {
        try {
            JWSVerifier jwsVerifier = new MACVerifier(signerKey.getBytes());
            SignedJWT signedJWT = SignedJWT.parse(token);
            String userID = signedJWT.getJWTClaimsSet().getSubject();
            Date expirationTime = signedJWT.getJWTClaimsSet().getExpirationTime();

            if (!signedJWT.verify(jwsVerifier) ||
                    expirationTime.before(new Date()) ||
                    !redisService.getString(prefixAccessToken + userID).equals("1")) {
                throw new AuthenticationException("", new AuthenticatedException(
                        ErrorCode.UNAUTHORIZED.getMessage()
                )) {
                };
            }
        } catch (JOSEException | ParseException e) {
            throw new AuthenticationException("", new AuthenticatedException(
                    ErrorCode.UNAUTHORIZED.getMessage()
            )) {
            };
        }
        SecretKeySpec secretKeySpec = new SecretKeySpec(signerKey.getBytes(), "HS512");
        NimbusJwtDecoder nimbusJwtDecoder = NimbusJwtDecoder
                .withSecretKey(secretKeySpec)
                .macAlgorithm(MacAlgorithm.HS512)
                .build();
        return nimbusJwtDecoder.decode(token);
    }
}
