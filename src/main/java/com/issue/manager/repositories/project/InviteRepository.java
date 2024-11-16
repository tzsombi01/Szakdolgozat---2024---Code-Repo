package com.issue.manager.repositories.project;

import com.issue.manager.models.project.Invite;
import com.issue.manager.repositories.EntityRepository;

import java.util.Optional;

public interface InviteRepository extends EntityRepository<Invite> {

    Optional<Invite> findByUser(String user);
}
