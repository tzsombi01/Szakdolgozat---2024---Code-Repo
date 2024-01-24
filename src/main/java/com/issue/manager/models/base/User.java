package com.issue.manager.models.base;

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
@Document(collection = User.USERS_COLLECTION_NAME)
public class User extends Entity {

    public static final String USERS_COLLECTION_NAME = "users";

    private String userName;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String accessToken; // GitHub access token
    private boolean active;
    private boolean locked;
}
