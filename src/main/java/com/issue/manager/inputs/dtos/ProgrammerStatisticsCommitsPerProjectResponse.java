package com.issue.manager.inputs.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProgrammerStatisticsCommitsPerProjectResponse {
    private String id;
    private String name;
    private int numberOfCommits;
}
