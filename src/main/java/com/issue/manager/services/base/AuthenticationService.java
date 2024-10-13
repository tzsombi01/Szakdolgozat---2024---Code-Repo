package com.issue.manager.services.base;

import com.issue.manager.auth.JwtService;
import com.issue.manager.inputs.base.UserInput;
import com.issue.manager.inputs.dtos.AuthenticationRequest;
import com.issue.manager.inputs.dtos.AuthenticationResponse;
import com.issue.manager.models.base.User;
import com.issue.manager.models.constants.MessageConstants;
import com.issue.manager.models.project.*;
import com.issue.manager.repositories.base.UserRepository;
import com.issue.manager.repositories.project.InviteEventRepository;
import com.issue.manager.repositories.project.InviteRepository;
import com.issue.manager.repositories.project.NotificationRepository;
import com.issue.manager.repositories.project.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;

    private final InviteEventRepository inviteEventRepository;

    private final InviteRepository inviteRepository;

    private final NotificationRepository notificationRepository;

    private final ProjectRepository projectRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(UserInput request) {
        String firstName = request.getFirstName();
        if(firstName == null || firstName.length() == 0) {
            throw new IllegalArgumentException("Name must be at least one caharcter long");
        }

        String lastName = request.getLastName();
        if(lastName == null || lastName.length() == 0) {
            throw new IllegalArgumentException("Name must be at least one caharcter long");
        }

        String email = request.getEmail();
        if(email != null && email.length() > 0) {
            email = email.toLowerCase();

            Pattern pattern = Pattern.compile("^(.+)@(.+)$");
            if(!pattern.matcher(email).matches()) {
                throw new RuntimeException("Email did not validate");
            }

            if (userRepository.existsByEmail(email)) {
                throw new RuntimeException("User already exists with email " + email);
            }
        }

        User user = request.toModel();

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        User savedUser = userRepository.save(user);

        createInvites(savedUser);

        String jwtToken = jwtService.generateToken(savedUser);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    private void createInvites(User user) {
        List<InviteEvent> allByEmail = inviteEventRepository.findAllByEmail(user.getEmail());

        for (InviteEvent event : allByEmail) {
            if (projectRepository.existsById(event.getProject())) {
                inviteRepository.save(new Invite(user.getId(), event.getProject()));

                Notification notification = new Notification(
                        user.getId(),
                        NotificationType.ACCEPT,
                        "project-invites",
                        MessageConstants.INVITED_TO_PROJECT_NOTIFICATION_NAME,
                        MessageConstants.INVITED_TO_PROJECT_MESSAGE,
                        false
                );

                notificationRepository.save(notification);
            }
        }
    }

    public AuthenticationResponse login(AuthenticationRequest request) {
        String email = request.getEmail();
        if(email != null && email.length() > 0) {
            email = email.toLowerCase();

            Pattern pattern = Pattern.compile("^(.+)@(.+)$");
            if(!pattern.matcher(email).matches()) {
                throw new RuntimeException("Email did not validate");
            }
        }

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User was not found by email: " + request.getEmail()));

        String jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}
