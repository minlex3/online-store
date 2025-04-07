package com.store.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.RedirectServerAuthenticationSuccessHandler;
import org.springframework.security.web.server.authentication.logout.RedirectServerLogoutSuccessHandler;
import org.springframework.web.server.WebSession;
import reactor.core.publisher.Mono;

import java.net.URI;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http,
                                                            ReactiveUserDetailsService userDetailsService) {
        RedirectServerLogoutSuccessHandler handler = new RedirectServerLogoutSuccessHandler();
        handler.setLogoutSuccessUrl(URI.create("/products"));

        return http
                .authorizeExchange(exchanges -> exchanges
//                        .anyExchange().permitAll()
                        .pathMatchers(
                                "/login",
                                "/register",
                                "/products",
                                "/products/*",
                                "/static/**"
                        ).permitAll()
                        .anyExchange().authenticated()
                )
                .formLogin(Customizer.withDefaults())
//                .formLogin(login -> login
//                        .loginPage("/login")
//                        .authenticationSuccessHandler(new RedirectServerAuthenticationSuccessHandler("/"))
//                )
                .logout(logout -> logout
                                .logoutUrl("/logout")
                                .logoutSuccessHandler(handler)
//                                .logoutSuccessHandler((exchange, authentication) ->
//                                        exchange.getExchange().getSession()
//                                                .flatMap(WebSession::invalidate)
//                                                .then(Mono.fromRunnable(() -> exchange.getExchange().getResponse()
//                                                        .setStatusCode(HttpStatus.OK)))
//                                )
                )
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .build();
//                                .deleteCookies("JSESSIONID")
//                .csrf(ServerHttpSecurity.CsrfSpec::disable)
//                .userDetailsService(userDetailsService);

//        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
