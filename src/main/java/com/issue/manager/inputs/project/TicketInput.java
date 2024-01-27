package com.issue.manager.inputs.project;

import com.issue.manager.inputs.ModelInput;
import com.issue.manager.models.project.Ticket;
import com.issue.manager.models.project.TicketStatus;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TicketInput extends ModelInput<Ticket> {

    private Long ticketNumber;
    private String assignee; // User reference
    private String creator; // User reference
    private String description;
    private String ticketReferences; // Ticket reference
    private List<TicketStatus> statuses;
    private List<String> comments; // Comment references
    private List<String> mentionedInCommits; // Commit references

    @Override
    public Ticket toModel(Ticket model) {
        model.setTicketNumber(ticketNumber);
        model.setAssignee(assignee);
        model.setCreator(creator);
        model.setDescription(description);
        model.setTicketReferences(ticketReferences);
        model.setStatuses(statuses);
        model.setComments(comments);
        model.setMentionedInCommits(mentionedInCommits);

        return model;
    }

}
