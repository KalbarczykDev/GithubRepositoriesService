package dev.kalbarczyk.githubservice.exception;

public class RateLimitExceededException extends RuntimeException {
    public RateLimitExceededException() {
        super("GitHub API rate limit exceeded. Please try again later.");
    }
}
