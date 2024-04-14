package com.issue.manager.controllers.project;

import com.issue.manager.inputs.project.TicketInput;
import com.issue.manager.models.core.QueryOptions;
import com.issue.manager.models.project.Ticket;
import com.issue.manager.services.project.TicketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Log4j2
@Controller
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    @GetMapping
    public ResponseEntity<Page<Ticket>> getTickets(@RequestBody QueryOptions queryOptions) {
        return new ResponseEntity<>(ticketService.getTickets(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Ticket> createTicket(@RequestBody TicketInput ticketInput) {
        return new ResponseEntity<>(ticketService.createTicket(ticketInput), HttpStatus.CREATED);
    }
}
