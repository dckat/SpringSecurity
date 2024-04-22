package com.meerkat.ss.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.logging.Logger;

@Controller
public class MainController {

    private Logger logger = Logger.getLogger(MainController.class.getName());

    @GetMapping("/")
    public String main() {
        return "main.html";
    }

    // CORS 작동 확인 테스트 메소드
    @PostMapping("/test")
    @ResponseBody
    public String test() {
        logger.info("Test 메소드 호출");
        return "Hello";
    }
}
