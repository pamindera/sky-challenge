package com.sky.challenge.integration;

import com.sky.challenge.dto.response.UserResponseDTO;
import com.sky.challenge.entity.User;
import com.sky.challenge.error.ErrorResponse;
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
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Import(TestConfig.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UpdateUserTest {

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
        testUser = this.testingFixtures.createUser(Map.of("email", "patch_user@mindera.com"));
        otherUser = this.testingFixtures.createUser(Map.of("email", "other_patch_user@mindera.com"));
    }


    @Test
    @DisplayName("Should fail - No auth")
    public void executeFailNoAuth() {
        given()
                .contentType(ContentType.JSON)
                .body(Map.of("name", "New Name"))
                .when()
                .patch("/api/v1/user/{id}", testUser.getId())
                .then()
                .statusCode(401);
    }

    @Test
    @DisplayName("Should fail - blank name")
    public void executeErrorBlankName() {
        String token = this.testingFixtures.login(testUser);

        var body = Map.of(
                "name", ""
        );

        ErrorResponse response = given()
                .contentType(ContentType.JSON)
                .auth()
                .oauth2(token)
                .body(body)
                .when()
                .patch("/api/v1/user/{id}", testUser.getId())
                .then()
                .statusCode(400)
                .extract()
                .as(ErrorResponse.class);

        assertTrue(response.message().contains("must not be blank"));
    }

    @Test
    @DisplayName("Should fail - pre authorize access denied")
    public void executeErrorPreAuth() {
        String token = this.testingFixtures.login(testUser);

            given()
                .contentType(ContentType.JSON)
                .auth()
                .oauth2(token)
                .body(Map.of("name", "New Name"))
                .when().patch("/api/v1/user/{id}", otherUser.getId())
                .then()
                .statusCode(401);
    }


    @Test
    @DisplayName("Should succeed - update user successfully")
    public void executeSuccess() {
        String token = this.testingFixtures.login(testUser);
        Map<String,String> body = Map.of("name", "New Name");

        UserResponseDTO response = given()
                .contentType(ContentType.JSON)
                .auth()
                .oauth2(token)
                .body(body)
                .when()
                .patch("/api/v1/user/{id}", testUser.getId())
                .then()
                .statusCode(200)
                .extract()
                .as(UserResponseDTO.class);

        assertNotNull(response.getId());
        assertEquals(body.get("name"), response.getName());
        assertNotNull(response.getEmail());
        assertNotNull(response.getCreatedAt());
        assertNotNull(response.getUpdatedAt());

        Optional<User> entity = this.testingFixtures.getUserRepository().findById(UUID.fromString(response.getId()));
        assertTrue(entity.isPresent());
        assertEquals(body.get("name"), entity.get().getName());
    }

}
