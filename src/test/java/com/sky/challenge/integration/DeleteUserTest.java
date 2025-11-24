package com.sky.challenge.integration;

import com.sky.challenge.dto.response.UserResponseDTO;
import com.sky.challenge.entity.User;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import utils.TestConfig;
import utils.TestingFixtures;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Import(TestConfig.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DeleteUserTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestingFixtures testingFixtures;

    private User testUser;
    private User otherUser;


    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @BeforeAll void setUpAll() {
        testUser = this.testingFixtures.createUser(Map.of("email", "delete_user@mindera.com"));
        otherUser = this.testingFixtures.createUser(Map.of("email", "other_delete_user@mindera.com"));
    }


    @Test
    @DisplayName("Should fail - No auth")
    public void executeFailNoAuth() {
        given()
                .contentType(ContentType.JSON)
                .when()
                .delete("/api/v1/user/{id}", testUser.getId())
                .then()
                .statusCode(401);
    }

    @Test
    @DisplayName("Should fail - pre authorize access denied")
    public void executeErrorPreAuth() {
        String token = this.testingFixtures.login(testUser);

            given()
                .contentType(ContentType.JSON)
                .auth()
                .oauth2(token)
                .when().delete("/api/v1/user/{id}", otherUser.getId())
                .then()
                .statusCode(401);
    }


    @Test
    @DisplayName("Should succeed - delete user successfully")
    public void executeSuccess() {
        String token = this.testingFixtures.login(testUser);

       given()
                .contentType(ContentType.JSON)
                .auth()
                .oauth2(token)
                .when()
                .delete("/api/v1/user/{id}", testUser.getId())
                .then()
                .statusCode(204);

        Optional<User> entity = this.testingFixtures.getUserRepository().findById(testUser.getId());
        assertTrue(entity.isEmpty());
    }

}
