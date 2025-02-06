package org.dkiley.takehome.gateway;

import org.dkiley.takehome.exception.NotFoundException;
import org.dkiley.takehome.gateway.dto.GithubRepo;
import org.dkiley.takehome.gateway.dto.GithubUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import java.time.LocalDateTime;
import java.util.List;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class GithubGatewayTest {
    private RestTemplate restTemplate;
    private GithubGateway githubGateway;

    @BeforeEach
    void setUp() {
        restTemplate = mock(RestTemplate.class);
        githubGateway = new GithubGateway(restTemplate);
    }

    @Test
    void testGetGithubUserByUsername_Success() {
        GithubUser userInfo = new GithubUser(
            "bob.ross",
            "Bob Ross",
            "avatarUrl",
            "Daytona Beach",
            "bob.ross@test.com",
            "https://github.com/bob.ross",
            LocalDateTime.now()
        );

        when(restTemplate.getForEntity("https://api.github.com/users/" + userInfo.login(), GithubUser.class))
            .thenReturn(new ResponseEntity<>(userInfo, HttpStatus.OK));

        GithubUser result = githubGateway.getGithubUserByUsername(userInfo.login());

        assertNotNull(result);
        assertEquals(userInfo.login(), result.login());
        assertEquals(userInfo.name(), result.name());
        assertEquals(userInfo.avatarUrl(), result.avatarUrl());
        assertEquals(userInfo.location(), result.location());
        assertEquals(userInfo.email(), result.email());
        assertEquals(userInfo.htmlUrl(), result.htmlUrl());

        verify(restTemplate, times(1)).getForEntity("https://api.github.com/users/" + userInfo.login(), GithubUser.class);
    }

    @Test
    void testGetGithubUserByUsername_NotFound() {
        String username = "bob.saget";

        when(restTemplate.getForEntity("https://api.github.com/users/" + username, GithubUser.class))
            .thenThrow(HttpClientErrorException.create(
                HttpStatus.NOT_FOUND, "Not Found", new HttpHeaders(), null, null
            ));

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            githubGateway.getGithubUserByUsername(username);
        });
        assertEquals("User not found: " + username, exception.getMessage());
    }

    @Test
    void testGetGithubReposByUsername_Success() {
        String username = "bob.ross";
        List<GithubRepo> repos = List.of(
            new GithubRepo(
                "happy-tree",
                "https://github.com/bob.ross/happy-tree"
            ),
            new GithubRepo(
                "happy-accident",
                "https://github.com/bob.ross/happy-accident"
            )
        );

        when(restTemplate.getForObject("https://api.github.com/users/" + username + "/repos", GithubRepo[].class))
            .thenReturn(repos.toArray(GithubRepo[]::new));

        List<GithubRepo> result = githubGateway.getGithubReposByUsername(username);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("happy-tree", result.getFirst().name());
        assertEquals("https://github.com/bob.ross/happy-tree", result.getFirst().htmlUrl());
        assertEquals("happy-accident", result.getLast().name());
        assertEquals("https://github.com/bob.ross/happy-accident", result.getLast().htmlUrl());
        verify(restTemplate, times(1)).getForObject("https://api.github.com/users/" + username + "/repos", GithubRepo[].class);
    }

    @Test
    void testGetGithubReposByUsername_NoRepos() {
        String username = "bob.dylan";
        GithubRepo[] emptyRepos = new GithubRepo[0];

        when(restTemplate.getForObject("https://api.github.com/users/" + username + "/repos", GithubRepo[].class))
                .thenReturn(emptyRepos);

        List<GithubRepo> result = githubGateway.getGithubReposByUsername(username);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(restTemplate, times(1)).getForObject("https://api.github.com/users/" + username + "/repos", GithubRepo[].class);
    }

    @Test
    void testGetGithubReposByUsername_NotFound() {
        String username = "bob.saget";

        when(restTemplate.getForObject("https://api.github.com/users/" + username + "/repos", GithubRepo[].class))
            .thenThrow(HttpClientErrorException.create(
                HttpStatus.NOT_FOUND, "Not Found", new HttpHeaders(), null, null
            ));

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            githubGateway.getGithubReposByUsername(username);
        });
        assertEquals("Repos not found for user: " + username, exception.getMessage());
    }

    @Test
    void testGetGithubReposByUsername_Exception() {
        String username = "bob.saget";

        when(restTemplate.getForObject("https://api.github.com/users/" + username + "/repos", GithubRepo[].class))
                .thenThrow(new RuntimeException("Unknown error"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            githubGateway.getGithubReposByUsername(username);
        });
        assertEquals("Unknown error occurred while fetching repos for user: " + username, exception.getMessage());
    }
}
