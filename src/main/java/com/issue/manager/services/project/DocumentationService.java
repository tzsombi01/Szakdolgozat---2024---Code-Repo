package com.issue.manager.services.project;

import com.issue.manager.inputs.project.DocumentationInput;
import com.issue.manager.models.core.QueryOptions;
import com.issue.manager.models.project.Documentation;
import com.issue.manager.repositories.project.DocumentationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Log4j2
@RequiredArgsConstructor
public class DocumentationService {

    private final DocumentationRepository documentationRepository;

    public Page<Documentation> getDocumentations(QueryOptions queryOptions) {
        List<Documentation> all = documentationRepository.findAll();

        return new PageImpl<>(all);
    }

    public Documentation getDocumentation(String id) {
        Documentation documentation = documentationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Documentation was not found by id " + id));

        return documentation;
    }

    public Documentation createDocumentation(DocumentationInput documentationInput) {
        Documentation documentation = documentationInput.toModel();

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
