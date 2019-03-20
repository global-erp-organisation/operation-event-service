package com.ia.operation.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.ia.operation.documents.User;

import reactor.core.publisher.Mono;

public interface UserRepository extends ReactiveMongoRepository<User, String>{
    Mono<User> findByEmail(String email);
}
