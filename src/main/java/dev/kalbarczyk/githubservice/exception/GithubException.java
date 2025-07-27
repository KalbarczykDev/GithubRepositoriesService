package dev.kalbarczyk.githubservice.exception;

public sealed class GithubException extends RuntimeException
        permits GithubException.UserNotFound, GithubException.RateLimitExceeded, GithubException.MalformedData {

    public GithubException(String message) {
        super(message);
    }

    public GithubException(String message, Throwable cause) {
        super(message, cause);
    }

    public static final class UserNotFound extends GithubException {
        public UserNotFound(String username) {
            super("User not found: " + username);
        }
    }

    public static final class RateLimitExceeded extends GithubException {
        public RateLimitExceeded() {
            super("GitHub API rate limit exceeded");
        }
    }

    public static final class MalformedData extends GithubException {
        public MalformedData(String message) {
            super(message);
        }
    }
}