package dev.kalbarczyk.githubservice.model;



public record GitHubRepository(String name, boolean fork, GitHubOwner owner) {}
