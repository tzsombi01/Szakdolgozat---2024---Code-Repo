package com.issue.manager.controllers.project;

import com.issue.manager.controllers.BaseCrudController;
import com.issue.manager.inputs.project.CommentInput;
import com.issue.manager.models.project.Comment;
import com.issue.manager.repositories.EntityRepository;
import org.springframework.stereotype.Controller;

@Controller
public class CommentController extends BaseCrudController<CommentInput, Comment> {

    public CommentController(EntityRepository<Comment> repository) {
        super(repository);
    }

    @Override
    protected Class<CommentInput> getInputClass() {
        return CommentInput.class;
    }

    @Override
    protected Class<Comment> getModelClass() {
        return Comment.class;
    }
}
