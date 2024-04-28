package com.issue.manager.models.base;

import com.issue.manager.models.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = Role.ROLE_COLLECTION_NAME)
public class Role extends Entity {

    public static final String ROLE_COLLECTION_NAME = "roles";

    private String role;
    private String project; // Project reference

}