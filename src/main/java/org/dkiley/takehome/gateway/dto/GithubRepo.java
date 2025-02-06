package org.dkiley.takehome.gateway.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.dkiley.takehome.api.dto.RepoResponse;

public record GithubRepo(
    String name,
    @JsonProperty("html_url") String htmlUrl
) {
    public RepoResponse toRepoResponse() {
        return new RepoResponse(name, htmlUrl);
    }
}
