package com.issue.manager.controllers.base;

import com.issue.manager.controllers.BaseCrudController;
import com.issue.manager.inputs.base.UserInput;
import com.issue.manager.models.base.User;
import com.issue.manager.repositories.EntityRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;

@Log4j2
@Controller
public class UserController extends BaseCrudController<UserInput, User> {


    public UserController(EntityRepository<User> repository) {
        super(repository);
    }

    @Override
    protected Class<UserInput> getInputClass() {
        return UserInput.class;
    }

    @Override
    protected Class<User> getModelClass() {
        return User.class;
    }
}
