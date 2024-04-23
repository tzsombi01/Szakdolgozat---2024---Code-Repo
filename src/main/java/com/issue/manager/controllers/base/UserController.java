package com.issue.manager.controllers.base;

import com.issue.manager.inputs.dtos.UserResponseDTO;
import com.issue.manager.services.base.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    @ResponseStatus(HttpStatus.OK)
    public UserResponseDTO me() {
        return userService.getMe();
    }
}
