package com.issue.manager.utils;

import com.issue.manager.models.base.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class GitHubService {

    @Value("${github.api.url}")
    private String githubApiUrl;
    private final WebClient.Builder webClientBuilder;

    public void validatePublicRepository(String repoName) {
        String repoStrippedName = getRepositoryName(repoName);
        String ownerName = getOwnerName(repoName);
        String repoUrlFull = githubApiUrl + "/" + ownerName + "/" + repoStrippedName;
        WebClient webClient = webClientBuilder.build();
        try {
            Map<String, Object> repo = webClient.get()
                    .uri(repoUrlFull)
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                    .block();
            if (repo == null || repo.get("private") == null || Boolean.TRUE.equals(repo.get("private"))) {
                throw new IllegalArgumentException("The repository is not public.");
            }
        } catch (WebClientResponseException.NotFound e) {
            throw new IllegalArgumentException("The repository does not exist or not public");
        }
    }

    private String getOwnerName(String repoName) {
        String signature = "github.com/";
        return repoName.substring(repoName.indexOf(signature) + signature.length(), repoName.lastIndexOf("/"));
    }

    private String getRepositoryName(String repoName) {
        return repoName.substring(repoName.lastIndexOf("/") + 1, repoName.lastIndexOf("."));
    }

    public Map<String, Object> getPublicRepositoryInfo(String repoName) {
        String repoStrippedName = getRepositoryName(repoName);
        String ownerName = getOwnerName(repoName);
        String repoUrlFull = githubApiUrl + "/" + ownerName + "/" + repoStrippedName;
        WebClient webClient = webClientBuilder.build();
        Map<String, Object> repo = null;
        try {
                repo = webClient.get()
                        .uri(repoUrlFull)
                        .retrieve()
                        .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                        .block();

            if (repo == null || repo.get("private") == null || Boolean.TRUE.equals(repo.get("private"))) {
                throw new IllegalArgumentException("The repository is not public.");
            }
        } catch (WebClientResponseException.NotFound e) {
            throw new IllegalArgumentException("The repository does not exist or not public");
        }

        return repo;
    }

    public List<Map<String, Object>> getAllRepositoryCommits(String urlToCommits) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String accessToken = user.getAccessToken();

        WebClient webClient = webClientBuilder.build();
        List<Map<String, Object>> allCommits = new ArrayList<>();
        int page = 1;
        int perPage = 50;

        try {
            while (true) {
                String paginatedUrl = urlToCommits + "?page=" + page + "&per_page=" + perPage;
                List<Map<String, Object>> commitsPage;

                if (accessToken != null && !accessToken.isEmpty()) {
                    commitsPage = webClient.get()
                            .uri(paginatedUrl)
                            .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                            .retrieve()
                            .bodyToMono(new ParameterizedTypeReference<List<Map<String, Object>>>() {})
                            .block();
                } else {
                    commitsPage = webClient.get()
                            .uri(paginatedUrl)
                            .retrieve()
                            .bodyToMono(new ParameterizedTypeReference<List<Map<String, Object>>>() {})
                            .block();
                }

                if (commitsPage == null || commitsPage.isEmpty()) {
                    break;  // No more commits to retrieve
                }

                allCommits.addAll(commitsPage);
                page++;
            }

        } catch (WebClientResponseException.NotFound e) {
            throw new IllegalArgumentException("The repository does not exist or is not public");
        }

        return allCommits;
    }

    public Map<String, Object> getSingleCommitInfo(String urlToCommit) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String accessToken = user.getAccessToken();

        WebClient webClient = webClientBuilder.build();
        Map<String, Object> commitInfo = null;
        try {
            if (accessToken != null && !accessToken.isEmpty()) {
                commitInfo = webClient.get()
                            .uri(urlToCommit)
                            .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                            .retrieve()
                            .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                            .block();
            } else {
                commitInfo = webClient.get()
                            .uri(urlToCommit)
                            .retrieve()
                            .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                            .block();
            }
        } catch (WebClientResponseException.NotFound e) {
            throw new IllegalArgumentException("The repository does not exist or is not public");
        }

        return commitInfo;
    }

}
