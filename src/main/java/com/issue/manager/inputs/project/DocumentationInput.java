package com.issue.manager.inputs.project;

import com.issue.manager.inputs.ModelInput;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DocumentationInput extends ModelInput<Documentation> {

    private String creator; // Reference to User
    private List<String> comments;
    private String description;

    @Override
    public Documentation toModel(Documentation model) {
        model.setCreator(creator);
        model.setDescription(description);
        model.setComments(comments);

        return model;
    }
}