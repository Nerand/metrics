package org.example.performancedemo;

import org.springframework.web.bind.annotation.*;

@RestController
public class ItemController {

    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping("/api/items")
    public String getItems(
            @RequestParam int page,
            @RequestParam int size
    ) {
        return itemService.getItems(page, size);
    }
}