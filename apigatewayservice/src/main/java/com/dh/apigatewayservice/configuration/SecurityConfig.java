package com.dh.apigatewayservice.configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.logout.RedirectServerLogoutSuccessHandler;
import org.springframework.security.web.server.authentication.logout.ServerLogoutSuccessHandler;
import java.net.URI;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain (ServerHttpSecurity http) {
        http
                .authorizeExchange()
                .pathMatchers("/logout", "/user-info", "/auth-status").permitAll() // Permitir acceso al logout y endpoints de info
                .anyExchange()
                .authenticated()
                .and()
                .oauth2Login() // to redirect to oauth2 login page.
                .and()
                .logout()
                .logoutSuccessHandler(logoutSuccessHandler());

        return http.build();
    }

    @Bean
    public ServerLogoutSuccessHandler logoutSuccessHandler() {
        RedirectServerLogoutSuccessHandler logoutSuccessHandler = new RedirectServerLogoutSuccessHandler();
        logoutSuccessHandler.setLogoutSuccessUrl(URI.create("http://localhost:8080/realms/springboot-keycloak/protocol/openid-connect/logout?redirect_uri=http://localhost:9090/"));
        return logoutSuccessHandler;
    }
}
