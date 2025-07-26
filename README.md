# GitHubRepositoriesService

REST API that fetches **non-fork** GitHub repositories for a given user and includes the **branches with latest commit SHA**.

# Endpoint
```
GET /api/github/{username}/repositories
```
# Example response:

```json
[
  {
    "name": "Hello-World",
    "ownerLogin": "octocat",
    "branches": [
      {
        "name": "main",
        "lastCommitSha": "7fd1a60..."
      }
    ]
  }
]
```

# Error response
```json
{
  "status": "error",
  "message": "User not found: {username}"
}
```

# Run 
```bash
./mvnw spring-boot:run
```

# Test 
```bash
./mvnw test
```
