package com.issue.manager.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.issue.manager.controllers.base.Method;
import com.issue.manager.inputs.ModelInput;
import com.issue.manager.models.Entity;
import com.issue.manager.repositories.EntityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Log4j2
@CrossOrigin("*")
@RequiredArgsConstructor
public abstract class BaseCrudController<Input extends ModelInput<Model>, Model extends Entity> {


    protected final EntityRepository<Model> repository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public Model create(Input input) {

        try {

            log.debug(String.format("Validating %s Input", getInputClass().getSimpleName()));

            input.validateCreate();

            log.debug(String.format("Converting %s Input to model", getInputClass().getSimpleName()));

            Model model = input.toModel();

            preCreate(input);

            log.debug(String.format("Saving %s into the DB", getModelClass().getSimpleName()));

            Model result = repository.save(model);

            postCreate(input, result);

            return result;

        } catch (RuntimeException ex) {
            throw ex;
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new RuntimeException(ex.getMessage());
        }
    }

    public Model edit(String id, Object rawInput) {

        Input input = objectMapper.convertValue(rawInput, getInputClass());

        return edit(id, input);
    }

    public Model edit(String id, Input input) {

        try {

            log.debug(String.format("Editing %s model by ID: %s", getModelClass().getSimpleName(), id));
            log.debug(String.format("Validating %s Input", getInputClass().getSimpleName()));

            log.debug(String.format("Finding old %s model in DB", getModelClass().getSimpleName()));

            Model oldModel = repository.findById(id).orElseThrow(() -> new RuntimeException(getModelClass().getSimpleName() + "was not found"));

            input.validateEdit(oldModel);

            log.debug(String.format("Converting %s Input to model", getModelClass().getSimpleName()));

            Model newModel = input.toModel(oldModel);

            preEdit(input, oldModel);

            log.debug(String.format("Saving %s into the DB", getModelClass().getSimpleName()));

            Model result = repository.save(newModel);

            postEdit(input, oldModel, result);

            return result;

        } catch (RuntimeException ex) {
            throw ex;
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new RuntimeException(ex.getMessage());
        }

    }

    public Model delete(String id) {

        try {

            log.debug(String.format("Finding %s model to be deleted in DB by ID: %s", getModelClass().getSimpleName(), id));

            Model model = repository.findById(id).orElseThrow(() -> new RuntimeException(getModelClass().getSimpleName() + "was not found"));

            preDelete(model);

            log.debug(String.format("Deleting %s from the db", getModelClass().getSimpleName()));

            repository.delete(model);

            postDelete(model);

            return model;

        } catch (RuntimeException ex) {
            throw ex;
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new RuntimeException(ex.getMessage());
        }

    }

    public Model findOne(String id) {

        try {

            log.debug(String.format("Searching for one %s model in DB by ID: %s", getModelClass().getSimpleName(), id));

            Model model = repository.findById(id).orElseThrow(() -> new RuntimeException(getModelClass().getSimpleName() + "was not found"));

            return model;

        } catch (RuntimeException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }

    }

    protected String getSingleName() {
        return getModelClass().getSimpleName();
    }

    protected String getPluralName() {
        return getSingleName() + "s";
    }

    protected abstract Class<Input> getInputClass();

    protected abstract Class<Model> getModelClass();

    protected void preDelete(Model model) {
        //may be overridden in children
    }

    protected void postDelete(Model model) {
        //may be overridden in children
    }

    protected void preEdit(Input input, Model oldModel) {
        //may be overridden in children
    }

    protected void postEdit(Input input, Model oldModel, Model result) {
        //may be overridden in children
    }

    protected void preCreate(Input input) {
        //may be overridden in children
    }

    protected void postCreate(Input input, Model result) {
        //may be overridden in children
    }

    public List<String> getRequiredRoles() {
        return new ArrayList<>();
    }

    protected List<Method> getExcludedMethods() {
        // may be overridden in children
        return new ArrayList<>();
    }
}
