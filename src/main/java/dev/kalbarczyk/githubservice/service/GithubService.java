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

    public GithubService(final RestClient.Builder builder,
                         final @Value("${github.api.url:https://api.github.com/}") String baseUrl) {
        this.restClient = builder.baseUrl(baseUrl).build();
    }

    public final List<RepositoryDto> getRepositories(final String username) {


        final GitHubRepository[] repositories;
        try {
            repositories = restClient.get()
                    .uri("users/{username}/repos", username)
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError, (req, res) -> {
                        final var statusCode = res.getStatusCode().value();
                        if (statusCode == 404) {
                            throw new GithubException.UserNotFound(username);
                        } else if (statusCode == 403) {
                            throw new GithubException.RateLimitExceeded();
                        }
                    }).body(GitHubRepository[].class);
        } catch (RestClientException ex) {
            throw new GithubException.FetchError(ex.getMessage());
        }


        if (repositories == null) {
            throw new GithubException.MalformedData();
        }

        return Stream.of(repositories)
                .filter(repo -> !repo.fork())
                .map(repo -> {
                    final var name = repo.name();
                    final var owner = repo.owner().login();
                    final var branches = getBranches(owner, name);
                    return new RepositoryDto(name, owner, branches);
                }).toList();

    }

    private List<BranchDto> getBranches(final String owner, final String repoName) {

        final GitHubBranch[] branches;
        try {
            branches = restClient.get().
                    uri("repos/{owner}/{repo}/branches", owner, repoName)
                    .retrieve().body(GitHubBranch[].class);
        } catch (RestClientException ex) {
            throw new GithubException.FetchError(ex.getMessage());
        }


        if (branches == null) {
            throw new GithubException.MalformedData();
        }
        return Stream.of(branches)
                .map(branch -> {
                    final var name = branch.name();
                    final var sha = branch.commit().sha();
                    return new BranchDto(name, sha);
                })
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
