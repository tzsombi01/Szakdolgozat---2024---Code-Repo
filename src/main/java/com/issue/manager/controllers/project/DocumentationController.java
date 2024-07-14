package com.issue.manager.controllers.project;

import com.issue.manager.inputs.project.DocumentationInput;
import com.issue.manager.models.core.QueryOptions;
import com.issue.manager.models.project.Documentation;
import com.issue.manager.services.project.DocumentationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/documentations")
@RequiredArgsConstructor
public class DocumentationController {

    private final DocumentationService documentationService;

    @PostMapping("/get")
    @ResponseStatus(HttpStatus.OK)
    public Page<Documentation> getDocumentations(@RequestBody QueryOptions queryOptions) {
        return documentationService.getDocumentations(queryOptions);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Documentation getDocumentation(@PathVariable String id) {
        return documentationService.getDocumentation(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Documentation createDocumentation(@RequestBody DocumentationInput documentationInput) {
        return documentationService.createDocumentation(documentationInput);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Documentation editDocumentation(@PathVariable String id, @RequestBody DocumentationInput documentationInput) {
        return documentationService.editDocumentation(id, documentationInput);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Documentation deleteDocumentation(@PathVariable String id) {
        return documentationService.deleteDocumentation(id);
    }
}
