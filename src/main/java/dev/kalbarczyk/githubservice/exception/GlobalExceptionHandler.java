package dev.kalbarczyk.githubservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(GithubException.UserNotFound.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(GithubException.UserNotFound ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("status", HttpStatus.NOT_FOUND.value(),
                "message", ex.getMessage()));
    }

    @ExceptionHandler(GithubException.RateLimitExceeded.class)
    public ResponseEntity<Map<String, Object>> handleRateLimitExceeded(GithubException.RateLimitExceeded ex) {
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(Map.of("status", HttpStatus.TOO_MANY_REQUESTS.value(),
                "message", ex.getMessage()));
    }

    @ExceptionHandler(GithubException.MalformedData.class)
    public ResponseEntity<Map<String, Object>> handleMalformedData(GithubException.MalformedData ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("status", HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "message", ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "status", HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "message", "An unexpected error occurred"
        ));
    }
}
