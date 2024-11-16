package com.issue.manager.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.issue.manager.auth.JwtService;
import com.issue.manager.inputs.project.TicketInput;
import com.issue.manager.models.base.User;
import com.issue.manager.models.project.Project;
import com.issue.manager.models.project.Ticket;
import com.issue.manager.repositories.base.UserRepository;
import com.issue.manager.repositories.project.ProjectRepository;
import com.issue.manager.repositories.project.TicketRepository;
import com.issue.manager.utils.EmailService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class TicketControllerTest {

    public static final String baseTicketEndpointUrl = "/api/tickets";

    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtService jwtService;

    @MockBean
    private EmailService emailService;

    @Autowired
    private MockMvc mockMvc;

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
        projectRepository.deleteAll();
        ticketRepository.deleteAll();
    }

    @Test
    void itShould_createTicket() throws Exception {
        User user = new User();
        user.setEmail("test@gmail.com");
        user.setUserName("test");

        User savedUser = userRepository.save(user);


        Project project = new Project("testUrl", "test", List.of(), List.of());
        Project savedProject = projectRepository.save(project);

        TicketInput ticketInput = new TicketInput();
        ticketInput.setProject(savedProject.getId());
        ticketInput.setName("Ticket 1");

        // Mock the SecurityContext and Authentication
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Authentication authentication = Mockito.mock(Authentication.class);

        Mockito.when(authentication.getPrincipal()).thenReturn(savedUser);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        String ticketInputJson = new ObjectMapper().writeValueAsString(ticketInput);

        String token = jwtService.generateToken(savedUser);
        mockMvc.perform(post(baseTicketEndpointUrl)
                        .header("authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ticketInputJson))
                .andExpect(status().isCreated());

        List<Ticket> all = ticketRepository.findAll();
        assertThat(all.size()).isPositive();
    }
}
