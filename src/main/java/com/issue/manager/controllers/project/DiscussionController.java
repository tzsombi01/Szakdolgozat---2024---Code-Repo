package com.issue.manager.controllers.project;

import com.issue.manager.controllers.BaseCrudController;
import com.issue.manager.inputs.project.DiscussionInput;
import com.issue.manager.models.project.Discussion;
import com.issue.manager.repositories.EntityRepository;
import org.springframework.stereotype.Controller;

@Controller
public class DiscussionController extends BaseCrudController<DiscussionInput, Discussion> {

    public DiscussionController(EntityRepository<Discussion> repository) {
        super(repository);
    }

    @Override
    protected Class<DiscussionInput> getInputClass() {
        return DiscussionInput.class;
    }

    @Override
    protected Class<Discussion> getModelClass() {
        return Discussion.class;
    }
}
