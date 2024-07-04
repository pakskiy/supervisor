package com.pakskiy.supervisor.service;

import com.pakskiy.supervisor.dto.LoginRequestDto;
import com.pakskiy.supervisor.dto.LoginResponseDto;
import com.pakskiy.supervisor.dto.RegisterRequestDto;
import com.pakskiy.supervisor.dto.RegisterResponseDto;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

public interface AuthService {
    Mono<ResponseEntity<RegisterResponseDto>> register(RegisterRequestDto registerRequestDto);

    Mono<ResponseEntity<LoginResponseDto>> login(LoginRequestDto request);
}
