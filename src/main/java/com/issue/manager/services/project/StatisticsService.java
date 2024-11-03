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

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.*;

@Service
@RequiredArgsConstructor
public class StatisticsService {

    private final GitHubService gitHubService;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    private static final String KEY_TO_COMMITS = "commits_url";
    private static final String KEY_TO_COMMIT_CHANGE_SIZE = "stats";
    private static final String KEY_TO_COMMIT_TOTAL = "total";
    private static final String KEY_TO_COMMIT_FOR_DATE = "commit";
    private static final String KEY_TO_DATE = "date";
    private static final String KEY_TO_AUTHOR = "author";
    private static final String KEY_TO_LOGIN = "login";
    private static final String KEY_TO_COMMIT_URL = "url";
    private final long DAY_IN_MILLISECONDS = 1000 * 60 * 60 * 24;

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
                String keyToCommits = (String) publicRepositoryInfo.get(KEY_TO_COMMITS);
                if (keyToCommits != null) {
                    List<Map<String, Object>> publicRepositoryCommits = gitHubService.getAllRepositoryCommits(getCommitsUrl(keyToCommits));
                    if (publicRepositoryCommits != null) {
                        var statisticsInfos = new ArrayList<>();

                        List<User> users = userRepository.findAllById(programmerStatisticsRequest.getIds());

                        for (User user : users) {
                            var commitsPerProjectResponse = new ProgrammerStatisticsCommitsPerProjectResponse();
                            commitsPerProjectResponse.setId(user.getId());
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

                        List<User> users = userRepository.findAllById(programmerStatisticsRequest.getIds());

                        Map<String, Object> singleCommitInfo = null;

                        for (User user : users) {
                            var averageCommitSizeResponse = new ProgrammerStatisticsAverageCommitSizeResponse();
                            averageCommitSizeResponse.setId(user.getId());
                            averageCommitSizeResponse.setName(user.getUsernameUserName());
                            double averageCommitSize = 0.0;
                            averageCommitSizeResponse.setAverageSize(averageCommitSize);

                            int numberOfCommits = 0;
                            for (Map<String, Object> commitInfo : publicRepositoryCommits) {
                                var authorInfo = getAuthorInfo(commitInfo);

                                if (authorInfo != null && !authorInfo.isEmpty()) {
                                    String commitAuthor = (String) authorInfo.get(KEY_TO_LOGIN);

                                    if (user.getGitUserNames().contains(commitAuthor)) {
                                        String commitUrl = (String) commitInfo.get(KEY_TO_COMMIT_URL);

                                        if (singleCommitInfo == null) {
                                            singleCommitInfo = gitHubService.getSingleCommitInfo(commitUrl);
                                        }

                                        int total = (int) getStatistics(singleCommitInfo).getOrDefault(KEY_TO_COMMIT_TOTAL, 0);
                                        averageCommitSize += total;
                                        numberOfCommits += 1;
                                    }
                                }
                            }

                            averageCommitSizeResponse.setAverageSize(
                                    numberOfCommits != 0
                                            ? BigDecimal.valueOf(averageCommitSize)
                                                .divide(BigDecimal.valueOf(numberOfCommits), 2, RoundingMode.HALF_UP)
                                                .doubleValue()
                                            : 0);
                            statisticsInfos.add(averageCommitSizeResponse);
                        }
                        programmerStatisticsResponse.setStatisticsInfos(statisticsInfos);
                    }
                }
            }
            case DAILY_COMMITS_FOR_YEAR -> {
                Long fromTimestamp = programmerStatisticsRequest.getFrom();
                Instant from = Instant.ofEpochMilli(fromTimestamp);

                Object keyToCommits = publicRepositoryInfo.get(KEY_TO_COMMITS);
                if (keyToCommits != null) {
                    List<Map<String, Object>> publicRepositoryCommits = gitHubService.getAllRepositoryCommits(getCommitsUrl((String) keyToCommits));

                    if (publicRepositoryCommits != null) {
                        int startWeekday = from.atZone(ZoneId.systemDefault()).getDayOfWeek().getValue() % 7;
                        List<Map<String, Object>> structuredData = new ArrayList<>();

                        Optional<User> optionalUser = Optional.empty();
                        if (!programmerStatisticsRequest.getIds().isEmpty()) {
                            optionalUser = userRepository.findById(programmerStatisticsRequest.getIds().get(0));

                            if (optionalUser.isEmpty()) {
                                programmerStatisticsResponse.setStatisticsInfos(List.of());
                                return programmerStatisticsResponse;
                            }
                        }

                        User user = optionalUser.get();
                        Map<Instant, Integer> dailyCommitCounts = new HashMap<>();

                        List<Map<String, Object>> publicRepositoryCommitsFromBaseTime = publicRepositoryCommits.stream()
                                .filter(commitInfo -> {
                                    Map<String, Object> authorInfo = getAuthorInfo(commitInfo);
                                    if (authorInfo != null && !authorInfo.isEmpty()) {
                                        Instant commitInstant = getInstant(commitInfo);

                                        return commitInstant.isAfter(from);
                                    }
                                    return false;
                                })
                                .toList();

                        for (Map<String, Object> commitInfo : publicRepositoryCommitsFromBaseTime) {
                            Map<String, Object> authorInfo = getAuthorInfo(commitInfo);
                            if (authorInfo != null && !authorInfo.isEmpty()) {
                                String commitAuthor = (String) authorInfo.get(KEY_TO_LOGIN);
                                Instant commitInstant = getInstant(commitInfo);
                                Instant dayPrecisionInstant = commitInstant.atZone(ZoneOffset.UTC).toLocalDate().atStartOfDay(ZoneOffset.UTC).toInstant();

                                if (user.getGitUserNames().contains(commitAuthor)) {
                                    dailyCommitCounts.merge(dayPrecisionInstant, 1, Integer::sum);
                                }
                            }
                        }

                        for (int dayOffset = 0; dayOffset < 365; dayOffset++) {
                            Instant currentDate = from.plusSeconds(dayOffset * DAY_IN_MILLISECONDS).atZone(ZoneOffset.UTC).toLocalDate().atStartOfDay(ZoneOffset.UTC).toInstant();
                            int dayOfWeek = currentDate.atZone(ZoneId.systemDefault()).getDayOfWeek().getValue() % 7;
                            int weekOfYear = (startWeekday + dayOffset) / 7;
                            Integer commitCount = dailyCommitCounts.getOrDefault(currentDate, 0);

                            structuredData.add(Map.of(
                                    "x", dayOfWeek,
                                    "y", 52 - weekOfYear,
                                    "value", commitCount,
                                    "date", currentDate.toEpochMilli()
                            ));
                        }

                        // Adding empty tiles at the end to fill the row
                        Instant lastDate = from.plusSeconds(365 * DAY_IN_MILLISECONDS).atZone(ZoneOffset.UTC).toLocalDate().atStartOfDay(ZoneOffset.UTC).toInstant();
                        int lastWeekday = lastDate.atZone(ZoneId.systemDefault()).getDayOfWeek().getValue() % 7;
                        int remainingDays = 6 - lastWeekday;
                        for (int i = 0; i < remainingDays; i++) {
                            structuredData.add(Map.of(
                                    "x", (lastWeekday + i + 1) % 7,
                                    "y", 0,
                                    "value", 0,
                                    "date", 0
                            ));
                        }

                        programmerStatisticsResponse.setStatisticsInfos(Collections.singletonList(structuredData));
                    }
                }
            }

            default -> {
                throw new RuntimeException("Not supported statistic type!");
            }
        }

        return programmerStatisticsResponse;
    }

    private static Instant getInstant(Map<String, Object> commitInfo) {
        Map<String, Object> commitInfoForAuthorAndDate = (Map<String, Object>) commitInfo.get(KEY_TO_COMMIT_FOR_DATE);
        Map<String, Object> authorAndDateInfo = getAuthorInfo(commitInfoForAuthorAndDate);
        String commitTimeStamp = (String) authorAndDateInfo.get(KEY_TO_DATE);
        return Instant.parse(commitTimeStamp);
    }

    private static Map<String, Object> getAuthorInfo(Map<String, Object> commitInfo) {
        return (Map<String, Object>) commitInfo.getOrDefault(KEY_TO_AUTHOR, new HashMap<>());
    }

    private static Map<String, Object> getStatistics(Map<String, Object> singleCommitInfo) {
        return (Map<String, Object>) singleCommitInfo.getOrDefault(KEY_TO_COMMIT_CHANGE_SIZE, new HashMap<>());
    }

    private String getCommitsUrl(String commitsUrl) {
        return commitsUrl.substring(0, commitsUrl.indexOf("{"));
    }
}
