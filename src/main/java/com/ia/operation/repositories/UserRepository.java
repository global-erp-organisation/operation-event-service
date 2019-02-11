package com.ia.operation.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.ia.operation.documents.User;

public interface UserRepository extends ReactiveMongoRepository<User, String>{

}
