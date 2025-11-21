package com.sky.challenge.repository;

import com.sky.challenge.entity.Project;
import com.sky.challenge.entity.User;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepositoryInterface extends JpaRepository<Project, UUID> {
    Page<Project> findAllByUserOrderByCreatedAtDesc(User user, Pageable pageable);
}
