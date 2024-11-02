package com.banking.usermicroservice.utils;

import com.banking.usermicroservice.dto.UserDtoRequest;
import com.banking.usermicroservice.dto.UserDtoResponse;
import com.banking.usermicroservice.entities.User;

public class AppUtils {

    public static User convertToUser(UserDtoResponse userDtoResponse){
        return User.builder()
                .email(userDtoResponse.getEmail())
                .userName(userDtoResponse.getUsername())
                .password(userDtoResponse.getPassword())
                .role(userDtoResponse.getRole())
                .amount(userDtoResponse.getAmount())
                .build();
    }

    public static User convertToUser(UserDtoRequest userDtoRequest){
        return User.builder()
                .email(userDtoRequest.getEmail())
                .userName(userDtoRequest.getUsername())
                .password(userDtoRequest.getPassword())
                .role(userDtoRequest.getRole())
                .amount(userDtoRequest.getAmount())
                .build();
    }

    public static UserDtoResponse convertToUserDto(User user){
        return UserDtoResponse.builder()
                .username(user.getUserName())
                .password(user.getPassword())
                .amount(user.getAmount())
                .role(user.getRole())
                .email(user.getEmail())
                .userId(user.getUserId())
                .build();
    }

}
