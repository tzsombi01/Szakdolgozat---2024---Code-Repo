package com.issue.manager.services.project;

import com.issue.manager.inputs.project.TicketInput;
import com.issue.manager.models.base.User;
import com.issue.manager.models.core.Filter;
import com.issue.manager.models.core.QueryOptions;
import com.issue.manager.models.project.Project;
import com.issue.manager.models.project.Ticket;
import com.issue.manager.repositories.project.ProjectRepository;
import com.issue.manager.repositories.project.TicketRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@Log4j2
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;
    private final ProjectRepository projectRepository;

    public Page<Ticket> getTickets(QueryOptions queryOptions) {
        Ticket exampleTicket = new Ticket();
        ExampleMatcher matcher = ExampleMatcher.matching();

        if (queryOptions.getFilters() != null) {
            for (Filter filter : queryOptions.getFilters()) {
                if ("project".equals(filter.getField())) {
                    exampleTicket.setProject((String) filter.getValue());
                    matcher = matcher.withMatcher("project", ExampleMatcher.GenericPropertyMatchers.exact());
                }
            }
        }

        Example<Ticket> example = Example.of(exampleTicket, matcher);

        Pageable pageable = PageRequest.of(queryOptions.getSkip(), queryOptions.getTake());

        return ticketRepository.findAll(example, pageable);
    }

    public Ticket createTicket(TicketInput ticketInput) {
        Ticket ticket = ticketInput.toModel();

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ticket.setCreator(user.getId());
        ticket.setTicketNumber(generateTicketNumber(ticket));

        return ticketRepository.save(ticket);
    }

    private Long generateTicketNumber(Ticket ticket) {
        Project project = projectRepository.findById(ticket.getProject())
                .orElseThrow(() -> new RuntimeException("Project was not found by id " + ticket.getProject()));

        Long maxTicketNumber = project.getTickets().stream().map(id -> ticketRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket was not found by id " + id)))
                .max(Comparator.comparing(Ticket::getTicketNumber))
                .map(Ticket::getTicketNumber)
                .orElseThrow(() -> new RuntimeException("No tickets found in the project"));

        return maxTicketNumber + 1;
    }

    public Ticket getTicket(String id) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket was not found by id " + id));

        return ticket;
    }

    public Ticket editTicket(String id, TicketInput ticketInput) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket was not found by id " + id));

        Ticket editedTicket = ticketInput.toModel(ticket);

        return ticket;
    }

    public Ticket deleteTicket(String id) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket was not found by id " + id));

        Project project = projectRepository.findById(ticket.getProject())
                .orElseThrow(() -> new RuntimeException("Project was not found by id " + ticket.getProject()));
        project.removeTicket(id);
        projectRepository.save(project);

        ticketRepository.deleteById(id);

        return ticket;
    }
}
