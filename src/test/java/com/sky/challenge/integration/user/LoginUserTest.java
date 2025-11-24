package com.sky.challenge.integration.user;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

import com.sky.challenge.dto.response.LoginResponseDTO;
import com.sky.challenge.entity.User;
import com.sky.challenge.error.ErrorMessage;
import com.sky.challenge.error.ErrorResponse;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.Map;
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
public class LoginUserTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestingFixtures testingFixtures;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    @DisplayName("Should fail - blank email")
    public void executeErrorBlankEmail() {
        var body = Map.of(
                "email", "",
                "password", "dummy_@password!A1");

        ErrorResponse response = given().contentType(ContentType.JSON)
                .body(body)
                .when()
                .post("/api/v1/login")
                .then()
                .statusCode(400)
                .extract()
                .as(ErrorResponse.class);

        assertTrue(response.message().contains("must not be blank"));
    }

    @Test
    @DisplayName("Should fail - blank password")
    public void executeErrorBlankPassword() {
        var body = Map.of(
                "email", "user@test.com",
                "password", "");

        ErrorResponse response = given().contentType(ContentType.JSON)
                .body(body)
                .when()
                .post("/api/v1/login")
                .then()
                .statusCode(400)
                .extract()
                .as(ErrorResponse.class);

        assertTrue(response.message().contains("must not be blank"));
    }

    @Test
    @DisplayName("Should fail - user not found")
    public void executeErrorUserAlreadyExists() {
        var body = Map.of(
                "email", "fake_user@sky.com",
                "password", "dummy_@password!A1");

        ErrorResponse response = given().contentType(ContentType.JSON)
                .body(body)
                .when()
                .post("/api/v1/login")
                .then()
                .statusCode(404)
                .extract()
                .as(ErrorResponse.class);

        assertEquals(ErrorMessage.INVALID_CREDENTIALS, response.message());
    }

    @Test
    @DisplayName("Should fail - invalid credentials")
    public void executeErrorUserBadCredentials() {
        String email = "login_invalid_credentials@sky.com";
        String password = "dummy_@password!A1";

        User user = this.testingFixtures.createUser(Map.of(
                "email", email,
                "password", password));

        var body = Map.of("email", user.getEmail(), "password", "wrong_password123!");

        ErrorResponse response = given().contentType(ContentType.JSON)
                .body(body)
                .when()
                .post("/api/v1/login")
                .then()
                .statusCode(404)
                .extract()
                .as(ErrorResponse.class);

        assertEquals(ErrorMessage.INVALID_CREDENTIALS, response.message());
    }

    @Test
    @DisplayName("Should succeed - user login")
    public void executeSuccess() {

        String email = "login_success@sky.com";
        String password = "dummy_@password!A1";

        User user = this.testingFixtures.createUser(Map.of("email", email, "password", password));

        var body = Map.of("email", user.getEmail(), "password", password);

        LoginResponseDTO response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post("/api/v1/login")
                .then()
                .statusCode(200)
                .extract()
                .as(LoginResponseDTO.class);

        assertNotNull(response.token());
    }
}
