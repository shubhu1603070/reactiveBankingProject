package com.banking.usermicroservice.webClient.service;

import com.banking.usermicroservice.Enums.MonoFluxType;
import com.banking.usermicroservice.dto.UserDtoResponse;
import com.banking.usermicroservice.entities.User;
import com.banking.usermicroservice.repo.UserRepo;
import com.banking.usermicroservice.utils.AppUtils;
import com.banking.usermicroservice.webClient.configuration.WebClientConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
public class WebClientService {

    @Value("${application.base.url}")
    private String baseURL;

    private final UserRepo userRepo;

    private WebClientConfiguration webClientConfiguration;

    public WebClientService(UserRepo userRepo) {
        this.userRepo = userRepo;
        this.webClientConfiguration = new WebClientConfiguration(baseURL);
    }

    public Mono<UserDtoResponse> signUp(UserDtoResponse userDtoResponse){
        return getSaveUser(userDtoResponse)
                .flatMap(userRepo::save)
                .map(AppUtils::convertToUserDto);
    }

    public Mono<UserDtoResponse> signUp1(UserDtoResponse userDtoResponse){
        return (Mono<UserDtoResponse>) webClientConfiguration.callGetWebClient(HttpMethod.POST, "/user/api/v1/signup", "", null, userDtoResponse, MonoFluxType.Mono, UserDtoResponse.class);

    }

    private static Mono<User> getSaveUser(UserDtoResponse userDtoResponse){
        return Mono.justOrEmpty(userDtoResponse)
                .map(AppUtils::convertToUser)
                .map(user -> {
                    setCreatedAt(user);
                    setUpdatedAt(user);
                    return user;
                });
    }

    private static void setCreatedAt(User user){
        user.setCreateAt(LocalDateTime.now());
    }

    private static void setUpdatedAt(User user){
        user.setCreateAt(LocalDateTime.now());
    }

    public Mono<UserDtoResponse> findByUserName(String username) {
        return userRepo.findByUserName(username)
                .map(AppUtils::convertToUserDto);
    }
}
