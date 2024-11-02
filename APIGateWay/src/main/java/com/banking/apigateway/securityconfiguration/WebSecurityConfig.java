package com.banking.apigateway.securityconfiguration;

import com.banking.apigateway.securityconfiguration.security.support.CustomServerAuthenticationConvertor;
import com.banking.apigateway.securityconfiguration.security.support.JWTVerifierHandler;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.util.matcher.NegatedServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.OrServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;
import reactor.core.publisher.Mono;

import java.security.SecureRandom;
import java.util.Base64;

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
@Slf4j
public class WebSecurityConfig {


    @Value("${jwt.secret}")
    private String SECRET;

    @Value("${application.base.url}")
    private String localhost;

    @Value("${app.public_routes}")
    private String[] publicRouts;

    private final Logger logger = LoggerFactory.getLogger(WebSecurityConfig.class);

    /**
     * This method is used to generate BASE64 Token which we use for signing the token.
     * Written By Shubham Kumar
     * @return
     */
    private final String getBase64KeyForJWT() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] secretKey = new byte[64];
        secureRandom.nextBytes(secretKey);

        String secret = Base64.getEncoder().encodeToString(secretKey);
        System.out.println(secret);
        return secret;
    }

    @Bean
    SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity httpSecurity, CustomAuthenticationManager customAuthenticationManager) {

        httpSecurity.httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .exceptionHandling(exceptionHandlingSpec -> exceptionHandlingSpec.authenticationEntryPoint((exchange, ex) -> {
                    logger.info("[1] Authentication error: Unauthorized[401]: " + ex.getMessage());
                    return Mono.fromRunnable(() -> exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED));
                }).accessDeniedHandler((exchange, denied) -> {
                    logger.info("[1] Authentication error: Unauthorized[401]: " + denied.getMessage());
                    return Mono.fromRunnable(() -> exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN));
                })).authorizeExchange(authorizeExchangeSpec -> {
                    authorizeExchangeSpec.pathMatchers(HttpMethod.OPTIONS).permitAll();
                    authorizeExchangeSpec.pathMatchers(publicRouts).permitAll();
                    authorizeExchangeSpec.anyExchange().authenticated();
                }).addFilterAt(authenticationWebFilter(customAuthenticationManager), SecurityWebFiltersOrder.AUTHENTICATION);

        return httpSecurity.build();
    }




    @Bean
    AuthenticationWebFilter authenticationWebFilter(CustomAuthenticationManager authenticationManager) {
        AuthenticationWebFilter authenticationWebFilter = new AuthenticationWebFilter(authenticationManager);
        ServerWebExchangeMatcher serverWebExchangeMatcher = new OrServerWebExchangeMatcher(
                ServerWebExchangeMatchers.pathMatchers("/signup"),
                ServerWebExchangeMatchers.pathMatchers("/login")
        );
        authenticationWebFilter.setServerAuthenticationConverter(new CustomServerAuthenticationConvertor(new JWTVerifierHandler(SECRET)));
        authenticationWebFilter.setRequiresAuthenticationMatcher(new NegatedServerWebExchangeMatcher(serverWebExchangeMatcher));
        return authenticationWebFilter;
    }


}
