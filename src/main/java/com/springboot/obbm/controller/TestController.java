package com.springboot.obbm.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @GetMapping("/abc")
    public String abc() {
        System.out.println("abc");
        return "API is working";
    }
}
