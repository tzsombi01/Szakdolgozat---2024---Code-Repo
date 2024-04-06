package com.issue.manager.controllers.project;

import com.issue.manager.controllers.BaseCrudController;
import com.issue.manager.inputs.project.Documentation;
import com.issue.manager.inputs.project.DocumentationInput;
import com.issue.manager.repositories.EntityRepository;
import org.springframework.stereotype.Controller;

@Controller
public class DocumentationController extends BaseCrudController<DocumentationInput, Documentation> {

    public DocumentationController(EntityRepository<Documentation> repository) {
        super(repository);
    }

    @Override
    protected Class<DocumentationInput> getInputClass() {
        return DocumentationInput.class;
    }

    @Override
    protected Class<Documentation> getModelClass() {
        return Documentation.class;
    }
}