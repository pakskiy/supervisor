package com.pakskiy.supervisor.conf;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcReactiveOAuth2UserService;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.userinfo.ReactiveOAuth2UserService;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtGrantedAuthoritiesConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {
    private final String[] publicRoutes = {"/api/v1/auth/register/**", "api/v1/auth/login"};

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(configurer -> configurer
                        .pathMatchers(HttpMethod.OPTIONS).permitAll()
                        .pathMatchers(publicRoutes).permitAll()
                        .pathMatchers("/api/v1/auth/info/**").authenticated()
                        .anyExchange()
                        .authenticated())
                .oauth2ResourceServer(customizer -> customizer.jwt(jwt -> {
                    ReactiveJwtAuthenticationConverter jwtAuthenticationConverter = new ReactiveJwtAuthenticationConverter();
                    jwtAuthenticationConverter.setPrincipalClaimName("preferred_username");
                    JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();

                    // Custom converter for the nested roles
                    Converter<Jwt, Collection<GrantedAuthority>> customConverter = jwt2 -> {
                        Map<String, Object> realmAccess = (Map<String, Object>) jwt2.getClaims().get("realm_access");
                        if (realmAccess == null || realmAccess.isEmpty()) {
                            return new ArrayList<>();
                        }
                        Collection<String> roles = (Collection<String>) realmAccess.get("roles");
                        return roles.stream()
                                .map(roleName -> "ROLE_" + roleName) // Prefixing role name with "ROLE_"
                                .map(SimpleGrantedAuthority::new)
                                .collect(Collectors.toList());
                    };

                    // Combine the default and custom converters
                    jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(
                            new ReactiveJwtGrantedAuthoritiesConverterAdapter(jwt3 -> {
                                Stream<GrantedAuthority> defaultAuthorities = jwtGrantedAuthoritiesConverter.convert(jwt3).stream();
                                Stream<GrantedAuthority> customAuthorities = Objects.requireNonNull(customConverter.convert(jwt3)).stream();
                                return Stream.concat(defaultAuthorities, customAuthorities).collect(Collectors.toList());
                            })
                    );

                    // Set the custom JWT authentication converter
                    jwt.jwtAuthenticationConverter(jwtAuthenticationConverter);


                }))

                .exceptionHandling(exceptionHandlingConfigurer -> exceptionHandlingConfigurer
                        .authenticationEntryPoint((swe, e) -> {
                            log.error("IN securityWebFilterChain - unauthorized error: {}", e.getMessage());
                            return Mono.fromRunnable(() -> swe.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED));
                        })
                        .accessDeniedHandler((swe, e) -> {
                            log.error("IN securityWebFilterChain - access denied: {}", e.getMessage());

                            return Mono.fromRunnable(() -> swe.getResponse().setStatusCode(HttpStatus.FORBIDDEN));
                        }))
                .build();


//                .authorizeHttpRequests(authorise ->
//                        authorise
//                                .requestMatchers("/static/**", "/templates/**")
//                                .permitAll()
//                                .anyRequest()
//                                .authenticated())
//                .csrf(AbstractHttpConfigurer::disable)
//                .exceptionHandling(authorise -> authorise.accessDeniedPage("/access-denied"))
//                .oauth2Login(withDefaults())
//                .logout(logout ->
//                        logout.logoutSuccessHandler(oidcLogoutSuccessHandler)).build();
    }

    @Bean
    public ReactiveOAuth2UserService<OidcUserRequest, OidcUser> oidcUserService() {
        OidcReactiveOAuth2UserService oidcUserService = new OidcReactiveOAuth2UserService();
        return userRequest -> oidcUserService.loadUser(userRequest)
                .map(oidcUser -> {
                    List<GrantedAuthority> grantedAuthorities = Stream.concat(oidcUser.getAuthorities().stream(),
                                    oidcUser.getClaimAsStringList("roles").stream()
                                            .filter(authority -> authority.startsWith("ROLE_"))
                                            .map(SimpleGrantedAuthority::new))
                            .toList();
                    return new DefaultOidcUser(grantedAuthorities, oidcUser.getIdToken(), oidcUser.getUserInfo(),
                            "preferred_username");
                });
    }
}
