package com.issue.manager.controllers.project;

import com.issue.manager.inputs.project.CommentInput;
import com.issue.manager.models.core.QueryOptions;
import com.issue.manager.models.project.Comment;
import com.issue.manager.services.project.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/get")
    @ResponseStatus(HttpStatus.OK)
    public Page<Comment> getComments(@RequestBody QueryOptions queryOptions) {
        return commentService.getComments(queryOptions);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Comment getComment(@PathVariable String id) {
        return commentService.getComment(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Comment createComment(@RequestBody CommentInput commentInput) {
        return commentService.createComment(commentInput);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Comment editComment(@PathVariable String id, @RequestBody CommentInput commentInput) {
        return commentService.editComment(id, commentInput);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Comment deleteComment(@PathVariable String id) {
        return commentService.deleteComment(id);
    }
}
