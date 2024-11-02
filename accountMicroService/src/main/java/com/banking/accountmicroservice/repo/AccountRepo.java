package com.banking.accountmicroservice.repo;

import com.banking.accountmicroservice.entity.Account;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface AccountRepo extends ReactiveMongoRepository<Account,String> {
    Mono<Account> findByUserId(String userId);

    Mono<Void> deleteByUserId(String userId);
}
