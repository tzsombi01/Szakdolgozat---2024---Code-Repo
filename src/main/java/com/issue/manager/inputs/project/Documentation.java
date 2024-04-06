package com.issue.manager.inputs.project;

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
@Document(collation = Documentation.DOCUMENTATION_COLLECTION_NAME)
public class Documentation extends Entity {
    public static final String DOCUMENTATION_COLLECTION_NAME = "documentations";

    private String creator; // Reference to User
    private List<String> comments;
    private String description;
}
