package com.issue.manager.services.project;

import com.issue.manager.inputs.project.TicketInput;
import com.issue.manager.models.project.Project;
import com.issue.manager.models.project.Ticket;
import com.issue.manager.repositories.project.TicketRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Log4j2
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;
    private final ProjectService projectService;

    public Page<Ticket> getTickets() {
        List<Ticket> all = ticketRepository.findAll();
        return new PageImpl<>(all);
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
