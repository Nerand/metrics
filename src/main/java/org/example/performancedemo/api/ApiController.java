package org.example.performancedemo.api;

import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

@RestController
@RequestMapping("/api")
public class ApiController {



    // WRITE: имитация записи (POST), возвращает id + echo
    @PostMapping("/items")
    public Map<String, Object> createItem(@RequestBody Map<String, Object> body) throws InterruptedException {
        Thread.sleep(ThreadLocalRandom.current().nextInt(20, 90));
        return Map.of(
                "id", ThreadLocalRandom.current().nextLong(1_000_000),
                "saved", true,
                "name", body.getOrDefault("name", "unknown"),
                "ts", Instant.now().toString()
        );
    }
}