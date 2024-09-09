package com.issue.manager.controllers.project;

import com.issue.manager.inputs.dtos.ProgrammerStatisticsRequest;
import com.issue.manager.inputs.dtos.ProgrammerStatisticsResponse;
import com.issue.manager.services.project.StatisticsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RestController
@RequestMapping("/api/statistics")
@RequiredArgsConstructor
public class StatisticsController {

    private final StatisticsService statisticsService;

    @PostMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ProgrammerStatisticsResponse getProgrammerStatistics(@PathVariable String id, @RequestBody ProgrammerStatisticsRequest programmerStatisticsRequest) {
        return statisticsService.getProgrammerStatistics(id, programmerStatisticsRequest);
    }
}
