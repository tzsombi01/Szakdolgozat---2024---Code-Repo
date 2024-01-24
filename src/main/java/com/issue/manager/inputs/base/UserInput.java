package com.issue.manager.inputs.base;

import com.issue.manager.inputs.ModelInput;
import com.issue.manager.models.base.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserInput extends ModelInput<User> {

    private String userName;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private boolean active = true;
    private boolean locked;

    @Override
    public User toModel(User model) {
        super.toModel();
        model.setUserName(this.userName);
        model.setFirstName(this.firstName);
        model.setLastName(this.lastName);
        model.setEmail(this.email);
        model.setPassword(this.password);
        model.setActive(this.active);
        model.setLocked(this.locked);

        return model;
    }
}
