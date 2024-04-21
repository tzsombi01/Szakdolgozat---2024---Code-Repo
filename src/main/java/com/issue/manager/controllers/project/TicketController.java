package com.issue.manager.controllers.project;

import com.issue.manager.inputs.project.TicketInput;
import com.issue.manager.models.core.QueryOptions;
import com.issue.manager.models.project.Ticket;
import com.issue.manager.services.project.TicketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Log4j2
@Controller
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    @PostMapping("/get")
    @ResponseStatus(HttpStatus.OK)
    public Page<Ticket> getTickets(@RequestBody QueryOptions queryOptions) {
        return ticketService.getTickets();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Ticket createTicket(@RequestBody TicketInput ticketInput) {
        return ticketService.createTicket(ticketInput);
    }
}
