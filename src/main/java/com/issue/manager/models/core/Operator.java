package com.issue.manager.models.core;

public enum Operator {
    eq, // EQUALS                       | String, Date, Boolean
    neq, // NOT EQUALS                  | String, Date, Boolean
    contains, // CONTAINS               | String
    doesnotcontain, // DOES NOT CONTAIN | String
    startswith, // STARTS WITH          | String
    endswith, // ENDS WITH              | String
    gte, // IS AFTER OR EQUAL TO        | Date
    gt, // IS AFTER                     | Date
    lte, // IS BEFORE OR EQUAL TO       | Date
    lt, // IS BEFORE                    | Date
    includes, // INCLUDES               | Array
    between, // BETWEEN TWO DATES       | Date
}