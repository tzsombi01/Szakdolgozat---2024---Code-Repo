package com.issue.manager.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Service
@RequiredArgsConstructor
public class GitHubService {

    @Value("${github.api.url}")
    private String githubApiUrl;

    private final String repositoryType = "public";

    private final WebClient.Builder webClientBuilder;

    public void validatePublicRepository(String repoUrl) {
        WebClient webClient = webClientBuilder.build();
        try {
            Object repo = webClient.get()
                    .uri(repoUrl)
                    .retrieve()
                    .bodyToMono(Object.class)
                    .block();
//            if (!repositoryType.equals(repo.getVisibility())) {
//                throw new IllegalArgumentException("The repository is not public.");
//            }
            System.out.println("a");
        } catch (WebClientResponseException.NotFound e) {
            throw new IllegalArgumentException("The repository does not exist.");
        }
    }
}
