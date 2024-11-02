package com.banking.usermicroservice.dto;

import com.banking.usermicroservice.Enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@ToString
public class UserDtoResponse {
    String userId;
    @NotBlank
    @Email
    String email;
    @NotBlank
    String username;
    @NotBlank
    String password;
    String amount;
    @NotBlank
    List<Role> role;
}