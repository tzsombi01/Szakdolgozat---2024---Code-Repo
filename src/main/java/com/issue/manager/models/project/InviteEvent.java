package com.issue.manager.models.project;

import com.issue.manager.models.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@FieldNameConstants
@Document(collection = InviteEvent.INVITE_EVENT_COLLECTION_NAME)
public class InviteEvent extends Entity {

    public static final String INVITE_EVENT_COLLECTION_NAME = "invite_events";

    private String email;
    private String project;
}
