package com.issue.manager.inputs.project;

import com.issue.manager.inputs.ModelInput;
import com.issue.manager.models.project.Notification;
import com.issue.manager.models.project.NotificationType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotificationInput extends ModelInput<Notification> {

    private String target;
    private NotificationType notificationType;
    private String path;
    private String name;
    private String message;
    private boolean seen;

    public Notification toModel(Notification model) {
        model.setTarget(target);
        model.setNotificationType(notificationType);
        model.setPath(path);
        model.setName(name);
        model.setMessage(message);
        model.setSeen(seen);

        return model;
    }
}
