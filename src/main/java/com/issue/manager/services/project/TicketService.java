package com.issue.manager.services.project;

import com.issue.manager.inputs.project.TicketInput;
import com.issue.manager.models.project.Ticket;
import com.issue.manager.repositories.project.TicketRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@Log4j2
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;

    public Page<Ticket> getTickets() {
        return ticketRepository.findAll(
                PageRequest.of(1, 2)
        );
    }

    public Ticket createTicket(TicketInput ticketInput) {
        Ticket ticket = ticketInput.toModel();

        return ticketRepository.save(ticket);
    }
}
