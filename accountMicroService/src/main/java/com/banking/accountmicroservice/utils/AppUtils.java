package com.banking.accountmicroservice.utils;

import com.banking.accountmicroservice.dto.AccountDto;
import com.banking.accountmicroservice.dto.AccountRequest;
import com.banking.accountmicroservice.entity.Account;
import com.banking.accountmicroservice.enums.Status;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class AppUtils {

    public static AccountDto convertEntityToDto(Account account){
        return AccountDto.builder()
                .accountId(account.getAccountId())
                .accountType(account.getAccountType())
                .userId(account.getUserId())
                .creationDateTime(account.getCreateAt())
                .amount(account.getAmount())
                .build();
    }

    public static Account convertDtoToEntity(AccountDto accountDto){
        return Account.builder()
                        .accountId(accountDto.getAccountId())
                        .accountType(accountDto.getAccountType())
                        .userId(accountDto.getUserId())
                        .amount(accountDto.getAmount())
                .build();
    }


    public static Account convertRequestToEntity(AccountRequest accountRequest) {
        return Account.builder()
                .userId(accountRequest.getUserId())
                .accountType(accountRequest.getAccountType())
                .amount(accountRequest.getAmount())
                .build();
    }
}
