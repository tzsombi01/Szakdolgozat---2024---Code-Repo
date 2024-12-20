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
@Document(collection = Notification.NOTIFICATION_COLLECTION_NAME)
public class Notification extends Entity {

    public static final String NOTIFICATION_COLLECTION_NAME = "notifications";

    private String target;
    private NotificationType notificationType;
    private String path;
    private String name;
    private String message;
    private boolean seen;
}
