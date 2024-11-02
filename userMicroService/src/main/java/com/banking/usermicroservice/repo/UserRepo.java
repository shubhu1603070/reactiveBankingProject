package com.banking.usermicroservice.repo;


import com.banking.usermicroservice.entities.User;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface UserRepo extends ReactiveMongoRepository<User,String> {

    Mono<User> findByEmail(String email);

    Mono<User> findByUserName(String username);
}
