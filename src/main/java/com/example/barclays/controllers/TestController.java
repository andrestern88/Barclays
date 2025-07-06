package com.example.barclays.controllers;

import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/v1/test")
public class TestController {
    @GetMapping("/all")
    public String allAccess() {
        return "Public Content.";
    }
    @GetMapping("/user")
    public String userAccess() {
        return "User Content.";
    }
}