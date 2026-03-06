package org.example.performancedemo.service;

import org.example.performancedemo.model.Review;
import org.example.performancedemo.repository.jpa.ReviewJpaRepository;
import org.example.performancedemo.repository.mongo.ReviewMongoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class BenchmarkService {

    private final ReviewJpaRepository jpa;
    private final ReviewMongoRepository mongo;

    public BenchmarkService(ReviewJpaRepository jpa, ReviewMongoRepository mongo) {
        this.jpa = jpa;
        this.mongo = mongo;
    }

    public long insertJpa(int count) {
        long start = System.currentTimeMillis();

        for (int i = 0; i < count; i++) {
            jpa.save(new Review("p1", 5, "good", LocalDateTime.now()));
        }

        return System.currentTimeMillis() - start;
    }

    public long insertMongo(int count) {
        long start = System.currentTimeMillis();

        for (int i = 0; i < count; i++) {
            mongo.save(new Review("p1", 5, "good", LocalDateTime.now()));
        }

        return System.currentTimeMillis() - start;
    }

    public long insertBatchJpa(int count) {
        List<Review> reviews = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            reviews.add(new Review("p2", 4, "batch-jpa", LocalDateTime.now()));
        }

        long start = System.currentTimeMillis();
        jpa.saveAll(reviews);
        return System.currentTimeMillis() - start;
    }

    public long insertBatchMongo(int count) {
        List<Review> reviews = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            reviews.add(new Review("p2", 4, "batch-mongo", LocalDateTime.now()));
        }

        long start = System.currentTimeMillis();
        mongo.saveAll(reviews);
        return System.currentTimeMillis() - start;
    }

    public long readByIdJpa() {
        Review saved = jpa.save(new Review("p3", 5, "read-jpa", LocalDateTime.now()));

        long start = System.currentTimeMillis();
        jpa.findById(saved.getId());
        return System.currentTimeMillis() - start;
    }

    public long readByIdMongo() {
        Review saved = mongo.save(new Review("p3", 5, "read-mongo", LocalDateTime.now()));

        long start = System.currentTimeMillis();
        mongo.findById(saved.getId());
        return System.currentTimeMillis() - start;
    }

    public long filterJpa() {
        for (int i = 0; i < 100; i++) {
            jpa.save(new Review("week1", 3, "filter-jpa", LocalDateTime.now()));
        }

        long start = System.currentTimeMillis();
        jpa.findByProductId("week1");
        return System.currentTimeMillis() - start;
    }

    public long filterMongo() {
        for (int i = 0; i < 100; i++) {
            mongo.save(new Review("week1", 3, "filter-mongo", LocalDateTime.now()));
        }

        long start = System.currentTimeMillis();
        mongo.findByProductId("week1");
        return System.currentTimeMillis() - start;
    }
}