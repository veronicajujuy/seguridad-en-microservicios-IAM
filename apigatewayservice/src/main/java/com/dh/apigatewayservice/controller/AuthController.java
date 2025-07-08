package com.dh.apigatewayservice.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@RestController
public class AuthController {

    @GetMapping("/user-info")
    public Mono<Map<String, Object>> getUserInfo(@AuthenticationPrincipal OidcUser principal) {
        Map<String, Object> userInfo = new HashMap<>();

        if (principal != null) {
            userInfo.put("username", principal.getPreferredUsername());
            userInfo.put("email", principal.getEmail());
            userInfo.put("name", principal.getFullName());
            userInfo.put("roles", principal.getClaimAsStringList("realm_access.roles"));
            userInfo.put("authenticated", true);
        } else {
            userInfo.put("authenticated", false);
        }

        return Mono.just(userInfo);
    }

    @GetMapping("/auth-status")
    public Mono<Map<String, Object>> getAuthStatus(@AuthenticationPrincipal OidcUser principal) {
        Map<String, Object> status = new HashMap<>();
        status.put("isAuthenticated", principal != null);
        status.put("username", principal != null ? principal.getPreferredUsername() : "anonymous");
        return Mono.just(status);
    }
}
