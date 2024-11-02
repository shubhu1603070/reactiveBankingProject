package com.banking.accountmicroservice.service;

import com.banking.accountmicroservice.dto.AccountDto;
import com.banking.accountmicroservice.dto.AccountRequest;
import com.banking.accountmicroservice.dto.UpdateRequest;
import com.banking.accountmicroservice.entity.Account;
import com.banking.accountmicroservice.enums.ErrorType;
import com.banking.accountmicroservice.enums.Status;
import com.banking.accountmicroservice.exception.FieldMissingException;
import com.banking.accountmicroservice.exception.RequestInvalidException;
import com.banking.accountmicroservice.exception.UserNotFoundException;
import com.banking.accountmicroservice.repo.AccountRepo;
import com.banking.accountmicroservice.utils.AppUtils;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AccountService {


    private final AccountRepo accountRepo;

    public Mono<AccountDto> createAccount(AccountRequest accountRequest){
        return Mono.justOrEmpty(accountRequest)
                .map(AppUtils::convertRequestToEntity)
                .map(account -> {
                    setCreatedAt(account);
                    setUpdatedAt(account);
                    account.setStatus(Status.PENDING);
                    return account;
                }).flatMap(accountRepo::save)
                .map(AppUtils::convertEntityToDto);
    }

    public Mono<AccountDto> updateAccount(UpdateRequest updateRequest) {
        return Mono.defer(() -> {
            if(StringUtils.isNotBlank(updateRequest.getAccountId())){
                return updateUsingAccountId(updateRequest.getAccountId(), updateRequest);
            }else if(StringUtils.isNotBlank(updateRequest.getUserId())){
                return updateUsingUserId(updateRequest.getUserId(),updateRequest);
            }else{
                return Mono.error(new FieldMissingException("Please provide either userId/accountId", ErrorType.FIELD_MISSING.name()));
            }
        }).switchIfEmpty(Mono.error(new RequestInvalidException("Please give proper request",ErrorType.REQUEST_NOT_VALID.name())));

    }

    public Mono<Void> deleteAccount(String userId, String accountId) {
        return Mono.defer(() -> {
            if(StringUtils.isNotBlank(accountId)){
                return accountRepo.deleteById(accountId);
            }else if(StringUtils.isNotBlank(userId)){
                return accountRepo.deleteByUserId(userId);
            }else{
                return Mono.error(new FieldMissingException("Please provide either userId/accountId", ErrorType.FIELD_MISSING.name()));
            }
        });
    }

    public Mono<AccountDto> getAccountDetails(String accountId, String userId) {
        return Mono.defer(() -> {
            if(StringUtils.isNotBlank(accountId)){
                return accountRepo.findById(accountId)
                        .map(AppUtils::convertEntityToDto);
            }else if(StringUtils.isNotBlank(userId)){
                return accountRepo.findByUserId(userId)
                        .map(AppUtils::convertEntityToDto);
            }else{
                return Mono.error(new FieldMissingException("Please provide either userId/accountId", ErrorType.FIELD_MISSING.name()));
            }
        });
    }

    public Mono<Integer> getAccountBalance(String accountId, String userId){
        return Mono.defer(() -> {
            if(StringUtils.isNotBlank(accountId)){
                return accountRepo.findById(accountId)
                        .map(Account::getAmount)
                        .switchIfEmpty(Mono.error(new UserNotFoundException("User not found with userId: " + userId, ErrorType.NOT_FOUND.name())));
            }else if(StringUtils.isNotBlank(userId)){
                return accountRepo.findByUserId(userId)
                        .map(Account::getAmount)
                        .switchIfEmpty(Mono.error(new UserNotFoundException("User not found with userId: " + userId, ErrorType.NOT_FOUND.name())));
            }else{
                return Mono.error(new FieldMissingException("Please provide either userId/accountId", ErrorType.FIELD_MISSING.name()));
            }
        });
    }

    public Mono<Object> checkStatusOfAccount(String accountId, String userId) {
        return Mono.defer(() -> {
            if(StringUtils.isNotBlank(accountId)){
                return accountRepo.findById(accountId)
                        .map(account -> {
                            return Collections.singletonMap("Valid",this.check(account));
                        })
                        .switchIfEmpty(Mono.error(new UserNotFoundException("User not found with "+accountId,ErrorType.NOT_FOUND.name())));
            }else if(StringUtils.isNotBlank(userId)){
                return accountRepo.findByUserId(userId)
                        .map(account -> {
                            return Collections.singletonMap("Valid",this.check(account));
                        })
                        .switchIfEmpty(Mono.error(new UserNotFoundException("User not found with "+accountId,ErrorType.NOT_FOUND.name())));
            }else{
                return Mono.error(new FieldMissingException("Please provide either userId/accountId", ErrorType.FIELD_MISSING.name()));
            }
        });
    }

    private boolean check(Account account){
        return (Status.valueOf("PENDING").equals(account.getStatus())
                || Status.valueOf("ACTIVE").equals(account.getStatus()))
                && account.getAmount() >= 0;
    }

    private Mono<AccountDto> updateUsingAccountId(String accountId,UpdateRequest updateRequest) {
        return accountRepo
                .findById(accountId)
                .map(account -> {
                    setUpdatedAt(account);
                    extracted(updateRequest, account);
                    return account;
                }).flatMap(accountRepo::save)
                .map(AppUtils::convertEntityToDto)
                .switchIfEmpty(Mono.error(new UserNotFoundException("User not found with accountId: " + accountId, ErrorType.NOT_FOUND.name())));
    }

    private Mono<AccountDto> updateUsingUserId(String userId,UpdateRequest updateRequest) {
        return accountRepo
                .findByUserId(userId)
                .map(account -> {
                    setUpdatedAt(account);
                    extracted(updateRequest, account);
                    return account;
                }).flatMap(accountRepo::save)
                .map(AppUtils::convertEntityToDto)
                .switchIfEmpty(Mono.error(new UserNotFoundException("User not found with userId: " + userId, ErrorType.NOT_FOUND.name())));
    }

    private static void extracted(UpdateRequest updateRequest, Account account) {
        if(updateRequest.getAccountType()!=null){
            account.setAccountType(updateRequest.getAccountType());
        }
        if(updateRequest.getStatus()!=null){
            account.setStatus(updateRequest.getStatus());
        }
    }

    private static void setCreatedAt(Account account){
        account.setCreateAt(LocalDateTime.now());
    }

    private static void setUpdatedAt(Account account){
        account.setUpdatedAt(LocalDateTime.now());
    }

}
