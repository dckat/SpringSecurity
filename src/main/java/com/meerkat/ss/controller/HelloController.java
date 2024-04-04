package com.meerkat.ss.controller;

import org.springframework.scheduling.annotation.Async;
import org.springframework.security.concurrent.DelegatingSecurityContextCallable;
import org.springframework.security.concurrent.DelegatingSecurityContextExecutorService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
public class HelloController {

    @GetMapping("/hello")
    public String hello() {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication a = context.getAuthentication();

        return "Hello, " + a.getName() + "!";
    }

    @GetMapping("bye")
    @Async
    public void goodbye() {
        SecurityContext context = SecurityContextHolder.getContext();
        String username = context.getAuthentication().getName();
    }

    @GetMapping("/ciao")
    public String ciao() throws Exception {
        Callable<String> task = () -> {
            SecurityContext context = SecurityContextHolder.getContext();
            return context.getAuthentication().getName();
        };

        ExecutorService executorService = Executors.newCachedThreadPool();

        try {
            var contextTask = new DelegatingSecurityContextCallable<>(task);
            return "Ciao, " + executorService.submit(contextTask).get() + "!";
        } finally {
            executorService.shutdown();
        }
    }

    @GetMapping("/hola")
    public String hola() throws Exception {
        Callable<String> task = () -> {
            SecurityContext context = SecurityContextHolder.getContext();
            return context.getAuthentication().getName();
        };

        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService = new DelegatingSecurityContextExecutorService(executorService);

        try {
            return "Hola, " + executorService.submit(task).get() + "!";
        } finally {
            executorService.shutdown();
        }
    }
}
