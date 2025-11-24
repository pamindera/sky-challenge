package utils;

import com.sky.challenge.entity.Project;
import com.sky.challenge.entity.User;
import com.sky.challenge.repository.ProjectRepositoryInterface;
import com.sky.challenge.repository.UserRepositoryInterface;
import com.sky.challenge.security.JWTService;
import jakarta.persistence.EntityManager;
import java.util.Map;
import lombok.Getter;
import net.datafaker.Faker;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Getter
@Service
public class TestingFixtures {

    private final Faker faker;
    private final ProjectRepositoryInterface projectRepository;
    private final UserRepositoryInterface userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;
    private final EntityManager entityManager;

    public TestingFixtures(
            ProjectRepositoryInterface projectRepository,
            UserRepositoryInterface userRepository,
            PasswordEncoder passwordEncoder,
            JWTService jwtService,
            EntityManager entityManager) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.entityManager = entityManager;
        this.faker = new Faker();
    }

    public User createUser(Map<String, String> data) {
        User entity = new User(
                data.getOrDefault("email", faker.internet().emailAddress()),
                passwordEncoder.encode(
                        data.getOrDefault("password", faker.internet().password(8, 16, true, true, true))),
                data.getOrDefault("name", faker.name().fullName()));

        return this.userRepository.save(entity);
    }

    public String login(User user) {
        return jwtService.generateToken(user);
    }

    public Project createProject(Map<String, Object> data) {
        Project entity =
                new Project((String) data.getOrDefault("name", faker.random().hex(10)));

        if (data.containsKey("user")) {
            User user = (User) data.get("user");
            user.addProject(entity);
        }

        return this.projectRepository.save(entity);
    }
}
