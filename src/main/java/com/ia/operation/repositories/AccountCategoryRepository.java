package com.ia.operation.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.ia.operation.documents.AccountCategory;

public interface AccountCategoryRepository extends ReactiveMongoRepository<AccountCategory, String>{

}
