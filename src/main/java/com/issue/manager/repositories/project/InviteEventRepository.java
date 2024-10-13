package com.issue.manager.repositories.project;

import com.issue.manager.models.project.InviteEvent;
import com.issue.manager.repositories.EntityRepository;

import java.util.List;

public interface InviteEventRepository extends EntityRepository<InviteEvent> {

    List<InviteEvent> findAllByEmail(String email);
}
