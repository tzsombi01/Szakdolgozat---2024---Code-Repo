package com.issue.manager.models.core;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Filter {
    private String field;
    private Operator operator;
    private Object value;
    private String type;
}