package com.sky.challenge.repository;

import com.sky.challenge.entity.User;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepositoryInterface extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);
}
