package com.issue.manager.services.base;

import com.issue.manager.models.core.Filter;
import org.springframework.data.mongodb.core.query.Criteria;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class Operation {
    public static Criteria operation(Filter filter, Criteria criteria) {

        Object value = null;

        if (!filter.getType().equals("array")) {
            value = getValue(getClass(filter.getType()), filter.getValue());
        } else {
            value = filter.getValue();
        }

        return switch (filter.getOperator()) {
            case eq -> criteria.and(filter.getField()).is(value);
            case neq -> criteria.and(filter.getField()).ne(value);
            case contains -> criteria.and(filter.getField()).regex(value.toString(), "i");
            case doesnotcontain -> criteria.and(filter.getField()).not().regex(value.toString(), "i");
            case startswith -> criteria.and(filter.getField()).regex(String.format("^%s", value), "i");
            case endswith -> criteria.and(filter.getField()).regex(String.format("%s$", value), "i");
            case gte -> criteria.and(filter.getField()).gte(value);
            case gt -> criteria.and(filter.getField()).gt(value);
            case lte -> criteria.and(filter.getField()).lte(value);
            case lt -> criteria.and(filter.getField()).lt(value);
            case includes -> criteria.and(filter.getField()).in((List<?>) value);
            case between -> criteria.and(filter.getField()).gte(((List<?>) value).get(0) != null ? Long.parseLong(((List<String>) value).get(0)) : System.currentTimeMillis()).lte(((List<?>) value).get(1) != null ? Long.parseLong(((List<String>) value).get(1)) : System.currentTimeMillis());
        };

    }

    public static Object getValue(Class<?> type, Object value) {
        Object result = null;

        try {
            result = type.getMethod("valueOf", getMethodParamClass(type)).invoke(null, value);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
                 | SecurityException e) {
            e.printStackTrace();
            throw new IllegalStateException(String.format("Parsing filter value: '%s' failed!", value));
        }

        return result;
    }

    public static Class<?> getClass(String type) {
        return switch (type) {
            case "string" -> String.class;
            case "number", "date" -> Long.class;
            case "boolean" -> Boolean.class;
            default -> throw new IllegalArgumentException(String.format("%s type is unsupported", type));
        };
    }

    public static Class<?> getMethodParamClass(Class<?> type) {
        return switch (type.getSimpleName()) {
            case "String" -> Object.class;
            case "Long", "Boolean" -> String.class;
            default ->
                    throw new IllegalArgumentException(String.format("%s type is unsupported", type.getSimpleName()));
        };
    }
}
