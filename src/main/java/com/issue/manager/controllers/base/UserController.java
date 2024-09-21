package com.issue.manager.controllers.base;

import com.issue.manager.inputs.dtos.InviteUsersRequest;
import com.issue.manager.inputs.dtos.UserResponseDTO;
import com.issue.manager.models.core.QueryOptions;
import com.issue.manager.services.base.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public List<UserResponseDTO> getUsers(@RequestBody QueryOptions queryOptions) {
        return userService.getUsers(queryOptions);
    }

    @PostMapping("/access-token")
    @ResponseStatus(HttpStatus.OK)
    public void setAccessToken(@RequestBody String accessToken) {
        userService.setAccessToken(accessToken);
    }

    @PostMapping("/invite")
    @ResponseStatus(HttpStatus.OK)
    public void inviteUsersToProject(@RequestBody InviteUsersRequest inviteUsersRequest) {
        userService.inviteUsersToProject(inviteUsersRequest);
    }
}
