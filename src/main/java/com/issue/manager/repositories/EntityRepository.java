package com.issue.manager.repositories;

import com.issue.manager.models.Entity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EntityRepository<Model extends Entity> extends MongoRepository<Model, String> {
}
