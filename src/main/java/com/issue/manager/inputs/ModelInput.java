package com.issue.manager.inputs;

import com.issue.manager.models.Entity;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

import java.lang.reflect.ParameterizedType;

@Getter
@Setter
public abstract class ModelInput<Model extends Entity> {

    public void validateCreate() {

    }

    public void validateEdit(Model current) {

    }

    public Model toModel() {
        Model model = createInstance();

        return toModel(model);
    }

    public Model toModel(Model model) {
        return model;
    }

    @SneakyThrows
    private Model createInstance() {
        Class<Model> modelClass = (Class<Model>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0];

        return modelClass.getDeclaredConstructor().newInstance();
    }
}