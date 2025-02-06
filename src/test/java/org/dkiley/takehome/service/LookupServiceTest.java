package org.dkiley.takehome.service;

import org.dkiley.takehome.api.dto.UserResponse;
import org.dkiley.takehome.exception.NotFoundException;
import org.dkiley.takehome.gateway.GithubGateway;
import org.dkiley.takehome.gateway.dto.GithubRepo;
import org.dkiley.takehome.gateway.dto.GithubUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class LookupServiceTest {
    private GithubGateway githubGateway;
    private LookupService lookupService;

    @BeforeEach
    void setUp() {
        githubGateway = mock(GithubGateway.class);
        lookupService = new LookupService(githubGateway);
    }

    @Test
    void testLookupUser_Success() {
        GithubUser userInfo = new GithubUser(
            "bob.ross",
            "Bob Ross",
            "avatarUrl",
            "Daytona Beach",
            "bob.ross@test.com",
            "https://github.com/bob.ross",
            LocalDateTime.now()
        );

        List<GithubRepo> repos = List.of(
            new GithubRepo(
                "happy-tree",
                "https://github.com/bob.ross/happy-tree"
            )
        );

        when(githubGateway.getGithubUserByUsername(userInfo.login())).thenReturn(userInfo);
        when(githubGateway.getGithubReposByUsername(userInfo.login())).thenReturn(repos);

        UserResponse result = lookupService.lookupUser(userInfo.login());

        assertEquals(userInfo.login(), result.userName());
        assertEquals(userInfo.name(), result.displayName());
        assertEquals(userInfo.avatarUrl(), result.avatar());
        assertEquals(userInfo.location(), result.geoLocation());
        assertEquals(userInfo.email(), result.email());
        assertEquals(userInfo.htmlUrl(), result.url());
        assertEquals(repos.size(), result.repos().size());
        assertEquals(repos.getFirst().name(), result.repos().getFirst().name());
        assertEquals(repos.getFirst().htmlUrl(), result.repos().getFirst().url());

        verify(githubGateway, times(1)).getGithubUserByUsername(userInfo.login());
        verify(githubGateway, times(1)).getGithubReposByUsername(userInfo.login());
    }

    @Test
    void testLookupUser_GithubUserNotFound() {
        String username = "bob.dylan";

        when(githubGateway.getGithubUserByUsername(username)).thenThrow(new NotFoundException("User not found: " + username));

        assertThrows(NotFoundException.class, () -> lookupService.lookupUser(username));

        verify(githubGateway, times(1)).getGithubUserByUsername(username);
        verify(githubGateway, times(0)).getGithubReposByUsername(any());
    }

    @Test
    void testLookupUser_GithubUserUnknownException() {
        String username = "bob.dylan";

        when(githubGateway.getGithubUserByUsername(username)).thenThrow(new RuntimeException("Failed to fetch GitHub data"));

        assertThrows(RuntimeException.class, () -> lookupService.lookupUser(username));

        verify(githubGateway, times(1)).getGithubUserByUsername(username);
        verify(githubGateway, times(0)).getGithubReposByUsername(any());
    }

    @Test
    void testLookupUser_GithubRepoUserNotFound() {
        GithubUser userInfo = new GithubUser(
            "bob.ross",
            "Bob Ross",
            "avatarUrl",
            "Daytona Beach",
            "bob.ross@test.com",
            "https://github.com/bob.ross",
            LocalDateTime.now()
        );

        when(githubGateway.getGithubUserByUsername(userInfo.login())).thenReturn(userInfo);
        when(githubGateway.getGithubReposByUsername(userInfo.login())).thenThrow(new NotFoundException("User not found: " + userInfo.login()));


        assertThrows(NotFoundException.class, () -> lookupService.lookupUser(userInfo.login()));

        verify(githubGateway, times(1)).getGithubUserByUsername(userInfo.login());
        verify(githubGateway, times(1)).getGithubReposByUsername(userInfo.login());
    }

    @Test
    void testLookupUser_GithubRepoUnknownException() {
        GithubUser userInfo = new GithubUser(
                "bob.ross",
                "Bob Ross",
                "avatarUrl",
                "Daytona Beach",
                "bob.ross@test.com",
                "https://github.com/bob.ross",
                LocalDateTime.now()
        );

        when(githubGateway.getGithubUserByUsername(userInfo.login())).thenReturn(userInfo);
        when(githubGateway.getGithubReposByUsername(userInfo.login())).thenThrow(new RuntimeException("Failed to fetch GitHub data"));

        assertThrows(RuntimeException.class, () -> lookupService.lookupUser(userInfo.login()));

        verify(githubGateway, times(1)).getGithubUserByUsername(userInfo.login());
        verify(githubGateway, times(1)).getGithubReposByUsername(userInfo.login());
    }
}
