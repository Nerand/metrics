package org.example.performancedemo.domain;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ItemServiceTest {

    @Test
    void testAverage() {
        ItemService service = new ItemService();
        double result = service.calculateAverage(List.of(10, 20, 30));
        assertEquals(20.0, result, 0.001);
    }
}