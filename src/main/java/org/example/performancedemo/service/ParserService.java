package org.example.performancedemo.service;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.example.performancedemo.model.Review;
import org.example.performancedemo.repository.jpa.ReviewJpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ParserService {

    private final ReviewJpaRepository repository;

    private final Counter successCounter;
    private final Counter errorCounter;
    private final Counter savedRecordsCounter;
    private final Timer parseTimer;

    public ParserService(ReviewJpaRepository repository, MeterRegistry registry) {
        this.repository = repository;
        this.successCounter = registry.counter("parser.success");
        this.errorCounter = registry.counter("parser.errors");
        this.savedRecordsCounter = registry.counter("parser.records.saved");
        this.parseTimer = registry.timer("parser.execution.time");
    }

    @Transactional
    public void parse(List<String> data) {
        parseTimer.record(() -> {
            try {
                List<Review> reviews = new ArrayList<>();

                for (String line : data) {
                    String[] parts = line.split(",");

                    Review r = new Review(
                            parts[0],
                            Integer.parseInt(parts[1]),
                            parts[2],
                            LocalDateTime.now()
                    );

                    reviews.add(r);
                }

                repository.saveAll(reviews);
                savedRecordsCounter.increment(reviews.size());
                successCounter.increment();

            } catch (Exception e) {
                errorCounter.increment();
            }
        });
    }
}