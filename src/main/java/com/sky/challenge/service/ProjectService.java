package com.sky.challenge.service;

import static org.springframework.beans.support.PagedListHolder.DEFAULT_PAGE_SIZE;

import com.sky.challenge.dto.request.CreateProjectRequestDTO;
import com.sky.challenge.dto.request.ListRequestDTO;
import com.sky.challenge.dto.response.ListResponseDTO;
import com.sky.challenge.dto.response.ProjectResponseDTO;
import com.sky.challenge.entity.Project;
import com.sky.challenge.entity.User;
import com.sky.challenge.repository.ProjectRepositoryInterface;
import com.sky.challenge.repository.UserRepositoryInterface;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class ProjectService extends BaseService {

    private final ProjectRepositoryInterface projectRepository;

    public ProjectService(UserRepositoryInterface userRepository, ProjectRepositoryInterface projectRepository) {
        super(userRepository);
        this.projectRepository = projectRepository;
    }

    public ProjectResponseDTO create(UUID id, CreateProjectRequestDTO request) {
        User user = checkIfUserExists(id);

        Project project = new Project(request.getName());
        user.addProject(project);

        projectRepository.save(project);

        return new ProjectResponseDTO(project);
    }

    public ListResponseDTO<Project, ProjectResponseDTO> list(UUID id, ListRequestDTO request) {
        User user = checkIfUserExists(id);

        Pageable pageable = PageRequest.of(
                request.getPage(), DEFAULT_PAGE_SIZE, Sort.by("createdAt").descending());

        Page<Project> result = projectRepository.findAllByUserOrderByCreatedAtDesc(user, pageable);

        return new ListResponseDTO<Project, ProjectResponseDTO>(
                result.getTotalElements(), pageable.getPageNumber(), result.getContent(), ProjectResponseDTO::new);
    }
}
