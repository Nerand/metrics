package org.example.performancedemo.api;

import org.example.performancedemo.service.ParserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ParserController {

    private final ParserService parserService;

    public ParserController(ParserService parserService) {
        this.parserService = parserService;
    }

    @GetMapping("/parse-test")
    public String runParser() {

        List<String> data = new ArrayList<>();

        for (int i = 0; i < 500; i++) {
            data.add("product" + i + "," + (i % 5) + ",good review");
        }

        parserService.parse(data);

        return "Parsing finished";
    }
    @GetMapping("/parse-error-test")
    public String runParserWithError() {
        List<String> data = new ArrayList<>();
        data.add("broken_line");
        parserService.parse(data);
        return "Parsing with error finished";
    }
}