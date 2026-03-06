package org.example.performancedemo.service;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class BenchmarkController {

    private final BenchmarkService service;

    public BenchmarkController(BenchmarkService service) {
        this.service = service;
    }

    @GetMapping("/bench")
    public Map<String, Long> test() {
        Map<String, Long> result = new HashMap<>();

        result.put("postgres_insert_100", service.insertJpa(100));
        result.put("mongo_insert_100", service.insertMongo(100));

        result.put("postgres_batch_insert_1000", service.insertBatchJpa(1000));
        result.put("mongo_batch_insert_1000", service.insertBatchMongo(1000));

        result.put("postgres_read_by_id", service.readByIdJpa());
        result.put("mongo_read_by_id", service.readByIdMongo());

        result.put("postgres_filter", service.filterJpa());
        result.put("mongo_filter", service.filterMongo());

        return result;
    }
}