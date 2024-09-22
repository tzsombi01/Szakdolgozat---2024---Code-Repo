package com.issue.manager.controllers.project;

import com.issue.manager.inputs.project.StatusInput;
import com.issue.manager.models.core.QueryOptions;
import com.issue.manager.models.project.Status;
import com.issue.manager.services.project.StatusService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Log4j2
@RestController
@RequestMapping("/api/statuses")
@RequiredArgsConstructor
public class StatusController {

    private final StatusService statusService;

    @PostMapping("/get")
    @ResponseStatus(HttpStatus.OK)
    public List<Status> getStatuses(@RequestBody QueryOptions queryOptions) {
        return statusService.getStatuses(queryOptions);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Status getStatus(@PathVariable String id) {
        return statusService.getStatus(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Status createStatus(@RequestBody StatusInput statusInput) {
        return statusService.createStatus(statusInput);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Status editStatus(@PathVariable String id, @RequestBody StatusInput statusInput) {
        return statusService.editStatus(id, statusInput);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Status deleteStatus(@PathVariable String id) {
        return statusService.deleteStatus(id);
    }
}
