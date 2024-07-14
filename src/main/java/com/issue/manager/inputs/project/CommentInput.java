package com.issue.manager.inputs.project;

import com.issue.manager.inputs.ModelInput;
import com.issue.manager.models.project.Comment;
import com.issue.manager.models.project.CommentType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentInput extends ModelInput<Comment> {

    private String creator;
    private String reference;
    private CommentType commentType;
    private String description;
    private boolean edited;

    @Override
    public Comment toModel(Comment model) {
        model.setCreator(creator);
        model.setReference(reference);
        model.setCommentType(commentType);
        model.setDescription(description);
        model.setEdited(edited);

        return model;
    }
}
