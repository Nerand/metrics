package org.example.performancedemo;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class ItemService {

    @Cacheable("items")
    public String getItems(int page, int size) {
        try {
            Thread.sleep(2000);   // имитация медленного источника данных
        } catch (Exception ignored) {}

        return "items page=" + page + " size=" + size;
    }
}