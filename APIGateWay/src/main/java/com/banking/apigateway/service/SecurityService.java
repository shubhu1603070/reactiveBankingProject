package com.banking.apigateway.service;

import com.banking.apigateway.dto.LoginRequest;
import com.banking.apigateway.dto.TokenInfo;
import com.banking.apigateway.dto.UserDtoResponse;
import com.banking.apigateway.exceptions.APIException;
import com.banking.apigateway.securityconfiguration.security.support.PBKDF2Encoder;
import com.banking.apigateway.servicesURI.URLS;
import com.banking.apigateway.webClient.configuration.WebClientConfiguration;
import com.banking.apigateway.webClient.service.WebClientService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import java.io.Serializable;
import java.util.*;

@Component
public class SecurityService implements Serializable {

    //login
    //authenticate the username and password
    //generate token and return in response body

    @Value("${jwt.secret}")
    private String secretKey;
    @Value("${jwt.expiration}")
    private String expirationTime;

    private final PBKDF2Encoder passwordEncoder;

    private final WebClientService webClientService;

    private final URLS urls;

    private final ObjectMapper objectMapper;


    public SecurityService(PBKDF2Encoder passwordEncoder, WebClientService webClientService,ObjectMapper objectMapper) {
        this.passwordEncoder = passwordEncoder;
        this.webClientService = webClientService;
        this.urls = new URLS();
        this.objectMapper = objectMapper;
    }

    public WebClientConfiguration getWebClientService() {
        return webClientService.getWebClient();
    }

    public Mono<TokenInfo> authenticate(LoginRequest loginRequest){
        return Mono.justOrEmpty(loginRequest)
                .flatMap(loginRequest1 ->
                        getWebClientService().callGetWebClientPostMono(
                                urls.CALL_USER_SERVICE_FIND_BY_USERNAME, null,
                                null,loginRequest.getUsername())).switchIfEmpty(Mono.error(new APIException("username don't exist"+loginRequest.getUsername(),"INVALID_USERNAME")))
                .flatMap(object -> {
                    try {
                        return Mono.justOrEmpty(objectMapper.readValue(objectMapper.writeValueAsString(object), UserDtoResponse.class));
                    } catch (JsonProcessingException e) {
                        return Mono.error(new RuntimeException("Object not mapped properly"));
                    }
                }).flatMap(userDtoResponse -> {
                    if(!passwordEncoder.encode(loginRequest.getPassword()).equals(userDtoResponse.getPassword())){
                        return Mono.error(new AuthException("Invalid user password!", "INVALID_USER_PASSWORD"));
                    }
                    return Mono.just(generateToken(userDtoResponse));
                }).switchIfEmpty(Mono.error(new APIException("Given password don't match","INVALID_PASSWORD")));
    }

    public Mono<String> getString() {
        return getWebClientService().callGetWebClientGetMonoString(urls.CALL_USER_SERVICE_HELLO,null,null);
    }

    private TokenInfo generateToken(UserDtoResponse userDtoResponse) {
        Map<String,Object> claims = new HashMap<String,Object>(){{
            put("role",userDtoResponse.getRole());
        }};
        return doGenerateToken(userDtoResponse,claims);
    }

    private TokenInfo doGenerateToken(UserDtoResponse userDtoResponse, Map<String, Object> claims) {
        long issuedTime = Long.parseLong(expirationTime) * 100;
        Date expireTime = new Date(new Date().getTime() + issuedTime);
        return doGenerateToken(userDtoResponse,claims,expireTime);
    }

    private TokenInfo doGenerateToken(UserDtoResponse userDtoResponse, Map<String, Object> claims, Date expireTime) {
        Date createdAt = new Date();

        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(createdAt)
                .setExpiration(expireTime)
                .setSubject(userDtoResponse.getUserId())
                .setIssuer(userDtoResponse.getUsername())
                .setId(UUID.randomUUID().toString())
                .signWith(getSingKey())
                .compact();

        return TokenInfo.builder()
                .userId(userDtoResponse.getUserId())
                .username(userDtoResponse.getUsername())
                .issuedAt(createdAt)
                .expiresAt(expireTime)
                .token(token)
                .build();
    }

    private SecretKey getSingKey(){
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
    }

}
