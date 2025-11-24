package utils;

import com.sky.challenge.repository.ProjectRepositoryInterface;
import com.sky.challenge.repository.UserRepositoryInterface;
import com.sky.challenge.security.JWTService;
import jakarta.persistence.EntityManager;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@TestConfiguration
public class TestConfig {

    @Bean
    public TestingFixtures testFixturesService(
            ProjectRepositoryInterface projectRepository,
            UserRepositoryInterface userRepository,
            PasswordEncoder passwordEncoder,
            JWTService jwtService,
            EntityManager entityManager) {
        return new TestingFixtures(projectRepository, userRepository, passwordEncoder, jwtService, entityManager);
    }
}
