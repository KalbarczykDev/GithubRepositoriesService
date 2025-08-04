# GitHubRepositoriesService

REST API that fetches **non-fork** GitHub repositories for a given user and includes the **branches with latest commit
SHA**.


## Prerequisites
* Java 21 or higher (LTS version recommended)
* Maven 3.3.2 or higher

## Usage

### Cloning the Repository

````bash 
git clone https://github.com/KalbarczykDev/GithubRepositoriesService.git
cd GithubRepositoriesService
````

### Building the Project
````bash
./mvnw clean install
````
### Running the Application
````bash
./mvnw spring-boot:run
````
The application will start on http://localhost:8080 by default.

### Running the Tests
````bash
./mvnw test
````

### Endpoint

```
GET /api/github/{username}/repositories
```

### Example response:

```json
[
  {
    "repositoryName": "Hello-World",
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

### Error response

```json
{
  "status": "error",
  "message": "User not found: {username}"
}
```

## License

[MIT](https://choosealicense.com/licenses/mit/)
