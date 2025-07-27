package dev.kalbarczyk.githubservice.exception;

public sealed class GithubException extends RuntimeException
        permits GithubException.FetchError, GithubException.MalformedData, GithubException.RateLimitExceeded, GithubException.UserNotFound {

    public GithubException(final String message) {
        super(message);
    }


    public static final class UserNotFound extends GithubException {
        public UserNotFound(final String username) {
            super("User not found: " + username);
        }
    }

    public static final class RateLimitExceeded extends GithubException {
        public RateLimitExceeded() {
            super("GitHub API rate limit exceeded");
        }
    }

    public static final class MalformedData extends GithubException {
        public MalformedData() {
            super("GitHub API returned malformed data");
        }
    }

    public static final class FetchError extends GithubException {
        public FetchError(final String message) {
            super("Error fetching data from GitHub: " + message);
        }
    }
}