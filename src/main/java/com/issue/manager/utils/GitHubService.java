package com.issue.manager.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

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
}
