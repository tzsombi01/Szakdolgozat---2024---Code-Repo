package com.issue.manager.inputs.project;

import com.issue.manager.inputs.ModelInput;
import com.issue.manager.models.project.Project;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProjectInput extends ModelInput<Project> {

    private String url;
    private String name;
    private String gitHubUserName;
    private List<String> tickets; // Ticket references
    private List<String> users; // User references

    public Project toModel(Project model) {
        model.setUrl(url);
        model.setName(name);
        model.setGitHubUserName(gitHubUserName);
        model.setTickets(tickets);
        model.setUsers(users);

        return model;
    }
}
