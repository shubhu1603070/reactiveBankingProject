package com.banking.apigateway.dto;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Data
public class LoginRequest {
    String username;
    String password;
}
