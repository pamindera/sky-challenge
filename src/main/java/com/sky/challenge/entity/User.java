package com.sky.challenge.entity;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;
import lombok.*;

@Entity
@Table(name = "users", uniqueConstraints = @UniqueConstraint(columnNames = "email", name = "ukey_users_email"))
@Getter
@NoArgsConstructor
public class User extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = true)
    private String name;

    @OneToMany(
            mappedBy = "user",
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private Set<Project> projects;

    public User(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.projects = new HashSet<>();
    }

    public User addProject(Project project) {
        this.projects.add(project);
        project.setUser(this);

        return this;
    }

    public void updateDetails(String name) {
        this.name = name;
    }
}
