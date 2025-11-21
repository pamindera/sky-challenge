package com.sky.challenge.integration;

import static io.restassured.RestAssured.*;
import static org.junit.jupiter.api.Assertions.*;

import com.sky.challenge.dto.response.UserResponseDTO;
import com.sky.challenge.repository.UserRepositoryInterface;
import io.restassured.RestAssured;
import io.restassured.RestAssured.*;
import io.restassured.http.ContentType;
import io.restassured.matcher.RestAssuredMatchers.*;
import java.util.Arrays;
import java.util.Map;
import org.hamcrest.Matchers.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class CreateUserTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setup() {
        RestAssured.port = port;
    }

    @Autowired
    private Environment env;

    @Autowired
    private UserRepositoryInterface userRepository;

    @Test
    @DisplayName("Should succeed - user created successfully")
    public void executeCreateSuccess() {

        System.out.println("db" + this.userRepository.findByEmail("test@mindera.com"));

        System.out.println("Profile is: +" + Arrays.toString(env.getActiveProfiles()));
        var body = Map.of(
                "email", "test@mindera.com",
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

        System.out.println("db" + this.userRepository.findByEmail("test@mindera.com"));
        //        assertEquals(body.get("name"), response.getName());
        //        assertEquals(body.get("location"), response.getLocation());
        //        assertEquals(body.get("specifier"), response.getSpecifier());
        //        assertEquals(body.get("type"), response.getType());
        //        assertEquals(body.get("estimatedDeliveryDate"), response.getEstimatedDeliveryDate());
        //        assertEquals(ProjectStatus.DRAFT.toString(), response.getStatus());
        //        assertNotNull(response.getCreatedAt());
        //        assertNotNull(response.getUpdatedAt());
        //
        //        Project entity = this.fixtures.getProjectRepository().findById(response.getId());
        //        assertNotNull(entity);
        //        assertEquals(1, entity.getMembers().toArray().length);
    }
}
