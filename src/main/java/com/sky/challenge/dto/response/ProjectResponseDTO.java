package com.sky.challenge.dto.response;

import com.sky.challenge.entity.Project;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(force = true)
public class ProjectResponseDTO extends BaseResponseDTO {

    private final String name;

    public ProjectResponseDTO(Project project) {
        super(project);
        this.name = project.getName();
    }
}
