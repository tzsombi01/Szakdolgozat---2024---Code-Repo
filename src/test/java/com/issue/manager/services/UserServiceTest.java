package com.issue.manager.services;

import com.issue.manager.inputs.dtos.InviteUsersRequest;
import com.issue.manager.models.base.User;
import com.issue.manager.models.project.EmailType;
import com.issue.manager.models.project.Invite;
import com.issue.manager.models.project.InviteEvent;
import com.issue.manager.models.project.Project;
import com.issue.manager.repositories.base.UserRepository;
import com.issue.manager.repositories.project.InviteEventRepository;
import com.issue.manager.repositories.project.InviteRepository;
import com.issue.manager.repositories.project.NotificationRepository;
import com.issue.manager.repositories.project.ProjectRepository;
import com.issue.manager.services.base.UserService;
import com.issue.manager.utils.EmailService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class UserServiceTest {


    private UserService userServiceUnderTest;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private InviteEventRepository inviteEventRepository;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private InviteRepository inviteRepository;

    @MockBean
    private EmailService emailService;

    @BeforeEach
    void setUp() {
        userServiceUnderTest = new UserService(userRepository, inviteEventRepository, projectRepository, notificationRepository, inviteRepository, emailService);
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
        inviteEventRepository.deleteAll();
        projectRepository.deleteAll();
        notificationRepository.deleteAll();
        inviteRepository.deleteAll();
    }

    @Test
    void canInviteUserToProject_ExistingInDb() {
        // given
        User user = new User();
        user.setEmail("someemail@gmail.com");

        User savedUser = userRepository.save(user);

        Project project = new Project("testUrl", "test", List.of(), List.of());
        Project savedProject = projectRepository.save(project);

        InviteUsersRequest inviteUsersRequest = new InviteUsersRequest();
        inviteUsersRequest.setEmails(List.of(savedUser.getEmail()));
        inviteUsersRequest.setProjectId(savedProject.getId());

        // when
        userServiceUnderTest.inviteUsersToProject(inviteUsersRequest);

        // then
        Optional<Invite> optionalInvite = inviteRepository.findByUser(savedUser.getId());

        assertThat(optionalInvite).isPresent();
        Invite invite = optionalInvite.get();
        assertThat(invite.getProject()).isEqualTo(savedProject.getId());
        assertThat(invite.getUser()).isEqualTo(savedUser.getId());
    }
    @Test
    void canInviteUserToProject_DoesNotExistingInDb() throws Exception {
        // given
        Project project = new Project("testUrl", "test", List.of(), List.of());
        Project savedProject = projectRepository.save(project);

        String email = "test@gmail.com";
        InviteUsersRequest inviteUsersRequest = new InviteUsersRequest();
        inviteUsersRequest.setEmails(List.of(email));
        inviteUsersRequest.setProjectId(savedProject.getId());

        Mockito.doNothing().when(emailService).sendEmail(
                Mockito.eq(email),
                Mockito.eq(EmailType.NEW_USER_INVITED_TO_PROJECT),
                Mockito.anyMap()
        );
        // when
        userServiceUnderTest.inviteUsersToProject(inviteUsersRequest);

        // then
        List<InviteEvent> optionalInviteEvents = inviteEventRepository.findAllByEmail(email);

        assertThat(optionalInviteEvents.size()).isPositive();
        InviteEvent inviteEvent = optionalInviteEvents.get(0);
        assertThat(inviteEvent.getProject()).isEqualTo(savedProject.getId());
        assertThat(inviteEvent.getEmail()).isEqualTo(email);
    }
}
