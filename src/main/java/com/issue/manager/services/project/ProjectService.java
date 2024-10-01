package com.issue.manager.services.project;

import com.issue.manager.inputs.project.ProjectInput;
import com.issue.manager.models.base.User;
import com.issue.manager.models.core.Filter;
import com.issue.manager.models.core.QueryOptions;
import com.issue.manager.models.project.Discussion;
import com.issue.manager.models.project.Project;
import com.issue.manager.models.project.Status;
import com.issue.manager.models.project.StatusType;
import com.issue.manager.repositories.project.ProjectRepository;
import com.issue.manager.repositories.project.StatusRepository;
import com.issue.manager.utils.GitHubService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Log4j2
@RequiredArgsConstructor
public class ProjectService {

    private final GitHubService githubService;

    private final ProjectRepository projectRepository;
    private final StatusRepository statusRepository;

    public Page<Project> getProjects(QueryOptions queryOptions) {
        Project exampleProject = new Project();
        ExampleMatcher matcher = ExampleMatcher.matching();

        if (queryOptions.getFilters() != null) {
            for (Filter filter : queryOptions.getFilters()) {
                if ("name".equals(filter.getField())) {
                    exampleProject.setName((String) filter.getValue());
                    matcher = matcher.withMatcher("name", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase(true));
                }
            }
        }

        Example<Project> example = Example.of(exampleProject, matcher);

        Pageable pageable = PageRequest.of(queryOptions.getSkip(), queryOptions.getTake() > 0 ? queryOptions.getTake() : 10);

        return projectRepository.findAll(example, pageable);
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

        Project savedProject = projectRepository.save(project);

        addDefaultStatuses(savedProject.getId());

        return savedProject;
    }

    private List<Status> addDefaultStatuses(String id) {
        Status testReady = new Status();
        testReady.setProject(id);
        testReady.setName("test ready");
        testReady.setType(StatusType.DONE);

        Status workInProgress = new Status();
        workInProgress.setProject(id);
        workInProgress.setName("work in progress");
        workInProgress.setType(StatusType.WORKING_ON_IT);

        Status TBD = new Status();
        TBD.setProject(id);
        TBD.setName("TBD");
        TBD.setType(StatusType.TBD);

        Status bug = new Status();
        bug.setProject(id);
        bug.setName("bug");
        bug.setType(StatusType.CRITICAL);

        Status critical = new Status();
        critical.setProject(id);
        critical.setName("critical");
        critical.setType(StatusType.CRITICAL);

        return statusRepository.saveAll(List.of(testReady, workInProgress, TBD, bug, critical));
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
