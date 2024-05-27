package com.mdemydovych.nadiya.gateway.config;

import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity.CsrfSpec;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebSecurity
@EnableWebFluxSecurity
public class SecurityConfiguration {

  @Bean
  public GlobalFilter requestFilter() {
    return new AddUserTokenFilter();
  }

  @Bean
  public SecurityWebFilterChain csrfSecurityFilterChain(ServerHttpSecurity http) {
    return http
        .authorizeExchange(
            (exchanges) -> exchanges.anyExchange().authenticated())
        .oauth2Client(Customizer.withDefaults())
        .oauth2Login(Customizer.withDefaults())
        .csrf(CsrfSpec::disable)
        .build();
  }
}
