package com.issue.manager.services.base;

import com.issue.manager.inputs.dtos.UserResponseDTO;
import com.issue.manager.models.base.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@Log4j2
@RequiredArgsConstructor
public class UserService {

    public UserResponseDTO getMe() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return new UserResponseDTO(
                user.getUsername(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getGitUserNames()
        );
    }
}
