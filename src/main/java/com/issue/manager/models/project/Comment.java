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
@Document(collation = Comment.COMMENTS_COLLECTION_NAME)
public class Comment extends Entity {

    public static final String COMMENTS_COLLECTION_NAME = "comments";

    private String creator; // Reference to User
    private String description;
    private boolean edited;
}
