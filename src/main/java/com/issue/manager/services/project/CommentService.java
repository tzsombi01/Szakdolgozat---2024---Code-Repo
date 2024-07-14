package com.issue.manager.services.project;

import com.issue.manager.inputs.project.CommentInput;
import com.issue.manager.models.core.QueryOptions;
import com.issue.manager.models.project.Comment;
import com.issue.manager.repositories.project.CommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Log4j2
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    public Page<Comment> getComments(QueryOptions queryOptions) {
        List<Comment> all = commentRepository.findAll();

        return new PageImpl<>(all);
    }

    public Comment getComment(String id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment was not found by id " + id));

        return comment;
    }

    public Comment createComment(CommentInput commentInput) {
        Comment comment = commentInput.toModel();

        return commentRepository.save(comment);
    }

    public Comment editComment(String id, CommentInput commentInput) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment was not found by id " + id));

        Comment editedComment = commentInput.toModel(comment);

        commentRepository.save(editedComment);

        return comment;
    }

    public Comment deleteComment(String id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment was not found by id " + id));

        commentRepository.deleteById(id);

        return comment;
    }
}
