package org.example.performancedemo.repository.jpa;

import org.example.performancedemo.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewJpaRepository extends JpaRepository<Review, String> {
    List<Review> findByProductId(String productId);
}