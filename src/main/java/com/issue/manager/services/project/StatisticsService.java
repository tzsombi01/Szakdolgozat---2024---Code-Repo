package com.issue.manager.services.project;

import com.issue.manager.inputs.dtos.ProgrammerStatisticsRequest;
import com.issue.manager.inputs.dtos.ProgrammerStatisticsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StatisticsService {


    public ProgrammerStatisticsResponse getProgrammerStatistics(String id, ProgrammerStatisticsRequest programmerStatisticsRequest) {
        return null;
    }
}
