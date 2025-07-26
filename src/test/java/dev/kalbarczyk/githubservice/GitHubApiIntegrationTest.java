package dev.kalbarczyk.githubservice;


import dev.kalbarczyk.githubservice.model.dto.RepositoryDto;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class GitHubApiIntegrationTest {

    @LocalServerPort
    private int port;

    private final RestTemplate restTemplate = new RestTemplate();

    @Test
    void shouldReturnNonForkRepositoriesWithBranches() {
        String username = "octocat";
        String url = "http://localhost:" + port + "/api/github/" + username + "/repositories";


        RepositoryDto[] response = restTemplate.getForObject(url, RepositoryDto[].class);

        assertThat(response).isNotNull();
        assertThat(response.length).isGreaterThan(0);
        for (RepositoryDto repo : response) {
            assertThat(repo.name()).isNotBlank();
            assertThat(repo.ownerLogin()).isEqualToIgnoringCase(username);
            assertThat(repo.branches()).allSatisfy(branch -> {
                assertThat(branch.name()).isNotBlank();
                assertThat(branch.lastCommitSha()).isNotBlank();
            });
        }
    }
}