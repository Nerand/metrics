package org.example.performancedemo.repository.mongo;

import org.example.performancedemo.model.Review;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ReviewMongoRepository extends MongoRepository<Review, String> {
    List<Review> findByProductId(String productId);
}