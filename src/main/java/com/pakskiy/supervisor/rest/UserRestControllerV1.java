package com.pakskiy.supervisor.rest;

import com.pakskiy.supervisor.dto.RegisterRequestDto;
import com.pakskiy.supervisor.dto.RegisterResponseDto;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/api/v1/user")
@AllArgsConstructor
public class UserRestControllerV1 {
    @PostMapping
    public Mono<ResponseEntity<RegisterResponseDto>> register(@Valid @RequestBody RegisterRequestDto registerRequestDto) {

    }

    @GetMapping("/public")
    public Mono<String> publicEndpoint() {
        return Mono.just("This is a public endpoint");
    }

    @GetMapping("/private")
    public Mono<String> privateEndpoint() {
        return Mono.just("This is a private endpoint");
    }
}
