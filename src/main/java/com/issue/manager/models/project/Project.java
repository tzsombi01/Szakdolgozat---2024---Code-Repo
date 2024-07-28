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
@Document(collection = Project.PROJECT_COLLECTION_NAME)
public class Project extends Entity {

    public static final String PROJECT_COLLECTION_NAME = "projects";

    private String url;
    private String name;
    private List<String> tickets; // Ticket references
    private List<String> users; // User references

    public void addUser(String id) {
        if (users == null) {
            users = new ArrayList<>();
        }

        users.add(id);
    }

    public void deleteUser(String id) {
        if (users == null) {
            users = new ArrayList<>();
            return;
        }

        users.remove(id);
    }
}

