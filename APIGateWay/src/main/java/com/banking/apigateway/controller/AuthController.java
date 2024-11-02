package com.banking.apigateway.controller;

import com.banking.apigateway.dto.LoginRequest;
import com.banking.apigateway.dto.TokenInfo;
import com.banking.apigateway.service.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("")
public class AuthController {

    @Autowired
    private SecurityService securityService;


    @PostMapping("/login")
    public Mono<TokenInfo> login(@RequestBody LoginRequest loginRequest){
        return securityService.authenticate(loginRequest);
    }

    @GetMapping("/getResult")
    public Mono<String> getString(){
        return securityService.getString();
    }

}
