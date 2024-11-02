package com.banking.usermicroservice.controller;

import com.banking.usermicroservice.dto.FindUserByUserNameRequest;
import com.banking.usermicroservice.dto.UserDtoRequest;
import com.banking.usermicroservice.dto.UserDtoResponse;
import com.banking.usermicroservice.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/user/api/v1")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public Mono<UserDtoResponse> signUpUser(@RequestBody UserDtoRequest userDtoRequest){
        return userService.signUp(userDtoRequest);
    }

    @PostMapping("/find-user")
    public Mono<UserDtoResponse> findByUsername(@RequestBody FindUserByUserNameRequest findUserByUserNameRequest){
        return userService.findByUserName(findUserByUserNameRequest.getUsername());
    }

    @GetMapping("/hello")
    public Mono<String> findByUserId(){
        return Mono.just("Hello There!");
    }

    @PostMapping("/save-user")
    public Mono<UserDtoResponse> saveUser(@RequestBody UserDtoRequest userDtoRequest){
        return userService.saveUser(userDtoRequest);
    }

}
