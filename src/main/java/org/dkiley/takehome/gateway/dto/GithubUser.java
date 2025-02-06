package org.dkiley.takehome.gateway.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public record GithubUser(
    String login,
    String name,
    @JsonProperty("avatar_url") String avatarUrl,
    String location,
    String email,
    @JsonProperty("html_url") String htmlUrl,
    @JsonProperty("created_at") LocalDateTime createdAt
) { }