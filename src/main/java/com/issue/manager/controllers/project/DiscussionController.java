package com.issue.manager.controllers.project;

import com.issue.manager.inputs.project.DiscussionInput;
import com.issue.manager.models.core.QueryOptions;
import com.issue.manager.models.project.Discussion;
import com.issue.manager.services.project.DiscussionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/discussions")
@RequiredArgsConstructor
public class DiscussionController {

    private final DiscussionService discussionService;

    @PostMapping("/get")
    @ResponseStatus(HttpStatus.OK)
    public Page<Discussion> getDiscussions(@RequestBody QueryOptions queryOptions) {
        return discussionService.getDiscussions(queryOptions);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Discussion getDiscussion(@PathVariable String id) {
        return discussionService.getDiscussion(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Discussion createDiscussion(@RequestBody DiscussionInput discussionInput) {
        return discussionService.createDiscussion(discussionInput);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Discussion editDiscussion(@PathVariable String id, @RequestBody DiscussionInput discussionInput) {
        return discussionService.editDiscussion(id, discussionInput);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Discussion deleteDiscussion(@PathVariable String id) {
        return discussionService.deleteDiscussion(id);
    }
}
