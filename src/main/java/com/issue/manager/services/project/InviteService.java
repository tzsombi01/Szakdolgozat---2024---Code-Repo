package com.issue.manager.services.project;

import com.issue.manager.inputs.project.InviteInput;
import com.issue.manager.models.core.Filter;
import com.issue.manager.models.core.QueryOptions;
import com.issue.manager.models.project.Invite;
import com.issue.manager.repositories.project.InviteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

@Service
@Log4j2
@RequiredArgsConstructor
public class InviteService {

    private final InviteRepository inviteRepository;

    public Page<Invite> getInvites(QueryOptions queryOptions) {
        Invite exampleInvite = new Invite();
        ExampleMatcher matcher = ExampleMatcher.matching();

        if (queryOptions.getFilters() != null) {
            for (Filter filter : queryOptions.getFilters()) {
                if ("user".equals(filter.getField())) {
                    exampleInvite.setUser((String) filter.getValue());
                    matcher = matcher.withMatcher("user", ExampleMatcher.GenericPropertyMatchers.exact());
                }
            }
        }

        Example<Invite> example = Example.of(exampleInvite, matcher);

        Pageable pageable = PageRequest.of(queryOptions.getSkip(), queryOptions.getTake() > 0 ? queryOptions.getTake() : 10);

        return inviteRepository.findAll(example, pageable);
    }

    public Invite getInvite(String id) {
        return inviteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Invite was not found by id " + id));
    }

    public Invite createInvite(InviteInput inviteInput) {
        Invite invite = inviteInput.toModel();

        return inviteRepository.save(invite);
    }

    public Invite editInvite(String id, InviteInput inviteInput) {
        Invite invite = inviteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Invite was not found by id " + id));

        Invite editedInvite = inviteInput.toModel(invite);

        inviteRepository.save(editedInvite);

        return editedInvite;
    }

    public Invite deleteInvite(String id) {
        Invite invite = inviteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Invite was not found by id " + id));

        inviteRepository.deleteById(id);

        return invite;
    }
}
