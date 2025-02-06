package org.dkiley.takehome.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.dkiley.takehome.gateway.dto.GithubRepo;
import org.dkiley.takehome.gateway.dto.GithubUser;

import java.time.LocalDateTime;
import java.util.List;

public record UserResponse(
    String userName,
    String displayName,
    String avatar,
    String geoLocation,
    String email,
    String url,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime createdAt,
    List<RepoResponse> repos
) {
    public static UserResponse from(GithubUser user, List<GithubRepo> repoList) {
        return new UserResponse(
            user.login(),
            user.name(),
            user.avatarUrl(),
            user.location(),
            user.email(),
            user.htmlUrl(),
            user.createdAt(),
            repoList.stream().map(GithubRepo::toRepoResponse).toList()
        );
    }
}
