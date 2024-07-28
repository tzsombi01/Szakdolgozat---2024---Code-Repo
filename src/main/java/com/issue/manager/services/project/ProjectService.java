package com.issue.manager.services.project;

import com.issue.manager.inputs.project.ProjectInput;
import com.issue.manager.models.base.User;
import com.issue.manager.models.core.Filter;
import com.issue.manager.models.core.QueryOptions;
import com.issue.manager.models.project.Project;
import com.issue.manager.repositories.project.ProjectRepository;
import com.issue.manager.utils.GitHubService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Log4j2
@RequiredArgsConstructor
public class ProjectService {

    private final GitHubService githubService;

    private final ProjectRepository projectRepository;

    public Page<Project> getProjects(QueryOptions queryOptions) {
        List<Project> all = projectRepository.findAll();

        return new PageImpl<>(all);
    }

    public Project getProject(String id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project was not found by id " + id));

        return project;
    }

    public Project createProject(ProjectInput projectInput) {
        Project project = projectInput.toModel();

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        project.addUser(user.getId());

        githubService.validatePublicRepository(project.getUrl());

        return projectRepository.save(project);
    }

    public Project editProject(String id, ProjectInput projectInput) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project was not found by id " + id));

        Project editedProject = projectInput.toModel(project);

        projectRepository.save(editedProject);

        return project;
    }

    public Project deleteProject(String id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project was not found by id " + id));

        projectRepository.deleteById(id);

        return project;
    }
}
