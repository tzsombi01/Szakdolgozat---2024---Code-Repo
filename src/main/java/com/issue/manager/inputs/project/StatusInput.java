package com.issue.manager.inputs.project;

import com.issue.manager.inputs.ModelInput;
import com.issue.manager.models.project.Status;
import com.issue.manager.models.project.StatusType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StatusInput extends ModelInput<Status>  {

    private String name;
    private StatusType type;
    private String project;

    @Override
    public Status toModel(Status model) {
        model.setName(name);
        model.setType(type);
        model.setProject(project);

        return model;
    }
}
