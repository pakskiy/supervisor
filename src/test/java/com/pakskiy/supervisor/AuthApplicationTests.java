package com.pakskiy.supervisor;

import dasniko.testcontainers.keycloak.KeycloakContainer;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.server.ServerWebExchange;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Testcontainers
@RequiredArgsConstructor
@AutoConfigureWebTestClient
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AuthApplicationTests {

    private static final DockerImageName dockerImageName = DockerImageName.parse("postgres:16.2.0");
    @Autowired
    private WebTestClient webClient;

    private static final String KEYCLOAK_CLIENT_ID = "app-auth-client-id";
    private static final String KEYCLOAK_CLIENT_SECRET = "FuXDordwvRlgaCasQ1xsqi6s9pkAuQPI";

    static final KeycloakContainer keycloak;

    @Container
    static PostgreSQLContainer<?> postgresqlContainer = (PostgreSQLContainer) new PostgreSQLContainer(dockerImageName)
            .withDatabaseName("paymentProvider")
            .withUsername("postgres")
            .withPassword("123456");

    static {
        keycloak = new KeycloakContainer("quay.io/keycloak/keycloak:24.0.4")
                .withRealmImportFile("realm-export-test.json")
                .withEnv("DB_VENDOR", "h2")
                .withEnv("DB_URL", "jdbc:h2:mem:testdb")
                .withEnv("DB_USER", "sa")
                .withEnv("DB_PASSWORD", "");
        keycloak.start();
    }

    @BeforeAll
    static void beforeAll() {
        postgresqlContainer.start();
        System.setProperty("spring.r2dbc.url", "r2dbc:postgresql://localhost:" + postgresqlContainer.getFirstMappedPort() + "/paymentProvider");
        System.setProperty("spring.flyway.url", "jdbc:postgresql://localhost:" + postgresqlContainer.getFirstMappedPort() + "/paymentProvider");
//        merchantRequestDto.setLogin(login);
//        merchantRequestDto.setKey(key);
//        exchange = Mockito.mock(ServerWebExchange.class);
//        when(exchange.getAttribute("merchantId")).thenReturn(1L);
//        when(exchange.getAttribute("accountId")).thenReturn(1L);



    }

    @Test
    void contextLoads() {

    }

}
