package com.issue.manager.services.project;

import com.issue.manager.inputs.dtos.ProgrammerStatisticsCommitsPerProjectResponse;
import com.issue.manager.inputs.dtos.ProgrammerStatisticsRequest;
import com.issue.manager.inputs.dtos.ProgrammerStatisticsResponse;
import com.issue.manager.models.base.User;
import com.issue.manager.models.project.Project;
import com.issue.manager.repositories.base.UserRepository;
import com.issue.manager.repositories.project.ProjectRepository;
import com.issue.manager.utils.GitHubService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class StatisticsService {

    private final GitHubService gitHubService;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    private static final String KEY_TO_COMMITS = "commits_url";
    private static final String KEY_TO_AUTHOR = "author";
    private static final String KEY_TO_LOGIN = "login";

    public ProgrammerStatisticsResponse getProgrammerStatistics(String id, ProgrammerStatisticsRequest programmerStatisticsRequest) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project was not found by id " + id));

        Map<String, Object> publicRepositoryInfo = gitHubService.getPublicRepositoryInfo(project.getUrl());

        ProgrammerStatisticsResponse programmerStatisticsResponse = new ProgrammerStatisticsResponse();
        programmerStatisticsResponse.setFrom(programmerStatisticsRequest.getFrom());
        programmerStatisticsResponse.setUntil(programmerStatisticsRequest.getUntil());
        programmerStatisticsResponse.setIds(programmerStatisticsRequest.getIds());
        programmerStatisticsResponse.setType(programmerStatisticsRequest.getType());

        switch(programmerStatisticsRequest.getType()) {
            case COMMITS_PER_PROJECT -> {
                Object keyToCommits = publicRepositoryInfo.get(KEY_TO_COMMITS);
                if (keyToCommits != null) {
                    List<Map<String, Object>> publicRepositoryCommits = gitHubService.getPublicRepositoryCommits(getCommitsUrl((String) keyToCommits));
                    if (publicRepositoryCommits != null) {
                        var statisticsInfos = new ArrayList<>();
                        for (String userId : programmerStatisticsRequest.getIds()) {
                            Optional<User> optionalUser = userRepository.findById(userId);

                            if (optionalUser.isEmpty()) {
                                continue;
                            }

                            User user = optionalUser.get();
                            var commitsPerProjectResponse = new ProgrammerStatisticsCommitsPerProjectResponse();
                            commitsPerProjectResponse.setId(userId);
                            commitsPerProjectResponse.setName(user.getUsernameUserName());
                            int numberOfCommits = 0;
                            commitsPerProjectResponse.setNumberOfCommits(numberOfCommits);

                            for (Map<String, Object> commitInfo : publicRepositoryCommits) {
                                var authorInfo = (Map<String, Object>) commitInfo.getOrDefault(KEY_TO_AUTHOR, new HashMap<>());

                                if (authorInfo != null && !authorInfo.isEmpty()) {
                                    String commitAuthor = (String) authorInfo.get(KEY_TO_LOGIN);

                                    if (user.getGitUserNames().contains(commitAuthor)) {
                                        numberOfCommits += 1;
                                    }
                                }
                            }

                            commitsPerProjectResponse.setNumberOfCommits(numberOfCommits);
                            statisticsInfos.add(commitsPerProjectResponse);
                        }

                        programmerStatisticsResponse.setStatisticsInfos(statisticsInfos);
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
