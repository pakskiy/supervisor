package com.pakskiy.supervisor;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuthApplicationTests {


    @Test
    @DisplayName("Load context")
    void loadContext() {
    }



}



//package com.pakskiy.supervisor;
//
//import dasniko.testcontainers.keycloak.KeycloakContainer;
//import lombok.RequiredArgsConstructor;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.MethodOrderer;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.TestMethodOrder;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.test.web.reactive.server.WebTestClient;
//import org.testcontainers.junit.jupiter.Container;
//import org.testcontainers.junit.jupiter.Testcontainers;
//
////@RunWith(SpringRunner.class)
//@SpringBootTest
//@ActiveProfiles("test")
//@Testcontainers
//@RequiredArgsConstructor
//@AutoConfigureWebTestClient
//@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
//class AuthApplicationTests {
//
//    @Autowired
//    private WebTestClient webClient;
//
//    private static final String KEYCLOAK_CLIENT_ID = "app-auth-client-id";
//    private static final String KEYCLOAK_CLIENT_SECRET = "FuXDordwvRlgaCasQ1xsqi6s9pkAuQPI";
//
//    @Container
//    static KeycloakContainer keycloak = new KeycloakContainer("quay.io/keycloak/keycloak:24.0.4")
//            .withRealmImportFile("realm-export-test.json")
//            .withEnv("DB_VENDOR", "h2")
//            .withEnv("DB_URL", "jdbc:h2:mem:testdb")
//            .withEnv("DB_USER", "sa")
//            .withEnv("DB_PASSWORD", "");
//
//
//    @BeforeAll
//    static void beforeAll() {
//
////        System.setProperty("spring.r2dbc.url", "r2dbc:postgresql://localhost:" + postgresqlContainer.getFirstMappedPort() + "/paymentProvider");
//        System.setProperty("spring.security.oauth2.resourceserver.jwt.issuer-uri", keycloak.getAuthServerUrl() + "/realms/appauth");
//        System.setProperty("keycloak-service.client.base-admin-url", keycloak.getAuthServerUrl() + "/admin/realms/appauth");
//        System.setProperty("keycloak.clientId", KEYCLOAK_CLIENT_ID);
//        System.setProperty("keycloak.clientSecret", KEYCLOAK_CLIENT_SECRET);
//        System.setProperty("keycloak.urls.auth", keycloak.getAuthServerUrl());
//        keycloak.start();
//    }
//
////    @BeforeAll
////    static void beforeAll() {
////        AuthApplicationTests.postgresqlContainer.start();
////        System.setProperty("spring.r2dbc.url", "r2dbc:postgresql://localhost:" + postgresqlContainer.getFirstMappedPort() + "/paymentProvider");
////        System.setProperty("spring.flyway.url", "jdbc:postgresql://localhost:" + postgresqlContainer.getFirstMappedPort() + "/paymentProvider");
//////        merchantRequestDto.setLogin(login);
//////        merchantRequestDto.setKey(key);
//////        exchange = Mockito.mock(ServerWebExchange.class);
//////        when(exchange.getAttribute("merchantId")).thenReturn(1L);
//////        when(exchange.getAttribute("accountId")).thenReturn(1L);
////
////
////
////    }
//
////    @DynamicPropertySource
////    static void registerResourceServerIssuerProperty(DynamicPropertyRegistry registry) {
////        registry.add("spring.security.oauth2.resourceserver.jwt.issuer-uri", () -> keycloak.getAuthServerUrl() + "/realms/appauth");
////        registry.add("keycloak-service.client.base-admin-url", () -> keycloak.getAuthServerUrl() + "/admin/realms/appauth");
////        registry.add("keycloak.clientId", () -> KEYCLOAK_CLIENT_ID);
////        registry.add("keycloak.clientSecret", () -> KEYCLOAK_CLIENT_SECRET);
////        registry.add("keycloak.urls.auth", () -> keycloak.getAuthServerUrl());
////    }
//
//    @Test
//    public void testKeycloak() {
//        // Use Keycloak instance (e.g., access Admin Console URL)
//    }
//
//}
