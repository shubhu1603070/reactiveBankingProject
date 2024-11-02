package com.banking.usermicroservice.services;

import com.banking.usermicroservice.dto.UserDtoRequest;
import com.banking.usermicroservice.dto.UserDtoResponse;
import com.banking.usermicroservice.entities.User;
import com.banking.usermicroservice.kafka.KafkaProducerController;
import com.banking.usermicroservice.passwordencoder.PBKDF2Encoder;
import com.banking.usermicroservice.repo.UserRepo;
import com.banking.usermicroservice.utils.AppUtils;
import org.springframework.dao.DuplicateKeyException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepo userRepo;

    private final PBKDF2Encoder passwordEncoder;

    private final KafkaProducerController kafkaProducerController;

    public Mono<UserDtoResponse> signUp(UserDtoRequest userDtoRequest){
        return Mono.defer(() -> {
            return getSaveUser(userDtoRequest)
                    .map(user -> {
                        user.setPassword(passwordEncoder.encode(user.getPassword()));
                        return user;
                    })
                    .flatMap(userRepo::save)
                    .map(AppUtils::convertToUserDto)
                    .doOnError(ex -> {
                        System.out.println("The exception is : "+ex);
                    })
                    .onErrorResume(DuplicateKeyException.class, ex -> Mono.error(new RuntimeException("Email already used!",ex)))
                    .switchIfEmpty(Mono.error(new RuntimeException("Couldn't convert the DTO")));
        });
    }

    private static Mono<User> getSaveUser(UserDtoRequest userDtoRequest){
        return Mono.justOrEmpty(userDtoRequest)
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
        user.setUpdatedAt(LocalDateTime.now());
    }

    public Mono<UserDtoResponse> findByUserName(String username) {
        return userRepo.findByUserName(username)
                .map(AppUtils::convertToUserDto);
    }

    public Mono<UserDtoResponse> saveUser(UserDtoRequest userDtoRequest) {
        return Mono.defer(() -> {
            User user = AppUtils.convertToUser(userDtoRequest);
            return userRepo.save(user)
                    .map(AppUtils::convertToUserDto)
                    .doOnSuccess(kafkaProducerController::sendMessage);
        });
    }
}
