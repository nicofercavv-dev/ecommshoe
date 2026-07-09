package com.nicofercavv.catalog_service.infrastructure.repository;

import com.nicofercavv.catalog_service.domain.model.Sneaker;

import java.util.List;

public interface CustomProductRepository {
    List<Sneaker> vectorSearch(List<Double> embedding, int maxResults);
}
