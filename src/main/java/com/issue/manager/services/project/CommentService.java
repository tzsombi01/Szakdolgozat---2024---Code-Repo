package com.issue.manager.services.project;

import com.issue.manager.inputs.project.CommentInput;
import com.issue.manager.models.core.Filter;
import com.issue.manager.models.core.QueryOptions;
import com.issue.manager.models.project.Comment;
import com.issue.manager.models.project.Discussion;
import com.issue.manager.models.project.Documentation;
import com.issue.manager.models.project.Ticket;
import com.issue.manager.repositories.project.CommentRepository;
import com.issue.manager.repositories.project.DiscussionRepository;
import com.issue.manager.repositories.project.DocumentationRepository;
import com.issue.manager.repositories.project.TicketRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Log4j2
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final TicketRepository ticketRepository;
    private final DiscussionRepository discussionRepository;
    private final DocumentationRepository documentationRepository;

    public  List<Comment> getComments(QueryOptions queryOptions) {
        Comment exampleComment = new Comment();
        ExampleMatcher matcher = ExampleMatcher.matching();

        if (queryOptions.getFilters() != null) {
            for (Filter filter : queryOptions.getFilters()) {
                if ("reference".equals(filter.getField())) {
                    exampleComment.setReference((String) filter.getValue());
                    matcher = matcher.withMatcher("reference", ExampleMatcher.GenericPropertyMatchers.exact());
                }
            }
        }

        Example<Comment> example = Example.of(exampleComment, matcher);

        List<Comment> all = commentRepository.findAll(example);

        return all;
    }

    public Comment getComment(String id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment was not found by id " + id));

        return comment;
    }

    public Comment createComment(CommentInput commentInput) {
        Comment comment = commentInput.toModel();

        Comment savedComment = commentRepository.save(comment);

        postCreate(savedComment);

        return savedComment;
    }

    private void postCreate(Comment comment) {
        switch(comment.getCommentType()) {
            case Ticket -> {
                Ticket ticket = ticketRepository.findById(comment.getReference()).orElseThrow(() -> new RuntimeException("Ticket was not found with id: " + comment.getReference()));

                ticket.addComment(comment.getId());

                ticketRepository.save(ticket);
            }
            case Discussion -> {
                Discussion discussion = discussionRepository.findById(comment.getReference()).orElseThrow(() -> new RuntimeException("Discussion was not found with id: " + comment.getReference()));

                discussion.addComment(comment.getId());

                discussionRepository.save(discussion);
            }
            case Documentation -> {
                Documentation documentation = documentationRepository.findById(comment.getReference()).orElseThrow(() -> new RuntimeException("Documentation was not found with id: " + comment.getReference()));

                documentation.addComment(comment.getId());

                documentationRepository.save(documentation);
            }
            default -> throw new RuntimeException("Commenting is not allowed under this resource!");
        }
    }

    public Comment editComment(String id, CommentInput commentInput) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment was not found by id " + id));

        Comment editedComment = commentInput.toModel(comment);

        editedComment.setEdited(true);

        commentRepository.save(editedComment);

        return comment;
    }

    public Comment deleteComment(String id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment was not found by id " + id));

        commentRepository.deleteById(id);

        postDelete(comment);

        return comment;
    }

    private void postDelete(Comment comment) {
        switch(comment.getCommentType()) {
            case Ticket -> {
                Ticket ticket = ticketRepository.findById(comment.getReference()).orElseThrow(() -> new RuntimeException("Ticket was not found with id: " + comment.getReference()));

                ticket.deleteComment(comment.getId());

                ticketRepository.save(ticket);
            }
            case Discussion -> {
                Discussion discussion = discussionRepository.findById(comment.getReference()).orElseThrow(() -> new RuntimeException("Discussion was not found with id: " + comment.getReference()));

                discussion.deleteComment(comment.getId());

                discussionRepository.save(discussion);
            }
            case Documentation -> {
                Documentation documentation = documentationRepository.findById(comment.getReference()).orElseThrow(() -> new RuntimeException("Documentation was not found with id: " + comment.getReference()));

                documentation.deleteComment(comment.getId());

                documentationRepository.save(documentation);
            }
            default -> throw new RuntimeException("Comments are not allowed under this resource!");
        }
    }
}
