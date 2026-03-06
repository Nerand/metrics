package org.example.performancedemo;

import java.util.ArrayList;
import java.util.List;

public class MemoryLeakDemo {
    private static final List<byte[]> leak = new ArrayList<>();

    public static void main(String[] args) throws Exception {
        int counter = 0;

        while (true) {
            leak.add(new byte[512 * 1024]);
            counter++;

            if (counter % 20 == 0) {
                System.out.println("Allocated blocks: " + counter +
                        " | Stored blocks: " + leak.size());
            }

            Thread.sleep(150);
        }
    }
}