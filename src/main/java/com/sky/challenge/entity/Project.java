package com.sky.challenge.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "projects")
@Getter
@Setter
@NoArgsConstructor
public class Project extends BaseEntity
{
    @ManyToOne
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "fkey_projects_on_users_id"), nullable = false)
    private User user;

    @Column(nullable = false)
    private String name;

    public Project(String name) {
        this.name = name;
    }
}