package com.issue.manager.services.project;

import com.issue.manager.inputs.project.NotificationInput;
import com.issue.manager.models.core.Filter;
import com.issue.manager.models.core.QueryOptions;
import com.issue.manager.models.project.Notification;
import com.issue.manager.repositories.project.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

@Service
@Log4j2
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public Page<Notification> getNotifications(QueryOptions queryOptions) {
        Notification exampleNotification = new Notification();
        ExampleMatcher matcher = ExampleMatcher.matching();

        if (queryOptions.getFilters() != null) {
            for (Filter filter : queryOptions.getFilters()) {
                if ("target".equals(filter.getField())) {
                    exampleNotification.setTarget((String) filter.getValue());
                    matcher = matcher.withMatcher("target", ExampleMatcher.GenericPropertyMatchers.exact());
                }
            }
        }

        Example<Notification> example = Example.of(exampleNotification, matcher);

        Pageable pageable = PageRequest.of(queryOptions.getSkip(), queryOptions.getTake() > 0 ? queryOptions.getTake() : 10);

        return notificationRepository.findAll(example, pageable);
    }

    public Notification getNotification(String id) {
        return notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification was not found by id " + id));
    }

    public Notification createNotification(NotificationInput notificationInput) {
        Notification notification = notificationInput.toModel();

        return notificationRepository.save(notification);
    }

    public Notification editNotification(String id, NotificationInput notificationInput) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification was not found by id " + id));

        Notification editedNotification = notificationInput.toModel(notification);

        notificationRepository.save(editedNotification);

        return notification;
    }

    public Notification deleteNotification(String id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification was not found by id " + id));

        notificationRepository.deleteById(id);

        return notification;
    }
}
