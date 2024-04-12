package com.issue.manager.models.core;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class QueryOptions {
    private int skip;
    private int take;
    private List<Filter> filters;
    private Sort sort;
}
