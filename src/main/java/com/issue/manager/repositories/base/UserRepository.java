package com.issue.manager.repositories.base;

import com.issue.manager.models.base.User;
import com.issue.manager.repositories.EntityRepository;

import java.util.Optional;

public interface UserRepository extends EntityRepository<User> {
    Optional<User> findByEmail(String userName);

    boolean existsByEmail(String email);
}
