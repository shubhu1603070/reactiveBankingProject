package com.banking.accountmicroservice.controller;

import com.banking.accountmicroservice.dto.AccountDto;
import com.banking.accountmicroservice.dto.AccountRequest;
import com.banking.accountmicroservice.dto.UpdateRequest;
import com.banking.accountmicroservice.service.AccountService;
import io.micrometer.common.util.StringUtils;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/account/api/v1")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;


    @GetMapping("/hello")
    public Mono<String> getHello(){
        return Mono.just("Hello Account");
    }

    @PostMapping("/create-account")
    public Mono<AccountDto> createAccount(@RequestBody AccountRequest accountRequest){
        return accountService.createAccount(accountRequest);
    }

    @PostMapping("/update-account")
    public Mono<AccountDto> updateAccount(@RequestBody UpdateRequest updateRequest){
        return accountService.updateAccount(updateRequest);
    }

    @DeleteMapping("/delete-using-userId/{userId}")
    public Mono<Void> deleteAccountByUserId(@PathVariable String userId){
        return accountService.deleteAccount(userId,null);
    }

    @DeleteMapping("/delete-using-accountId/{accountId}")
    public Mono<Void> deleteAccountByAccountId(@PathVariable String accountId){
        return accountService.deleteAccount(null,accountId);
    }

    @GetMapping("/get-details-userId/{userId}")
    public Mono<AccountDto> getAccountDetailsByUserId(@PathVariable String userId){
        return accountService.getAccountDetails(null,userId);
    }

    @GetMapping("/get-details-accountId/{accountId}")
    public Mono<AccountDto> getAccountDetailsByAccountId(@PathVariable String accountId){
        return accountService.getAccountDetails(accountId,null);
    }

    @GetMapping("/get-balance-userId/{userId}")
    public Mono<Integer> getBalanceByUserId(@PathVariable String userId){
        return accountService.getAccountBalance(null,userId);
    }

    @GetMapping("/get-balance-accountId/{accountId}")
    public Mono<Integer> getBalanceByAccountId(@PathVariable String accountId){
        return accountService.getAccountBalance(accountId,null);
    }

    @GetMapping("/check-account-valid-accountId/{accountId}")
    public Mono<Object> checkAccountStatusWithAccountId(@PathVariable String accountId){
        return accountService.checkStatusOfAccount(accountId,null);
    }

    @GetMapping("/check-account-valid-userId/{userId}")
    public Mono<Object> checkAccountStatusWithUserId(@PathVariable String userId){
        return accountService.checkStatusOfAccount(null,userId);
    }

}
