package com.issue.manager.services.project;

import com.issue.manager.inputs.dtos.ProgrammerStatisticsAverageCommitSizeResponse;
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
    private static final String KEY_TO_COMMIT_CHANGE_SIZE = "commits_url";
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
                    List<Map<String, Object>> publicRepositoryCommits = gitHubService.getAllRepositoryCommits(getCommitsUrl((String) keyToCommits));
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
            case AVERAGE_COMMIT_SIZE -> {
                Object keyToCommits = publicRepositoryInfo.get(KEY_TO_COMMITS);
                if (keyToCommits != null) {
                    List<Map<String, Object>> publicRepositoryCommits = gitHubService.getAllRepositoryCommits(getCommitsUrl((String) keyToCommits));
                    if (publicRepositoryCommits != null) {
                        var statisticsInfos = new ArrayList<>();
                        for (String userId : programmerStatisticsRequest.getIds()) {
                            Optional<User> optionalUser = userRepository.findById(userId);

                            if (optionalUser.isEmpty()) {
                                continue;
                            }

                            User user = optionalUser.get();
                            var averageCommitSizeResponse = new ProgrammerStatisticsAverageCommitSizeResponse();
                            averageCommitSizeResponse.setId(userId);
                            averageCommitSizeResponse.setName(user.getUsernameUserName());
                            double averageCommitSize = 0.0;
                            int numberOfCommits = 0;
                            averageCommitSizeResponse.setAverageSize(averageCommitSize);

                            for (Map<String, Object> commitInfo : publicRepositoryCommits) {
                                var authorInfo = (Map<String, Object>) commitInfo.getOrDefault(KEY_TO_AUTHOR, new HashMap<>());
                                var commitSizeInfo = (Map<String, Object>) commitInfo.getOrDefault(KEY_TO_COMMIT_CHANGE_SIZE, new HashMap<>());

                                if (authorInfo != null && !authorInfo.isEmpty()) {
                                    String commitAuthor = (String) authorInfo.get(KEY_TO_LOGIN);

                                    if (user.getGitUserNames().contains(commitAuthor)) {
                                        numberOfCommits += 1;
                                        // KEY_TO_COMMIT_CHANGE_SIZE
                                        averageCommitSize = 0;
                                    }
                                }
                            }

                            averageCommitSizeResponse.setAverageSize(numberOfCommits != 0 ? averageCommitSize / numberOfCommits : 0);
                            statisticsInfos.add(averageCommitSizeResponse);
                        }

                        programmerStatisticsResponse.setStatisticsInfos(statisticsInfos);
                    }
                }
            }
            case DAILY_COMMITS_FOR_YEAR -> {
                Long fromTimestamp = programmerStatisticsRequest.getFrom();
                Date from = new Date(fromTimestamp); // Convert Long to Date for processing
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(from);
                calendar.add(Calendar.YEAR, 1);
                Date until = calendar.getTime();

                // IMPORTANT -> 'commit' -> 'author' -> 'date'
                // https://api.github.com/repos/tzsombi01/Szakdolgozat-2024-frontend/commits/158d2d90176d621eef213374e969ddfc89be7baa

                // Fetch all commits between 'from' and 'until'
                Object keyToCommits = publicRepositoryInfo.get(KEY_TO_COMMITS);
                if (keyToCommits != null) {
                    List<Map<String, Object>> publicRepositoryCommits = gitHubService.getAllRepositoryCommits(getCommitsUrl((String) keyToCommits));

                    if (publicRepositoryCommits != null) {
                        int startWeekday = calendar.get(Calendar.DAY_OF_WEEK) - 1;
                        List<Map<String, Object>> structuredData = new ArrayList<>();

                        // Adding empty tiles before the first day of the year
                        for (int i = 0; i < startWeekday; i++) {
                            structuredData.add(Map.of(
                                    "x", i,
                                    "y", 52,
                                    "value", null,
                                    "date", null
                            ));
                        }

                        // Initialize the commit data structure
                        Map<Date, Integer> dailyCommitCounts = new HashMap<>();

                        // Populate dailyCommitCounts with the commits data
                        for (Map<String, Object> commitInfo : publicRepositoryCommits) {
                            Map<String, Object> authorInfo = (Map<String, Object>) commitInfo.getOrDefault(KEY_TO_AUTHOR, new HashMap<>());
                            if (authorInfo != null && !authorInfo.isEmpty()) {
                                String commitAuthor = (String) authorInfo.get(KEY_TO_LOGIN);
                                Long commitTimestamp = (Long) commitInfo.get("commit_date");
                                Date commitDate = new Date(commitTimestamp); // Convert to Date and ensure day precision

                                for (String userId : programmerStatisticsRequest.getIds()) {
                                    Optional<User> optionalUser = userRepository.findById(userId);
                                    if (optionalUser.isEmpty()) continue;
                                    User user = optionalUser.get();

                                    if (user.getGitUserNames().contains(commitAuthor)) {
//                                        commitDate = stripTime(commitDate);
                                        dailyCommitCounts.merge(commitDate, 1, Integer::sum);
                                    }
                                }
                            }
                        }

                        // Generate commit data for each day over the last year
                        for (int dayOffset = 0; dayOffset < 365; dayOffset++) {
                            Date currentDate = new Date(from.getTime() + dayOffset * (1000 * 60 * 60 * 24)); // Add days
                            int dayOfWeek = currentDate.getDay();
                            int weekOfYear = (startWeekday + dayOffset) / 7;
                            Integer commitCount = dailyCommitCounts.getOrDefault(currentDate, 0);

                            structuredData.add(Map.of(
                                    "x", dayOfWeek,
                                    "y", 52 - weekOfYear,
                                    "value", commitCount,
                                    "date", currentDate.getTime()
                            ));
                        }

                        // Adding empty tiles at the end to fill the row
                        Calendar lastCalendar = Calendar.getInstance();
                        lastCalendar.setTime(from);
                        lastCalendar.add(Calendar.DAY_OF_YEAR, 365);
                        int lastWeekday = lastCalendar.get(Calendar.DAY_OF_WEEK) - 1;
                        int remainingDays = 6 - lastWeekday;
                        for (int i = 0; i < remainingDays; i++) {
                            structuredData.add(Map.of(
                                    "x", (lastWeekday + i + 1) % 7,
                                    "y", 0,
                                    "value", null,
                                    "date", null
                            ));
                        }

//                        programmerStatisticsResponse.setStatisticsInfos(structuredData);
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
