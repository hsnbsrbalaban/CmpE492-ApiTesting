package com.example.demoapi.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BaseController {
    private static final String prefix = "Hello ";

    @GetMapping("/greeting")
    public String greeting(@RequestParam(value = "name", defaultValue = "World!") String name) {

        return prefix + name;
    }
}
