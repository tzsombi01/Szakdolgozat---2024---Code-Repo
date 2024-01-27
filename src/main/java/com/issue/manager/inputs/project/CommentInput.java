package com.issue.manager.inputs.project;

import com.issue.manager.inputs.ModelInput;
import com.issue.manager.models.project.Comment;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentInput extends ModelInput<Comment> {

    private String creator;
    private String description;
    private boolean edited;

    @Override
    public Comment toModel(Comment model) {
        model.setCreator(creator);
        model.setDescription(description);
        model.setEdited(edited);

        return model;
    }
}
