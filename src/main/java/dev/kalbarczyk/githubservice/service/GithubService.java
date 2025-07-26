package dev.kalbarczyk.githubservice.service;

import dev.kalbarczyk.githubservice.exception.GithubUserNotFoundException;
import dev.kalbarczyk.githubservice.model.GitHubRepository;
import dev.kalbarczyk.githubservice.model.dto.RepositoryDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class GithubService {
    private static final String GITHUB_URL = "https://api.github.com/";

    private final RestTemplate restTemplate = new RestTemplate();


    public List<RepositoryDto> getRepositories(String username){


        GitHubRepository[] repositories;
        try{

        }catch (HttpClientErrorException e){
            if(e.getStatusCode() == HttpStatus.NOT_FOUND){
                throw new GithubUserNotFoundException(username);
            }
        }

        return null;
    }

}
