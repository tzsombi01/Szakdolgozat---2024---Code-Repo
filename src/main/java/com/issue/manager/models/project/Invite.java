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
@Document(collection = Invite.INVITE_COLLECTION_NAME)
public class Invite extends Entity {

    public static final String INVITE_COLLECTION_NAME = "invites";

    private String user;

    private String project;
}
