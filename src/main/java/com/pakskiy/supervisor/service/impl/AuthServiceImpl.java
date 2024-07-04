package com.pakskiy.supervisor.service.impl;

import com.pakskiy.supervisor.dto.LoginRequestDto;
import com.pakskiy.supervisor.dto.LoginResponseDto;
import com.pakskiy.supervisor.dto.RegisterRequestDto;
import com.pakskiy.supervisor.dto.RegisterResponseDto;
import com.pakskiy.supervisor.service.AuthService;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.RolesResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.apache.http.util.TextUtils.isEmpty;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.urls.auth}")
    @Setter
    private String authServerUrl;

    @Value("${keycloak.clientId}")
    @Setter
    private String clientId;

    @Value("${keycloak.clientSecret}")
    @Setter
    private String clientSecret;

    private final Keycloak keycloak;

    public Keycloak keycloakForAuth(LoginRequestDto request) {
        return KeycloakBuilder.builder()
                .serverUrl(authServerUrl)
                .realm(realm)
                .grantType(OAuth2Constants.PASSWORD)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .username(request.getUsername())
                .password(request.getPassword())
                .build();
    }

    @Override
    public Mono<ResponseEntity<RegisterResponseDto>> register(RegisterRequestDto registerRequestDto) {
        return Mono.defer(() -> registerUser(registerRequestDto, "USERS"))
                .map(registeredUser -> ResponseEntity
                        .status(HttpStatus.CREATED)
                        .body(RegisterResponseDto.builder()
                                .username(registerRequestDto.getUsername())
                                .email(registerRequestDto.getEmail())
                                .message("User successfully registered!")
                                .build()))
                .onErrorMap(ex -> new RuntimeException(ex.getMessage()));
    }

    @Override
    public Mono<ResponseEntity<LoginResponseDto>> login(LoginRequestDto request) {
        try {
            if (isEmpty(request.getUsername()) || isEmpty(request.getPassword())) {
                return Mono.error(new IllegalArgumentException("Invalid credentials"));
            }

            AccessTokenResponse token = keycloakForAuth(request).tokenManager().getAccessToken();

            return Mono.just(ResponseEntity.status(HttpStatus.OK).body(LoginResponseDto.builder()
                    .accessToken(token.getToken())
                    .expiresIn(token.getExpiresIn())
                    .refreshToken(token.getRefreshToken())
                    .tokenType(token.getTokenType())
                    .build()));
        } catch (Exception e) {
            return Mono.error(new IllegalArgumentException("Invalid credentials"));
        }
    }

    public Mono<String> registerUser(RegisterRequestDto registration, String roleName) {
        if (isEmpty(registration.getUsername()) || isEmpty(registration.getPassword()) || isEmpty(registration.getEmail())) {
            return Mono.error(new IllegalArgumentException("Invalid credentials"));
        }

        Response response = getResponse(registration);
        if (response.getStatus() != 201) {
            return Mono.error(new IllegalArgumentException("Invalid credentials"));
        }
        URI uri = response.getLocation();

        String createdUserId = uri.getPath().substring(uri.getPath().lastIndexOf('/') + 1);
        log.info("Created user {}", createdUserId);

        assignRole(createdUserId, roleName);
        return Mono.just(createdUserId);
    }

    private Response getResponse(RegisterRequestDto registration) {
        UserRepresentation user = new UserRepresentation();
        user.setEnabled(true);
        user.setUsername(registration.getUsername());
        user.setEmail(registration.getEmail());
        user.setFirstName(registration.getFirstName());
        user.setLastName(registration.getLastName());
        user.setEmailVerified(true);

        CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
        credentialRepresentation.setValue(registration.getPassword());
        credentialRepresentation.setTemporary(false);
        credentialRepresentation.setType(CredentialRepresentation.PASSWORD);


        List<CredentialRepresentation> list = new ArrayList<>();
        list.add(credentialRepresentation);
        user.setCredentials(list);

        UsersResource usersResource = getUsersResource();

        Response response = usersResource.create(user);
        return response;
    }

    private UsersResource getUsersResource() {
        RealmResource realm1 = keycloak.realm(realm);
        return realm1.users();
    }

    public Mono<UserResource> getUserResource(String userId) {
        UsersResource usersResource = getUsersResource();
        return Mono.just(usersResource.get(userId));
    }

    public Mono<Void> assignRole(String userId, String roleName) {
        UserResource userResource = getUserResource(userId).block();
        RolesResource rolesResource = getRolesResource();
        RoleRepresentation representation = rolesResource.get(roleName).toRepresentation();

        log.info(rolesResource.get(roleName).toRepresentation().toString());
        userResource.roles().realmLevel().add(Collections.singletonList(representation));
        return Mono.empty();
    }

    private RolesResource getRolesResource() {
        return keycloak.realm(realm).roles();
    }


}
