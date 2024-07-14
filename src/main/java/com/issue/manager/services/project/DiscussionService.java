package com.issue.manager.services.project;

import com.issue.manager.inputs.project.DiscussionInput;
import com.issue.manager.models.project.Discussion;
import com.issue.manager.repositories.project.DiscussionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

@Service
@Log4j2
@RequiredArgsConstructor
public class DiscussionService {

    private final DiscussionRepository discussionRepository;

    public Page<Discussion> getDiscussions() {
        return new PageImpl<>(discussionRepository.findAll());
    }

    public Discussion getDiscussion(String id) {
        Discussion discussion = discussionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Discussion was not found by id " + id));

        return discussion;
    }

    public Discussion createDiscussion(DiscussionInput discussionInput) {
        Discussion discussion = discussionInput.toModel();

        return discussionRepository.save(discussion);
    }

    public Discussion editDiscussion(String id, DiscussionInput discussionInput) {
        Discussion discussion = discussionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Discussion was not found by id " + id));

        Discussion editedDiscussion = discussionInput.toModel(discussion);

        discussionRepository.save(editedDiscussion);

        return discussion;
    }

    public Discussion deleteDiscussion(String id) {
        Discussion discussion = discussionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Discussion was not found by id " + id));

        discussionRepository.deleteById(id);

        return discussion;
    }
}
