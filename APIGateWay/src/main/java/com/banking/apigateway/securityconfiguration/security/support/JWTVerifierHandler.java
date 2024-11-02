package com.banking.apigateway.securityconfiguration.security.support;

import com.banking.apigateway.securityconfiguration.security.auth.UnauthorizedException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.crypto.codec.Base64;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import java.util.Date;

public class JWTVerifierHandler {

    private final String secret;

    public JWTVerifierHandler(String secret) {
        this.secret = secret;
    }

    public Mono<VerificationResult> check(String token) {
        return Mono.justOrEmpty(verify(token))
                .onErrorResume(e -> Mono.error(new UnauthorizedException(e.getMessage())));
    }

    private VerificationResult verify(String token) {
        Claims claims = getAllClaims(token);
        Date expiration = claims.getExpiration();
        if (expiration.before(new Date())) {
            throw new RuntimeException("Token Expired");
        }
        return new VerificationResult(claims, token);
    }

    private Claims getAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getEncodedSecret(secret))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private static SecretKey getEncodedSecret(String secretKey) {
        return Keys
                .hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
    }


    public static class VerificationResult {
        public Claims claims;
        public String token;

        public VerificationResult(Claims claims, String token) {
            this.claims = claims;
            this.token = token;
        }
    }


}
