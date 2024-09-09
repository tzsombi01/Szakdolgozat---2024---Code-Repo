package com.issue.manager.services.project;

import com.issue.manager.inputs.dtos.ProgrammerStatisticsRequest;
import com.issue.manager.inputs.dtos.ProgrammerStatisticsResponse;
import com.issue.manager.models.project.Project;
import com.issue.manager.repositories.project.ProjectRepository;
import com.issue.manager.utils.GitHubService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StatisticsService {

    private final GitHubService gitHubService;
    private final ProjectRepository projectRepository;

    private static final String KEY_TO_COMMITS = "commits_url";

    public ProgrammerStatisticsResponse getProgrammerStatistics(String id, ProgrammerStatisticsRequest programmerStatisticsRequest) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project was not found by id " + id));

        Map<String, Object> publicRepositoryInfo = gitHubService.getPublicRepositoryInfo(project.getUrl());

        ProgrammerStatisticsResponse programmerStatisticsResponse = new ProgrammerStatisticsResponse();

        switch(programmerStatisticsRequest.getType()) {
            case COMMITS_PER_PROJECT -> {
                Object keyToCommits = publicRepositoryInfo.get(KEY_TO_COMMITS);
                if (keyToCommits != null) {
                    List<Map<String, Object>> publicRepositoryCommits = gitHubService.getPublicRepositoryCommits(getCommitsUrl((String) keyToCommits));
                    if (publicRepositoryCommits != null) {

                    }
                }


            }
            default -> {
//                throw new RuntimeException("Not supported statistic type!");
            }
        }

        return programmerStatisticsResponse;
    }

    private String getCommitsUrl(String commitsUrl) {
        return commitsUrl.substring(0, commitsUrl.indexOf("{"));
    }
}
