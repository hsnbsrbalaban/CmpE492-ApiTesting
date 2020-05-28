package com.example.demoapi.controller;

import com.example.demoapi.model.Request;
import com.example.demoapi.model.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;

@RestController
public class BaseController {
    private static final String prefix = "Hello ";
    private static final String longPrefix = "Hellooo ";

    @GetMapping("/greeting")
    public String greeting(@RequestParam(value = "name", defaultValue = "World!") String name) {
        return prefix + name;
    }

    @GetMapping("/greetingWithWait")
    public String greetingWithWait(@RequestParam(value = "name", defaultValue = "Wooorld!") String name) throws InterruptedException {
        TimeUnit.SECONDS.sleep(3);
        return longPrefix + name;
    }

    @PostMapping("/greeting")
    public ResponseEntity greetingPost(@RequestBody Request request) {
        String name = StringUtils.isEmpty(request.getName()) ? "World!" : request.getName();

        Response response = new Response();
        response.setReply(prefix + name);

        return new ResponseEntity(response, HttpStatus.OK);
    }

    @PostMapping("/greetingWithWait")
    public ResponseEntity greetingWithWaitPost(@RequestBody Request request) throws InterruptedException {
        String name = StringUtils.isEmpty(request.getName()) ? "Wooorld!" : request.getName();

        Response response = new Response();
        response.setReply(longPrefix + name);

        TimeUnit.SECONDS.sleep(3);
        return new ResponseEntity(response, HttpStatus.OK);
    }
}
