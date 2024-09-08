package com.issue.manager.services.project;

import com.issue.manager.inputs.project.DiscussionInput;
import com.issue.manager.models.base.User;
import com.issue.manager.models.core.Filter;
import com.issue.manager.models.core.QueryOptions;
import com.issue.manager.models.project.Discussion;
import com.issue.manager.repositories.project.DiscussionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@Log4j2
@RequiredArgsConstructor
public class DiscussionService {

    private final DiscussionRepository discussionRepository;

    public Page<Discussion> getDiscussions(QueryOptions queryOptions) {
        Discussion exampleDiscussion = new Discussion();
        ExampleMatcher matcher = ExampleMatcher.matching();

        if (queryOptions.getFilters() != null) {
            for (Filter filter : queryOptions.getFilters()) {
                if ("project".equals(filter.getField())) {
                    exampleDiscussion.setProject((String) filter.getValue());
                    matcher = matcher.withMatcher("project", ExampleMatcher.GenericPropertyMatchers.exact());
                }
            }
        }

        Example<Discussion> example = Example.of(exampleDiscussion, matcher);

        Pageable pageable = PageRequest.of(queryOptions.getSkip(), queryOptions.getTake() > 0 ? queryOptions.getTake() : 10);

        return discussionRepository.findAll(example, pageable);
    }

    public Discussion getDiscussion(String id) {
        Discussion discussion = discussionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Discussion was not found by id " + id));

        return discussion;
    }

    public Discussion createDiscussion(DiscussionInput discussionInput) {
        Discussion discussion = discussionInput.toModel();

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        discussion.setCreator(user.getId());

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
