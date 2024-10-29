package com.issue.manager.inputs.dtos;

import com.issue.manager.models.project.StatisticsType;
import lombok.*;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProgrammerStatisticsRequest {
    private List<String> ids; // User references
    private StatisticsType type;
    private Long from;
    private Long until;
}
