package com.issue.manager.controllers.project;

import com.issue.manager.inputs.project.NotificationInput;
import com.issue.manager.models.core.QueryOptions;
import com.issue.manager.models.project.Notification;
import com.issue.manager.services.project.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping("/get")
    @ResponseStatus(HttpStatus.OK)
    public Page<Notification> getNotifications(@RequestBody QueryOptions queryOptions) {
        return notificationService.getNotifications(queryOptions);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Notification getNotification(@PathVariable String id) {
        return notificationService.getNotification(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Notification createNotification(@RequestBody NotificationInput notificationInput) {
        return notificationService.createNotification(notificationInput);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Notification editNotification(@PathVariable String id, @RequestBody NotificationInput notificationInput) {
        return notificationService.editNotification(id, notificationInput);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Notification deleteNotification(@PathVariable String id) {
        return notificationService.deleteNotification(id);
    }
}
