package org.dkiley.takehome.api.controller;

import org.dkiley.takehome.api.dto.UserResponse;
import org.dkiley.takehome.service.LookupService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    private final LookupService lookupService;

    public UserController(LookupService lookupService) {
        this.lookupService = lookupService;
    }

    @GetMapping("/users/{username}")
    public ResponseEntity<UserResponse> lookupUser(@PathVariable String username) {
        UserResponse user = lookupService.lookupUser(username);

        return ResponseEntity.ok(user);
    }
}
