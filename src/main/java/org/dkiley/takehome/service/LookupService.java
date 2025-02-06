package org.dkiley.takehome.service;

import org.dkiley.takehome.api.dto.UserResponse;
import org.dkiley.takehome.gateway.GithubGateway;
import org.dkiley.takehome.gateway.dto.GithubRepo;
import org.dkiley.takehome.gateway.dto.GithubUser;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class LookupService {
    private final GithubGateway githubGateway;

    public LookupService(GithubGateway githubGateway) {
        this.githubGateway = githubGateway;
    }

    @Cacheable(value = "userCache", key = "#username")
    public UserResponse lookupUser(String username) {
        // Lookup user info
        GithubUser userInfo = githubGateway.getGithubUserByUsername(username);

        // Lookup user repos
        List<GithubRepo> repos = githubGateway.getGithubReposByUsername(username);

        return UserResponse.from(userInfo, repos);
    }
}
