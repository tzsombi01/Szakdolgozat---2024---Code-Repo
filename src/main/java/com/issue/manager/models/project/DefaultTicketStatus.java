package com.issue.manager.models.project;

public enum DefaultTicketStatus {
    WORK_IN_PROGRESS("Work in progress"),
    BLOCKED("Blocked"),
    TEST_READY("Test ready"),
    CLOSED("Closed");

    private String name;

    DefaultTicketStatus(String name) {
        this.name = name;
    }
}
