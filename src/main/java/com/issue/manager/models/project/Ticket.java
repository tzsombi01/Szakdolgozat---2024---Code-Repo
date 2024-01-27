package com.issue.manager.models.project;

import com.issue.manager.models.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@FieldNameConstants
@Document(collection = Ticket.TICKET_COLLECTION_NAME)
public class Ticket extends Entity {

    public static final String TICKET_COLLECTION_NAME = "tickets";

    private Long ticketNumber;
    private String assignee; // User reference
    private String creator; // User reference
    private String description;
    private String ticketReferences; // Ticket reference
    private List<TicketStatus> statuses;
    private List<String> comments; // Comment references
    private List<String> mentionedInCommits; // Commit references

}
