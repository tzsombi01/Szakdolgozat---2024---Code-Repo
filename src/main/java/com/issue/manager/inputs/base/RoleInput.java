package com.issue.manager.inputs.base;

import com.issue.manager.inputs.ModelInput;
import com.issue.manager.models.base.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoleInput extends ModelInput<Role> {

    private String role;
    private String project; // Project reference

    public Role toModel(Role model) {
        model.setRole(role);
        model.setProject(project);

        return model;
    }
}
