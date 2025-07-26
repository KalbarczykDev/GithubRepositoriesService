package dev.kalbarczyk.githubservice.service;

import dev.kalbarczyk.githubservice.exception.GithubUserNotFoundException;
import dev.kalbarczyk.githubservice.model.GitHubBranch;
import dev.kalbarczyk.githubservice.model.GitHubRepository;
import dev.kalbarczyk.githubservice.model.dto.BranchDto;
import dev.kalbarczyk.githubservice.model.dto.RepositoryDto;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class GithubService {
    private static final String GITHUB_URL = "https://api.github.com/";

    private final RestTemplate restTemplate = new RestTemplate();


    public List<RepositoryDto> getRepositories(String username) {


        GitHubRepository[] repositories;
        try {
            repositories = restTemplate.getForObject(GITHUB_URL + username + "/repos", GitHubRepository[].class);
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new GithubUserNotFoundException(username);
            }
            throw e;
        }

        List<RepositoryDto> result = new ArrayList<>();

        if (repositories == null) {
            return result;
        }

        for (GitHubRepository repo : repositories) {
            if (!repo.fork()) {
                List<BranchDto> branches = getBranches(repo.owner().login(), repo.name());
                RepositoryDto dto = new RepositoryDto(repo.name(), repo.owner().login(), branches);
                result.add(dto);
            }
        }


        return result;
    }

    private List<BranchDto> getBranches(String owner, String repoName) {
        String url = GITHUB_URL + "repos/" + owner + "/" + repoName + "/branches";
        GitHubBranch[] branches = restTemplate.getForObject(url, GitHubBranch[].class);
        List<BranchDto> result = new ArrayList<>();

        if (branches == null) {
            return result;
        }
        for (GitHubBranch branch : branches) {
            result.add(new BranchDto(branch.name(), branch.commit().sha()));
        }
        return result;
    }

}
