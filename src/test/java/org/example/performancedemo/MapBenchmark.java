package org.example.performancedemo;

import org.openjdk.jmh.annotations.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.SECONDS)
@Warmup(iterations = 2, time = 1)
@Measurement(iterations = 3, time = 1)
@Fork(1)
@State(Scope.Thread)
public class MapBenchmark {

    private Map<Integer, Integer> concurrent;
    private Map<Integer, Integer> syncMap;

    @Setup
    public void setup() {
        concurrent = new ConcurrentHashMap<>();
        syncMap = Collections.synchronizedMap(new HashMap<>());
        for (int i = 0; i < 1000; i++) {
            concurrent.put(i, i);
            syncMap.put(i, i);
        }
    }

    @Benchmark
    public int concurrent_get_put() {
        int key = 123;
        int v = concurrent.get(key);
        concurrent.put(key, v + 1);
        return v;
    }

    @Benchmark
    public int sync_get_put() {
        int key = 123;
        int v = syncMap.get(key);
        syncMap.put(key, v + 1);
        return v;
    }
}