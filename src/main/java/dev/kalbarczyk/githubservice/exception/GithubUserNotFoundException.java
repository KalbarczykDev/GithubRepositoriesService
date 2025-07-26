package dev.kalbarczyk.githubservice.exception;

public class GithubUserNotFoundException extends  RuntimeException{
    public GithubUserNotFoundException(String message) {
        super(message);
    }
}
