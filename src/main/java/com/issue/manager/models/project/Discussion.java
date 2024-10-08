package com.issue.manager.models.project;

import com.issue.manager.models.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@FieldNameConstants
@Document(collection = Discussion.DISCUSSION_COLLECTION_NAME)
public class Discussion extends Entity {

    public static final String DISCUSSION_COLLECTION_NAME = "discussions";

    private String creator; // Reference to User
    private String name;
    private String project;
    private List<String> comments;
    private String description;

    public void addComment(String id) {
        if (comments == null) {
            comments = new ArrayList<>();
        }

        comments.add(id);
    }

    public void deleteComment(String id) {
        if (comments == null) {
            comments = new ArrayList<>();
            return;
        }

        comments.remove(id);
    }
}
