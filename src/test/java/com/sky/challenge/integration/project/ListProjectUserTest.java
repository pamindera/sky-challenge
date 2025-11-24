package com.sky.challenge.integration.project;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.beans.support.PagedListHolder.DEFAULT_PAGE_SIZE;

import com.sky.challenge.dto.response.ListResponseDTO;
import com.sky.challenge.dto.response.ProjectResponseDTO;
import com.sky.challenge.entity.Project;
import com.sky.challenge.entity.User;
import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import java.time.Instant;
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
public class ListProjectUserTest {

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
        testUser = this.testingFixtures.createUser(Map.of("email", "list_user_project@sky.com"));
        otherUser = this.testingFixtures.createUser(Map.of("email", "other_list_user_project@sky.com"));

        for (int i = 0; i < 15; i++) {
            this.testingFixtures.createProject(Map.of("user", testUser));
        }
    }

    @Test
    @DisplayName("Should fail - No auth")
    public void executeFailNoAuth() {
        given().contentType(ContentType.JSON)
                .when()
                .get("/api/v1/user/{id}/project", testUser.getId())
                .then()
                .statusCode(401);
    }

    @Test
    @DisplayName("Should fail - pre authorize access denied")
    public void executeErrorPreAuth() {
        String token = this.testingFixtures.login(testUser);

        given().contentType(ContentType.JSON)
                .auth()
                .oauth2(token)
                .when()
                .get("/api/v1/user/{id}/project", otherUser.getId())
                .then()
                .statusCode(401);
    }

    @Test
    @DisplayName("Should succeed - list users projects successfully")
    public void executeSuccess() {
        String token = this.testingFixtures.login(testUser);

        ListResponseDTO<Project, ProjectResponseDTO> response = given().contentType(ContentType.JSON)
                .auth()
                .oauth2(token)
                .when()
                .get("/api/v1/user/{id}/project", testUser.getId())
                .then()
                .statusCode(200)
                .extract()
                .as(new TypeRef<ListResponseDTO<Project, ProjectResponseDTO>>() {});

        assertEquals(15, response.count());
        assertEquals(DEFAULT_PAGE_SIZE, response.items().size());
        assertEquals(0, response.page());

        for (ProjectResponseDTO dto : response.items()) {
            assertNotNull(dto.getId());
            assertNotNull(dto.getName());
            assertNotNull(dto.getCreatedAt());
            assertNotNull(dto.getUpdatedAt());
        }

        for (int i = 0; i < response.items().size() - 1; i++) {
            ProjectResponseDTO current = response.items().get(i);
            ProjectResponseDTO next = response.items().get(i + 1);

            Instant currentDate = Instant.parse(current.getCreatedAt());
            Instant nextDate = Instant.parse(next.getCreatedAt());

            assertTrue(currentDate.isAfter(nextDate) || currentDate.equals(nextDate));
        }
    }

    @Test
    @DisplayName("Should succeed - list users projects successfully with page 0")
    public void executeSuccessWithPage0() {
        String token = this.testingFixtures.login(testUser);

        ListResponseDTO<Project, ProjectResponseDTO> response = given().contentType(ContentType.JSON)
                .auth()
                .oauth2(token)
                .when()
                .get("/api/v1/user/{id}/project?page=0", testUser.getId())
                .then()
                .statusCode(200)
                .extract()
                .as(new TypeRef<ListResponseDTO<Project, ProjectResponseDTO>>() {});

        assertEquals(15, response.count());
        assertEquals(DEFAULT_PAGE_SIZE, response.items().size());
        assertEquals(0, response.page());
    }

    @Test
    @DisplayName("Should succeed - list users projects successfully with page 1")
    public void executeSuccessWithPage1() {
        String token = this.testingFixtures.login(testUser);

        ListResponseDTO<Project, ProjectResponseDTO> response = given().contentType(ContentType.JSON)
                .auth()
                .oauth2(token)
                .when()
                .get("/api/v1/user/{id}/project?page=1", testUser.getId())
                .then()
                .statusCode(200)
                .extract()
                .as(new TypeRef<ListResponseDTO<Project, ProjectResponseDTO>>() {});

        assertEquals(15, response.count());
        assertEquals(5, response.items().size());
        assertEquals(1, response.page());
    }

    @Test
    @DisplayName("Should succeed - list users projects successfully with page 2")
    public void executeSuccessWithPage2() {
        String token = this.testingFixtures.login(testUser);

        ListResponseDTO<Project, ProjectResponseDTO> response = given().contentType(ContentType.JSON)
                .auth()
                .oauth2(token)
                .when()
                .get("/api/v1/user/{id}/project?page=2", testUser.getId())
                .then()
                .statusCode(200)
                .extract()
                .as(new TypeRef<ListResponseDTO<Project, ProjectResponseDTO>>() {});

        assertEquals(15, response.count());
        assertEquals(0, response.items().size());
        assertEquals(2, response.page());
    }
}
