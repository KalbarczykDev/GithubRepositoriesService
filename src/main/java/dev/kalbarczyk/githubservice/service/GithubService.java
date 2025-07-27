package dev.kalbarczyk.githubservice.service;


import dev.kalbarczyk.githubservice.exception.GithubException;
import dev.kalbarczyk.githubservice.model.dto.BranchDto;
import dev.kalbarczyk.githubservice.model.dto.RepositoryDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.List;
import java.util.stream.Stream;

@Service
public class GithubService {

    private final RestClient restClient;

    public GithubService(RestClient.Builder builder,
                         @Value("${github.api.url:https://api.github.com/}") String baseUrl) {
        this.restClient = builder.baseUrl(baseUrl).build();
    }

    public List<RepositoryDto> getRepositories(String username) {


        GitHubRepository[] repositories;
        try {
            repositories = restClient.get()
                    .uri("users/{username}/repos", username)
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError, (req, res) -> {
                        if (res.getStatusCode().value() == 404) {
                            throw new GithubException.UserNotFound(username);
                        } else if (res.getStatusCode().value() != 403) {
                            throw new GithubException.RateLimitExceeded();
                        }
                    }).body(GitHubRepository[].class);
        } catch (RestClientException ex) {
            throw new RuntimeException("Unexpected error occurred while fetching repositories", ex);
        }


        if (repositories == null) {
            throw new GithubException.MalformedData();
        }

        return Stream.of(repositories)
                .filter(repo -> !repo.fork())
                .map(repo -> new RepositoryDto(
                        repo.name(),
                        repo.owner().login(),
                        getBranches(repo.owner().login(), repo.name())
                ))
                .toList();
    }

    private List<BranchDto> getBranches(String owner, String repoName) {

        GitHubBranch[] branches;
        try {
            branches = restClient.get().
                    uri("repos/{owner}/{repo}/branches", owner, repoName)
                    .retrieve().body(GitHubBranch[].class);
        } catch (RestClientException ex) {
            throw new RuntimeException("Failed to fetch branches from GitHub", ex);
        }


        if (branches == null) {
            throw new GithubException.MalformedData();
        }
        return Stream.of(branches)
                .map(branch -> new BranchDto(branch.name(), branch.commit().sha()))
                .toList();
    }

    private record GitHubRepository(String name, boolean fork, GitHubOwner owner) {
    }

    private record GitHubOwner(String login) {
    }

    private record GitHubCommit(String sha) {
    }

    private record GitHubBranch(String name, GitHubCommit commit) {
    }

}
