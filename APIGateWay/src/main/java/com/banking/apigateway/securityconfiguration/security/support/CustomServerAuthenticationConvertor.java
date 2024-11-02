package com.banking.apigateway.securityconfiguration.security.support;

import com.banking.apigateway.securityconfiguration.security.auth.CurrentUserAuthenticationBearer;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.function.Function;
import java.util.function.Predicate;


public class CustomServerAuthenticationConvertor implements ServerAuthenticationConverter {

    private final JWTVerifierHandler jwtVerifierHandler;

    private final String Bearer = "Bearer ";

    Predicate<String> checkBearerLength = authValueBearer -> authValueBearer.length() > Bearer.length();

    Function<String, Mono<? extends String>> extractTokenFromString = getBearerValue -> Mono.justOrEmpty(getBearerValue.substring(Bearer.length()));

    public CustomServerAuthenticationConvertor(JWTVerifierHandler jwtVerifierHandler) {
        this.jwtVerifierHandler = jwtVerifierHandler;
    }

    @Override
    public Mono<Authentication> convert(ServerWebExchange serverWebExchange) {
        return Mono.justOrEmpty(serverWebExchange)
                .flatMap(CustomServerAuthenticationConvertor::extract)
                .filter(checkBearerLength)
                .flatMap(extractTokenFromString)
                .flatMap(jwtVerifierHandler::check)
                .flatMap(CurrentUserAuthenticationBearer::create);
    }

    public static Mono<String> extract(ServerWebExchange serverWebExchange){
        return Mono.justOrEmpty(serverWebExchange.getRequest()
                                .getHeaders()
                                .getFirst(HttpHeaders.AUTHORIZATION));
    }
}
