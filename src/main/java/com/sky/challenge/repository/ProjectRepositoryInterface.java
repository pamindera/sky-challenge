package com.sky.challenge.repository;


import com.sky.challenge.entity.Project;
import com.sky.challenge.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProjectRepositoryInterface extends JpaRepository<Project, UUID> {

}
