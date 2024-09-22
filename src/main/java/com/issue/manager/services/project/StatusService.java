package com.issue.manager.services.project;

import com.issue.manager.inputs.project.StatusInput;
import com.issue.manager.models.core.Filter;
import com.issue.manager.models.core.QueryOptions;
import com.issue.manager.models.project.Comment;
import com.issue.manager.models.project.Status;
import com.issue.manager.repositories.project.StatusRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Log4j2
@RequiredArgsConstructor
public class StatusService {

    private final StatusRepository statusRepository;

    public List<Status> getStatuses(QueryOptions queryOptions) {
        Status exampleStatus = new Status();
        ExampleMatcher matcher = ExampleMatcher.matching();

        if (queryOptions.getFilters() != null) {
            for (Filter filter : queryOptions.getFilters()) {
                if ("project".equals(filter.getField())) {
                    exampleStatus.setProject((String) filter.getValue());
                    matcher = matcher.withMatcher("project", ExampleMatcher.GenericPropertyMatchers.exact());
                }
            }
        }

        Example<Status> example = Example.of(exampleStatus, matcher);

        return statusRepository.findAll(example);
    }

    public Status getComment(String id) {
        Status status = statusRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Status was not found by id " + id));

        return status;
    }

    public Status createStatus(StatusInput statusInput) {
        Status status = statusInput.toModel();

        return statusRepository.save(status);
    }

    public Status getStatus(String id) {
        Status status = statusRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Status was not found by id " + id));

        return status;
    }

    public Status editStatus(String id, StatusInput statusInput) {
        Status status = statusRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Status was not found by id " + id));

        Status editedStatus = statusInput.toModel(status);
        statusRepository.save(editedStatus);
        return editedStatus;
    }

    public Status deleteStatus(String id) {
        Status status = statusRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Status was not found by id " + id));

        statusRepository.deleteById(id);

        return status;
    }
}
