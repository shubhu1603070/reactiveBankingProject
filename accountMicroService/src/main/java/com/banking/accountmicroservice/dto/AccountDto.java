package com.banking.accountmicroservice.dto;

import com.banking.accountmicroservice.enums.AccountType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountDto {
    private String userId;
    private String accountId;
    private AccountType accountType;
    private Integer amount;
    private LocalDateTime creationDateTime;
}
