package com.issue.manager.inputs.project;

import com.issue.manager.inputs.ModelInput;
import com.issue.manager.models.project.Invite;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InviteInput extends ModelInput<Invite> {

    private String user;

    private String project;

    public Invite toModel(Invite model) {
        model.setUser(user);
        model.setProject(project);

        return model;
    }
}
