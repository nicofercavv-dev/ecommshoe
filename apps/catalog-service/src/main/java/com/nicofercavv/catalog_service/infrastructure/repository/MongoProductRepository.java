package com.nicofercavv.catalog_service.infrastructure.repository;

import com.nicofercavv.catalog_service.domain.model.Sneaker;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MongoProductRepository extends MongoRepository<Sneaker, String>, CustomProductRepository {
}
