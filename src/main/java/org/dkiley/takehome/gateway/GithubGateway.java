package org.dkiley.takehome.gateway;

import org.dkiley.takehome.exception.NotFoundException;
import org.dkiley.takehome.gateway.dto.GithubRepo;
import org.dkiley.takehome.gateway.dto.GithubUser;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import java.util.List;

@Service
public class GithubGateway {
    private final String githubUserUrl = "https://api.github.com/users/";
    private final RestTemplate restTemplate;

    public GithubGateway(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public GithubUser getGithubUserByUsername(String username) {
        try {
            return restTemplate.getForEntity( githubUserUrl + username, GithubUser.class).getBody();
        } catch (HttpClientErrorException.NotFound e) {
            throw new NotFoundException("User not found: " + username);
        } catch (Exception e) {
            throw new RuntimeException("Unknown error occurred while fetching user: " + username, e);
        }
    }

    public List<GithubRepo> getGithubReposByUsername(String username) {
        try {
            GithubRepo[] repos = restTemplate.getForObject(githubUserUrl + username + "/repos", GithubRepo[].class);

            return repos == null || repos.length == 0 ? List.of() : List.of(repos);
        } catch (HttpClientErrorException.NotFound e) {
            throw new NotFoundException("Repos not found for user: " + username);
        } catch (Exception e) {
            throw new RuntimeException("Unknown error occurred while fetching repos for user: " + username, e);
        }
    }

}
