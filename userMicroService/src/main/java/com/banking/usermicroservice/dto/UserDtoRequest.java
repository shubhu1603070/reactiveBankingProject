package com.banking.usermicroservice.dto;

import com.banking.usermicroservice.Enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.data.mongodb.core.index.Indexed;

import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@ToString
public class UserDtoRequest {
    @NotBlank
    @Email
    @Indexed(unique = true)
    String email;
    @NotBlank
    @Indexed(unique = true)
    String username;
    @NotBlank
    String password;
    String amount;
    @NotBlank
    List<Role> role;
}
