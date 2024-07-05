package com.pakskiy.supervisor.rest;

import com.pakskiy.supervisor.dto.InfoResponseDto;
import com.pakskiy.supervisor.dto.LoginRequestDto;
import com.pakskiy.supervisor.dto.LoginResponseDto;
import com.pakskiy.supervisor.dto.RegisterRequestDto;
import com.pakskiy.supervisor.dto.RegisterResponseDto;
import com.pakskiy.supervisor.service.AuthService;
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

import java.security.Principal;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/user")
public class UserRestControllerV1 {
    private final AuthService authService;

    @PostMapping("/register")
    public Mono<ResponseEntity<RegisterResponseDto>> register(@Valid @RequestBody RegisterRequestDto request) {
        return authService.register(request);
    }

    @PostMapping("/login")
    public Mono<ResponseEntity<LoginResponseDto>> login(@Valid @RequestBody LoginRequestDto request) {
        return authService.login(request);
    }

    @GetMapping("/info")
    public Mono<ResponseEntity<InfoResponseDto>> info(Mono<Principal> principal) {
        return authService.info(principal);
    }

    @GetMapping("/private")
    public Mono<String> privateEndpoint() {
        return Mono.just("This is a private endpoint");
    }
}
