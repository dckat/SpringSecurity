package com.meerkat.ss.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @PostMapping("/a")
    public String postEndPointA() {
        return "Works";
    }

    @GetMapping("/a")
    public String getEndPointA() {
        return "Works";
    }

    @GetMapping("/a/b")
    public String getEndPointB() {
        return "Works";
    }

    @GetMapping("/a/b/c")
    public String getEndPointC() {
        return "Works";
    }

    @GetMapping("/product/{code}")
    public String productCode(@PathVariable String code) {
        return code;
    }
}
