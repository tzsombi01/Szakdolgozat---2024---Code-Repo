package com.issue.manager.services.project;

import com.issue.manager.inputs.project.TicketInput;
import com.issue.manager.models.core.Filter;
import com.issue.manager.models.core.QueryOptions;
import com.issue.manager.models.project.Project;
import com.issue.manager.models.project.Ticket;
import com.issue.manager.repositories.project.TicketRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

@Service
@Log4j2
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;
    private final ProjectService projectService;

    public Page<Ticket> getTickets(QueryOptions queryOptions) {
        Ticket exampleTicket = new Ticket();
        ExampleMatcher matcher = ExampleMatcher.matching();

        for (Filter filter : queryOptions.getFilters()) {
            if ("project".equals(filter.getField())) {
                exampleTicket.setProject((String) filter.getValue());
                matcher = matcher.withMatcher("project", ExampleMatcher.GenericPropertyMatchers.exact());
            }
        }

        Example<Ticket> example = Example.of(exampleTicket, matcher);

        Pageable pageable = PageRequest.of(queryOptions.getSkip(), queryOptions.getTake());

        return ticketRepository.findAll(example, pageable);
    }

    public Ticket createTicket(TicketInput ticketInput) {
        Ticket ticket = ticketInput.toModel();

        ticket.setTicketNumber(generateTicketNumber(ticket));

        return ticketRepository.save(ticket);
    }

    private Long generateTicketNumber(Ticket ticket) {
        Project project = projectService.getProject(ticket.getProject());
        return (long) (project.getTickets().size() + 1);
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

        ticketRepository.deleteById(id);

        return ticket;
    }
}
