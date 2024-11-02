package com.banking.accountmicroservice.dto;

import com.banking.accountmicroservice.enums.AccountType;
import com.banking.accountmicroservice.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@Data
@NoArgsConstructor
public class UpdateRequest {
    private String accountId;
    private String userId;
    private AccountType accountType;
    private Status status;
}
