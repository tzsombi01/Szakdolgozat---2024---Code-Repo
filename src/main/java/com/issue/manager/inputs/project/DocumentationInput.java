package com.issue.manager.inputs.project;

import com.issue.manager.inputs.ModelInput;
import com.issue.manager.models.project.Documentation;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DocumentationInput extends ModelInput<Documentation> {

    private String creator; // Reference to User
    private String name;
    private String project;
    private List<String> comments;
    private String description;

    @Override
    public Documentation toModel(Documentation model) {
        model.setCreator(creator);
        model.setName(name);
        model.setProject(project);
        model.setDescription(description);
        model.setComments(comments);

        return model;
    }
}