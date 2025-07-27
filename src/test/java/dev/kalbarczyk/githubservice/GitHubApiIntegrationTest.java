package dev.kalbarczyk.githubservice;

import dev.kalbarczyk.githubservice.model.dto.RepositoryDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.web.reactive.server.WebTestClient;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {"github.api.url=http://localhost:${wiremock.server.port}/"})
@AutoConfigureWireMock(port = 0)
@AutoConfigureWebTestClient
class GitHubApiIntegrationTest {

    @LocalServerPort
    private int port;


    private WebTestClient webTestClient;


    @BeforeEach
    void setUp() {

        this.webTestClient = WebTestClient.bindToServer().baseUrl("http://localhost:" + port).build();

        stubFor(get(urlEqualTo("/users/octocat/repos"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("""
                                [
                                  {
                                    "name": "Hello-World",
                                    "fork": false,
                                    "owner": { "login": "octocat" }
                                  },
                                  {
                                   "name": "Forked-Repo",
                                   "fork": true,
                                   "owner": { "login": "octocat" }
                                   }
                                ]
                                """)));

        stubFor(get(urlEqualTo("/repos/octocat/Hello-World/branches"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("""
                                [
                                  {
                                    "name": "main",
                                    "commit": { "sha": "abc123" }
                                  }
                                ]
                                """)));
    }


    @Test
    void shouldReturnNonForkRepositoriesWithBranches() {
        String username = "octocat";
        webTestClient.get()
                .uri("http://localhost:" + port + "/api/github/{username}/repositories", username)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(RepositoryDto.class)
                .value(repos -> {
                    assertThat(repos).hasSize(1)
                            .anySatisfy(repo -> {
                                assertThat(repo.name()).isEqualTo("Hello-World");
                                assertThat(repo.ownerLogin()).isEqualTo("octocat");
                                assertThat(repo.branches()).hasSize(1);
                                assertThat(repo.branches().getFirst().name()).isEqualTo("main");
                                assertThat(repo.branches().getFirst().lastCommitSha()).isEqualTo("abc123");
                            });

                });
    }
}