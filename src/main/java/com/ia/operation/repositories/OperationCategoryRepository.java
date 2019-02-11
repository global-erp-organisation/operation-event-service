package com.ia.operation.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.ia.operation.documents.OperationCategory;

public interface OperationCategoryRepository extends ReactiveMongoRepository<OperationCategory, String>{

}
