package com.banking.usermicroservice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class SignInRequest {
    @NotNull
    private String username;
    @NotNull
    private String password;
}
