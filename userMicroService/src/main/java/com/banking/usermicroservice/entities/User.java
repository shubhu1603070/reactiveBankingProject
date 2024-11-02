package com.banking.usermicroservice.entities;

import com.banking.usermicroservice.Enums.Role;
import jakarta.validation.constraints.Email;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Document(value = "user_table")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@ToString
public class User{
    @Id
    @Generated
    String userId;
    @Indexed(unique = true)
    String userName;
    String name;
    String password;
    String amount;
    @Indexed(unique = true)
    @Email
    String email;
    LocalDateTime createAt;
    LocalDateTime updatedAt;
    List<Role> role;
}
