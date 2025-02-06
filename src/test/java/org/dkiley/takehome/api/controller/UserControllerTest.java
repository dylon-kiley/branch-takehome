package org.dkiley.takehome.api.controller;

import org.dkiley.takehome.api.dto.RepoResponse;
import org.dkiley.takehome.exception.NotFoundException;
import org.dkiley.takehome.service.LookupService;
import org.dkiley.takehome.api.dto.UserResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import java.time.LocalDateTime;
import java.util.List;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LookupService lookupService;

    @Test
    public void testLookupUser() throws Exception {
        UserResponse response = new UserResponse(
            "bob.ross",
            "Bob Ross",
            "avatarUrl",
            "Daytona Beach",
            "bob.ross@test.com",
            "https://github.com/bob.ross",
            LocalDateTime.now(),
            List.of(
                new RepoResponse(
                    "happy-tree",
                    "https://github.com/bob.ross/happy-tree"
                )
            )
        );

        when(lookupService.lookupUser(response.userName())).thenReturn(response);


        mockMvc.perform(get("/users/{username}", "bob.ross"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.user_name").value("bob.ross"))
            .andExpect(jsonPath("$.display_name").value("Bob Ross"))
            .andExpect(jsonPath("$.avatar").value("avatarUrl"))
            .andExpect(jsonPath("$.geo_location").value("Daytona Beach"))
            .andExpect(jsonPath("$.email").value("bob.ross@test.com"))
            .andExpect(jsonPath("$.url").value("https://github.com/bob.ross"))
            .andExpect(jsonPath("$.repos[0].name").value("happy-tree"))
            .andExpect(jsonPath("$.repos[0].url").value("https://github.com/bob.ross/happy-tree"));
    }

    @Test
    public void testNotFound() throws Exception {
        String username = "bob.dylan";

        when(lookupService.lookupUser(username)).thenThrow(new NotFoundException("User not found: " + username));

        mockMvc.perform(get("/users/{username}", username))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.error").value("User not found: " + username));
    }

    @Test
    public void testUnknownException() throws Exception {
        String username = "bob.saget";

        when(lookupService.lookupUser(username)).thenThrow(new RuntimeException("Failed to fetch GitHub data"));

        mockMvc.perform(get("/users/{username}", username))
            .andExpect(status().is5xxServerError())
            .andExpect(jsonPath("$.error").value("An unexpected error occurred"));
    }
}
