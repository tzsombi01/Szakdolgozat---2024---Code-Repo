package com.issue.manager.services.project;

import com.issue.manager.inputs.project.DocumentationInput;
import com.issue.manager.models.base.User;
import com.issue.manager.models.core.Filter;
import com.issue.manager.models.core.QueryOptions;
import com.issue.manager.models.project.Documentation;
import com.issue.manager.repositories.project.DocumentationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@Log4j2
@RequiredArgsConstructor
public class DocumentationService {

    private final DocumentationRepository documentationRepository;

    public Page<Documentation> getDocumentations(QueryOptions queryOptions) {
        Documentation exampleDocumentation = new Documentation();
        ExampleMatcher matcher = ExampleMatcher.matching();

        if (queryOptions.getFilters() != null) {
            for (Filter filter : queryOptions.getFilters()) {
                if ("project".equals(filter.getField())) {
                    exampleDocumentation.setProject((String) filter.getValue());
                    matcher = matcher.withMatcher("project", ExampleMatcher.GenericPropertyMatchers.exact());
                }
            }
        }

        Example<Documentation> example = Example.of(exampleDocumentation, matcher);

        Pageable pageable = PageRequest.of(queryOptions.getSkip(), queryOptions.getTake() > 0 ? queryOptions.getTake() : 10);

        return documentationRepository.findAll(example, pageable);
    }

    public Documentation getDocumentation(String id) {
        Documentation documentation = documentationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Documentation was not found by id " + id));

        return documentation;
    }

    public Documentation createDocumentation(DocumentationInput documentationInput) {
        Documentation documentation = documentationInput.toModel();

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        documentation.setCreator(user.getId());

        return documentationRepository.save(documentation);
    }

    public Documentation editDocumentation(String id, DocumentationInput documentationInput) {
        Documentation documentation = documentationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Documentation was not found by id " + id));

        Documentation editedDocumentation = documentationInput.toModel(documentation);

        documentationRepository.save(editedDocumentation);

        return documentation;
    }

    public Documentation deleteDocumentation(String id) {
        Documentation documentation = documentationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Documentation was not found by id " + id));

        documentationRepository.deleteById(id);

        return documentation;
    }
}
