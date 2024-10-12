package com.issue.manager.services.base;

import com.issue.manager.inputs.dtos.InviteUsersRequest;
import com.issue.manager.inputs.dtos.UserResponseDTO;
import com.issue.manager.models.base.User;
import com.issue.manager.models.constants.MessageConstants;
import com.issue.manager.models.core.Filter;
import com.issue.manager.models.core.QueryOptions;
import com.issue.manager.models.project.*;
import com.issue.manager.repositories.base.UserRepository;
import com.issue.manager.repositories.project.InviteEventRepository;
import com.issue.manager.repositories.project.ProjectRepository;
import com.issue.manager.utils.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Log4j2
@RequiredArgsConstructor
public class UserService {

    @Value("${frontend.base.url}")
    private String frontendBaseUrl;

    private final UserRepository userRepository;
    private final InviteEventRepository inviteEventRepository;
    private final ProjectRepository projectRepository;
    private final EmailService emailService;

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
        for (String email : inviteUsersRequest.getEmails()) {
            Optional<User> optionalUser = userRepository.findByEmail(email);

            if (optionalUser.isEmpty()) {
                try {
                    Map<String, Object> properties = new HashMap<>();
                    Project project = projectRepository.findById(inviteUsersRequest.getProjectId()).orElseThrow(() -> new RuntimeException("Project not found with id: " + inviteUsersRequest.getProjectId()));
                    properties.put("projectName", project.getName());
                    properties.put("link", frontendBaseUrl + "/register");
                    emailService.sendEmail(email, EmailType.NEW_USER_INVITED_TO_PROJECT, properties);

                    InviteEvent inviteEvent = new InviteEvent(
                            email,
                            inviteUsersRequest.getProjectId()
                    );

                    inviteEventRepository.save(inviteEvent);
                    continue;
                } catch (Exception e) {
                    log.error("Email could not be sent to address: " + email);
                }
            }

            User user = optionalUser.get();

            Notification notification = new Notification(
                    user.getId(),
                    NotificationType.ACCEPT,
                    "project-invites",
                    MessageConstants.INVITED_TO_PROJECT_NOTIFICATION_NAME,
                    MessageConstants.INVITED_TO_PROJECT_MESSAGE,
                    false
            );
        }
    }
}
