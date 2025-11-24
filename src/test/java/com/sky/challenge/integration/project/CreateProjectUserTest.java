package com.sky.challenge.integration.project;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

import com.sky.challenge.dto.response.ProjectResponseDTO;
import com.sky.challenge.entity.Project;
import com.sky.challenge.entity.User;
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
public class CreateProjectUserTest {

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

    @BeforeAll
    void setUpAll() {
        testUser = this.testingFixtures.createUser(Map.of("email", "add_project_user@sky.com"));
        otherUser = this.testingFixtures.createUser(Map.of("email", "other_add_project_user@sky.com"));
    }

    @Test
    @DisplayName("Should fail - No auth")
    public void executeFailNoAuth() {
        given().contentType(ContentType.JSON)
                .body(Map.of("name", "New Name"))
                .when()
                .post("/api/v1/user/{id}/project", testUser.getId())
                .then()
                .statusCode(401);
    }

    @Test
    @DisplayName("Should fail - blank project name")
    public void executeErrorBlankName() {
        String token = this.testingFixtures.login(testUser);

        var body = Map.of("name", "");

        ErrorResponse response = given().contentType(ContentType.JSON)
                .auth()
                .oauth2(token)
                .body(body)
                .when()
                .post("/api/v1/user/{id}/project", testUser.getId())
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

        given().contentType(ContentType.JSON)
                .auth()
                .oauth2(token)
                .body(Map.of("name", "New project Name"))
                .when()
                .post("/api/v1/user/{id}/project", otherUser.getId())
                .then()
                .statusCode(401);
    }

    @Test
    @DisplayName("Should succeed -  user created a project successfully")
    public void executeSuccess() {
        String token = this.testingFixtures.login(testUser);
        Map<String, String> body = Map.of("name", "New Name");

        ProjectResponseDTO response = given().contentType(ContentType.JSON)
                .auth()
                .oauth2(token)
                .body(body)
                .when()
                .post("/api/v1/user/{id}/project", testUser.getId())
                .then()
                .statusCode(201)
                .extract()
                .as(ProjectResponseDTO.class);

        assertNotNull(response.getId());
        assertEquals(body.get("name"), response.getName());
        assertNotNull(response.getCreatedAt());
        assertNotNull(response.getUpdatedAt());

        Optional<Project> entity =
                this.testingFixtures.getProjectRepository().findById(UUID.fromString(response.getId()));
        assertTrue(entity.isPresent());
        assertEquals(body.get("name"), entity.get().getName());
    }
}
