package com.pakskiy.supervisor.utils;

import com.pakskiy.supervisor.dto.LoginRequestDto;
import com.pakskiy.supervisor.dto.RegisterRequestDto;

public class UserUtils {
    public static RegisterRequestDto getJsonForRegistration() {
        return RegisterRequestDto.builder()
                .username("test001")
                .password("test001")
                .email("test001@gmail.com")
                .firstName("test001FirstName")
                .lastName("test001LastName")
                .build();
    }

    public static RegisterRequestDto getJsonForRegistrationForLogin() {
        return RegisterRequestDto.builder()
                .username("testLogin")
                .password("testLogin")
                .email("testLogin@gmail.com")
                .firstName("testLoginFirstName")
                .lastName("testLoginLastName")
                .build();
    }

    public static LoginRequestDto getJsonForLoginSuccess() {
        return LoginRequestDto.builder()
                .username("testLogin")
                .password("testLogin")
                .build();
    }

    public static LoginRequestDto getJsonForLoginFail() {
        return LoginRequestDto.builder()
                .username("test001")
                .password("test001")
                .build();
    }


}