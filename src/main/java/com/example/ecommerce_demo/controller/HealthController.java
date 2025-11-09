package com.example.ecommerce_demo.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
public class HealthController {
    @GetMapping("/ping")
    public String ping() {
        return "E-commerce service is up 🚀";
    }
}
