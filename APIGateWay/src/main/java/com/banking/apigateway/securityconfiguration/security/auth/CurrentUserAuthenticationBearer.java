package com.banking.apigateway.securityconfiguration.security.auth;

import com.banking.apigateway.securityconfiguration.security.support.JWTVerifierHandler;
import io.jsonwebtoken.Claims;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/*
    This class will be used to change the ServerWebExchange into Authentication Object
 */
public class CurrentUserAuthenticationBearer {
    public static Mono<Authentication> create(JWTVerifierHandler.VerificationResult verificationResult) {
        Claims claims = verificationResult.claims;
        String subject = claims.getSubject();
        List<String> role = claims.get("role",List.class);
        List<SimpleGrantedAuthority> roles = role
                .stream()
                .map(SimpleGrantedAuthority::new)
                .toList();


        if(subject!=null){
            MyUserPrinciple myUserPrinciple = new MyUserPrinciple(subject, claims.getIssuer());
            return Mono.justOrEmpty(new UsernamePasswordAuthenticationToken(myUserPrinciple,null,roles));
        }

        return Mono.empty();

    }
}
