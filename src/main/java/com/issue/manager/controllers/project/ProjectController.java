package com.issue.manager.controllers.project;

import com.issue.manager.inputs.project.ProjectInput;
import com.issue.manager.models.core.QueryOptions;
import com.issue.manager.models.project.Project;
import com.issue.manager.services.project.ProjectService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<Project> getProjects(@RequestBody QueryOptions queryOptions) {
        return projectService.getProjects();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Project getProject(@PathVariable String id) {
        return projectService.getProject(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Project createProject(@RequestBody ProjectInput projectInput) {
        return projectService.createProject(projectInput);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Project editProject(@PathVariable String id, @RequestBody ProjectInput projectInput) {
        return projectService.editProject(id, projectInput);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Project deleteProject(@PathVariable String id) {
        return projectService.deleteProject(id);
    }
}
