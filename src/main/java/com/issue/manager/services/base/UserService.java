package com.issue.manager.services.base;

import com.issue.manager.inputs.dtos.InviteUsersRequest;
import com.issue.manager.inputs.dtos.UserResponseDTO;
import com.issue.manager.models.base.User;
import com.issue.manager.models.core.Filter;
import com.issue.manager.models.core.QueryOptions;
import com.issue.manager.repositories.base.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Log4j2
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserResponseDTO getMe() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return new UserResponseDTO(
                user.getId(),
                user.getUsernameUserName(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getGitUserNames()
        );
    }

    public List<UserResponseDTO> getUsers(QueryOptions queryOptions) {
        List<User> allUsers = new ArrayList<>();

        for (Filter filter : queryOptions.getFilters()) {
            if ("id".equals(filter.getField())) {
                List<String> ids = (List<String>) filter.getValue();

                allUsers.addAll(userRepository.findAllById(ids));
            }
        }

        return allUsers.stream().map(user -> new UserResponseDTO(
                user.getId(),
                user.getUsernameUserName(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getGitUserNames()
        )).toList();
    }

    public void setAccessToken(String accessToken) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        user.setAccessToken(accessToken);

        userRepository.save(user);
    }

    public void inviteUsersToProject(InviteUsersRequest inviteUsersRequest) {
    }
}
