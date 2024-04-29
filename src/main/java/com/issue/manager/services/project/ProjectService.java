package com.issue.manager.services.project;

import com.issue.manager.inputs.project.ProjectInput;
import com.issue.manager.models.project.Project;
import com.issue.manager.repositories.project.ProjectRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Log4j2
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;

    public Page<Project> getProjects() {
        List<Project> all = projectRepository.findAll();

        return new PageImpl<>(all);
    }

    public Project getProject(String id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project was not found by id " + id));

        return null;
    }

    public Project createProject(ProjectInput projectInput) {
        Project project = projectInput.toModel();

        return project;
    }

    public Project editProject(String id, ProjectInput projectInput) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project was not found by id " + id));

        Project editedProject = projectInput.toModel(project);

        return project;
    }

    public Project deleteProject(String id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project was not found by id " + id));

        projectRepository.deleteById(id);

        return null;
    }
}
