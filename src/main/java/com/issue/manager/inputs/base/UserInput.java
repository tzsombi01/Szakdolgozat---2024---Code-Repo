package com.issue.manager.inputs.base;

import com.issue.manager.inputs.ModelInput;
import com.issue.manager.models.base.User;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserInput extends ModelInput<User> {

    private String userName;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private List<String> gitUserNames;
    private boolean active = true;
    private boolean locked;

    @Override
    public User toModel(User model) {
        model.setUserName(this.userName);
        model.setFirstName(this.firstName);
        model.setLastName(this.lastName);
        model.setEmail(this.email);
        model.setPassword(this.password);
        model.setGitUserNames(this.gitUserNames);
        model.setActive(this.active);
        model.setLocked(this.locked);

        System.out.println(model.getPassword());
        return model;
    }
}
