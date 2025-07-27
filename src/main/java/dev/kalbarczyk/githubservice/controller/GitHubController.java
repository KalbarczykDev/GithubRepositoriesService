package dev.kalbarczyk.githubservice.controller;

import dev.kalbarczyk.githubservice.model.dto.RepositoryDto;
import dev.kalbarczyk.githubservice.service.GithubService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/github")
public class GitHubController {
    private final GithubService githubService;

    public GitHubController(final GithubService githubService) {
        this.githubService = githubService;
    }


    @GetMapping("/{username}/repositories")
    public final List<RepositoryDto> getRepositories(final @PathVariable String username) {
        return githubService.getRepositories(username);
    }
}
