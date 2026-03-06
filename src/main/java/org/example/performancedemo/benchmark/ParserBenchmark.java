package org.example.performancedemo.benchmark;

import org.openjdk.jmh.annotations.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Thread)
public class ParserBenchmark {

    private List<String> data;

    @Setup
    public void setup() {
        data = new ArrayList<>();
        for (int i = 0; i < 10000; i++) {
            data.add("product" + i + "," + (i % 5) + ",good review");
        }
    }

    @Benchmark
    public int parseWithFor() {
        int count = 0;
        for (String line : data) {
            String[] parts = line.split(",");
            if (parts.length == 3) count++;
        }
        return count;
    }

    @Benchmark
    public long parseWithStream() {
        return data.stream()
                .filter(line -> line.split(",").length == 3)
                .count();
    }

    @Benchmark
    public long parseWithParallelStream() {
        return data.parallelStream()
                .filter(line -> line.split(",").length == 3)
                .count();
    }
}