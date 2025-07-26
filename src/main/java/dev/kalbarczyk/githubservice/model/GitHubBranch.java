package dev.kalbarczyk.githubservice.model;

import dev.kalbarczyk.githubservice.service.GithubService;

public record GitHubBranch(String name, GitHubCommit commit) {
}
