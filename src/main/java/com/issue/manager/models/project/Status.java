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
@Document(collection = Status.STATUS_COLLECTION_NAME)
public class Status extends Entity {

    public static final String STATUS_COLLECTION_NAME = "statuses";

    private String name;
    private StatusType type;
    private String project;
}
