package com.banking.accountmicroservice.entity;

import com.banking.accountmicroservice.enums.AccountType;
import com.banking.accountmicroservice.enums.Status;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(value = "account_table")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Account {
    @Id
    @Generated
    private String accountId;
    @Indexed(unique = true)
    private String userId;
    private AccountType accountType;
    private Integer amount;
    private Status status;
    LocalDateTime createAt;
    LocalDateTime updatedAt;
}
