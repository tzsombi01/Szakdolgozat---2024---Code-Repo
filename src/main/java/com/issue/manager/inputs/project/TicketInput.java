package com.issue.manager.inputs.project;

import com.issue.manager.inputs.ModelInput;
import com.issue.manager.models.project.Ticket;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class TicketInput extends ModelInput<Ticket> {

    private Long ticketNumber;
    private String name;
    private String project; // Project reference
    private String assignee; // User reference
    private String description;
    private List<String> ticketReferences; // Ticket reference
    private List<String> statuses;
    private List<String> comments; // Comment references
    private List<String> mentionedInCommits; // Commit references
    private boolean closed;

    public Ticket toModel(Ticket model) {
        model.setName(name);
        model.setProject(project);
        model.setAssignee(assignee);
        model.setDescription(description);
        model.setTicketReferences(ticketReferences);
        model.setStatuses(statuses);
        model.setComments(comments);
        model.setMentionedInCommits(mentionedInCommits);
        model.setCreatedAt(new Date().getTime());
        model.setClosed(closed);

        return model;
    }

}
