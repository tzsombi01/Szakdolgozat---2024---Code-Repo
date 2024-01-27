package com.issue.manager.controllers.project;

import com.issue.manager.controllers.BaseCrudController;
import com.issue.manager.inputs.project.TicketInput;
import com.issue.manager.models.project.Ticket;
import com.issue.manager.repositories.EntityRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;

@Log4j2
@Controller
public class TicketController extends BaseCrudController<TicketInput, Ticket> {

    public TicketController(EntityRepository<Ticket> repository) {
        super(repository);
    }

    @Override
    protected Class<TicketInput> getInputClass() {
        return TicketInput.class;
    }

    @Override
    protected Class<Ticket> getModelClass() {
        return Ticket.class;
    }
}
