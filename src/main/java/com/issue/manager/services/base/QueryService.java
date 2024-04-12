package com.issue.manager.services.base;

import com.issue.manager.models.Entity;
import com.issue.manager.models.core.QueryOptions;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@RequiredArgsConstructor
public class QueryService {

    private final MongoTemplate mongoTemplate;

    public <Model extends Entity> Page<Model> findEntity(QueryOptions queryOptions, Class<Model> modelClass) {
        return find(queryOptions, modelClass);
    }

    public <Model> Page<Model> find(QueryOptions queryOptions, Class<Model> modelClass) {
        Criteria criteria = new Criteria();

        Query query = new Query(criteria);

        if (queryOptions.getSort() != null) {

            log.debug(String.format("Setting sort for %s field", queryOptions.getSort().getField()));

            if (queryOptions.getSort().getDir().equals("asc")) {
                query.with(Sort.by(Sort.Order.asc(queryOptions.getSort().getField())));
            } else if (queryOptions.getSort().getDir().equals("desc")) {
                query.with(Sort.by(Sort.Order.desc(queryOptions.getSort().getField())));
            }
        }

        log.debug("Finding entities in DB.");

        long totalCount = mongoTemplate.count(query, modelClass);

        return new PageImpl<>(mongoTemplate.find(query.skip(queryOptions.getSkip()).limit(queryOptions.getTake()), modelClass),
                Pageable.unpaged(), totalCount);

    }

}