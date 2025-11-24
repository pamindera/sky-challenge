package com.sky.challenge.integration.user;

import static io.restassured.RestAssured.*;
import static org.junit.jupiter.api.Assertions.*;

import com.sky.challenge.dto.response.UserResponseDTO;
import com.sky.challenge.entity.User;
import com.sky.challenge.error.ErrorMessage;
import com.sky.challenge.error.ErrorResponse;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import utils.TestConfig;
import utils.TestingFixtures;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Import(TestConfig.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CreateUserTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestingFixtures testingFixtures;

    @BeforeEach
    void setup() {
        RestAssured.port = port;
    }

    @Test
    @DisplayName("Should fail - blank email")
    public void executeErrorBlankEmail() {
        var body = Map.of(
                "email", "",
                "password", "dummy_@password!A1",
                "name", "a name");

        ErrorResponse response = given().contentType(ContentType.JSON)
                .body(body)
                .when()
                .post("/api/v1/user")
                .then()
                .statusCode(400)
                .extract()
                .as(ErrorResponse.class);

        assertTrue(response.message().contains("must not be blank"));
    }

    @Test
    @DisplayName("Should fail - invalid email format")
    public void executeErrorInvalidEmailFormat() {
        var body = Map.of(
                "email", "email",
                "password", "dummy_@password!A1",
                "name", "a name");

        ErrorResponse response = given().contentType(ContentType.JSON)
                .body(body)
                .when()
                .post("/api/v1/user")
                .then()
                .statusCode(400)
                .extract()
                .as(ErrorResponse.class);

        assertTrue(response.message().contains("must be a well-formed email address"));
    }

    @Test
    @DisplayName("Should fail - email exceeds 255 chars")
    public void executeErrorEmailTooLong() {
        var body = Map.of(
                "email", "a".repeat(256) + "@test.com",
                "password", "dummy_@password!A1",
                "name", "a name");

        ErrorResponse response = given().contentType(ContentType.JSON)
                .body(body)
                .when()
                .post("/api/v1/user")
                .then()
                .statusCode(400)
                .extract()
                .as(ErrorResponse.class);

        assertTrue(response.message().contains("size must be between"));
    }

    @Test
    @DisplayName("Should fail - blank password")
    public void executeErrorBlankPassword() {
        var body = Map.of(
                "email", "user@test.com",
                "password", "",
                "name", "a name");

        ErrorResponse response = given().contentType(ContentType.JSON)
                .body(body)
                .when()
                .post("/api/v1/user")
                .then()
                .statusCode(400)
                .extract()
                .as(ErrorResponse.class);

        assertTrue(response.message().contains("must not be blank"));
    }

    @Test
    @DisplayName("Should fail - password too short")
    public void executeErrorPasswordTooShort() {
        var body = Map.of(
                "email", "user@test.com",
                "password", "Aa1@",
                "name", "a name");

        ErrorResponse response = given().contentType(ContentType.JSON)
                .body(body)
                .when()
                .post("/api/v1/user")
                .then()
                .statusCode(400)
                .extract()
                .as(ErrorResponse.class);

        assertTrue(
                response.message()
                        .contains(
                                "default message [Password must be at least 8 chars long and include upper/lowercase, a number, and a special character"));
    }

    @Test
    @DisplayName("Should fail - password missing uppercase")
    public void executeErrorPasswordMissingUppercase() {
        var body = Map.of(
                "email", "user@test.com",
                "password", "validpass1@",
                "name", "a name");

        ErrorResponse response = given().contentType(ContentType.JSON)
                .body(body)
                .when()
                .post("/api/v1/user")
                .then()
                .statusCode(400)
                .extract()
                .as(ErrorResponse.class);

        assertTrue(
                response.message()
                        .contains(
                                "default message [Password must be at least 8 chars long and include upper/lowercase, a number, and a special character"));
    }

    @Test
    @DisplayName("Should fail - password exceeds 255 chars")
    public void executeErrorPasswordTooLong() {
        var body = Map.of(
                "email", "user@test.com",
                "password", "A".repeat(256) + "@mail.com",
                "name", "a name");

        ErrorResponse response = given().contentType(ContentType.JSON)
                .body(body)
                .when()
                .post("/api/v1/user")
                .then()
                .statusCode(400)
                .extract()
                .as(ErrorResponse.class);

        assertTrue(response.message().contains("size must be between"));
    }

    @Test
    @DisplayName("Should fail - name exceeds 255 chars")
    public void executeErrorNameTooLong() {
        var body = Map.of(
                "email", "user@test.com",
                "password", "dummy_@password!A1",
                "name", "N".repeat(256));

        ErrorResponse response = given().contentType(ContentType.JSON)
                .body(body)
                .when()
                .post("/api/v1/user")
                .then()
                .statusCode(400)
                .extract()
                .as(ErrorResponse.class);

        assertTrue(response.message().contains("size must be between"));
    }

    @Test
    @DisplayName("Should fail - user already exists")
    public void executeErrorUserAlreadyExists() {

        User user = this.testingFixtures.createUser(Map.of());
        var body = Map.of(
                "email", user.getEmail(),
                "password", "dummy_@password!A1",
                "name", user.getEmail());

        ErrorResponse response = given().contentType(ContentType.JSON)
                .body(body)
                .when()
                .post("/api/v1/user")
                .then()
                .statusCode(409)
                .extract()
                .as(ErrorResponse.class);

        assertEquals(ErrorMessage.USER_FOUND + user.getEmail(), response.message());
    }

    @Test
    @DisplayName("Should succeed - user created successfully")
    public void executeCreateSuccess() {
        var body = Map.of(
                "email", "create_with_name@sky.com",
                "password", "dummy_@password!A1",
                "name", "mindera");

        UserResponseDTO response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post("/api/v1/user")
                .then()
                .statusCode(201)
                .extract()
                .as(UserResponseDTO.class);

        assertNotNull(response.getId());
        assertEquals(body.get("name"), response.getName());
        assertEquals(body.get("email"), response.getEmail());
        assertNotNull(response.getCreatedAt());
        assertNotNull(response.getUpdatedAt());

        Optional<User> entity = this.testingFixtures.getUserRepository().findById(UUID.fromString(response.getId()));
        assertTrue(entity.isPresent());
    }

    @Test
    @DisplayName("Should succeed - user created successfully without name")
    public void executeCreateSuccess2() {
        var body = Map.of(
                "email", "create_without_name@sky.com",
                "password", "dummy_@password!A1");

        UserResponseDTO response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post("/api/v1/user")
                .then()
                .statusCode(201)
                .extract()
                .as(UserResponseDTO.class);

        assertNotNull(response.getId());
        assertEquals(body.get("name"), response.getName());
        assertEquals(body.get("email"), response.getEmail());
        assertNotNull(response.getCreatedAt());
        assertNotNull(response.getUpdatedAt());

        Optional<User> entity = this.testingFixtures.getUserRepository().findById(UUID.fromString(response.getId()));
        assertTrue(entity.isPresent());
    }
}
