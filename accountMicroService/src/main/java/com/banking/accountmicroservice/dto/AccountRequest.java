package com.banking.accountmicroservice.dto;

import com.banking.accountmicroservice.enums.AccountType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountRequest {
    private String userId;
    private AccountType accountType;
    private Integer amount;
}
