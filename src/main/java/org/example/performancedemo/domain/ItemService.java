package org.example.performancedemo.domain;

import java.util.List;

public class ItemService {

    public double calculateAverage(List<Integer> values) {
        if (values == null || values.isEmpty()) {
            return 0;
        }

        int sum = 0;

        for (int v : values) {
            sum += v;
        }

        return (double) sum / values.size();
    }
}