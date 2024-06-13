package com.pakskiy.auth.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class HelloRestController {
    @GetMapping("/public")
    public Mono<String> publicEndpoint() {
        return Mono.just("This is a public endpoint");
    }

    @GetMapping("/private")
    public Mono<String> privateEndpoint() {
        return Mono.just("This is a private endpoint");
    }
}
