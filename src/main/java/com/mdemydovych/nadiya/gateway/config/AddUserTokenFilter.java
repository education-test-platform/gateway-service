package com.mdemydovych.nadiya.gateway.config;

import java.util.Objects;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

public class AddUserTokenFilter implements GlobalFilter, Ordered {


  @Override
  public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
    return ReactiveSecurityContextHolder.getContext()
        .map(SecurityContext::getAuthentication)
        .map(auth -> {
          if (Objects.nonNull(auth)) {
            OAuth2User user = (((OAuth2AuthenticationToken) auth).getPrincipal());
            ServerHttpRequest request = exchange.getRequest().mutate()
                .header("Authorization",
                    "Bearer " + ((DefaultOidcUser) user).getIdToken().getTokenValue())
                .build();
            return exchange.mutate().request(request).build();
          } else {
            return exchange;
          }
        })
        .flatMap(chain::filter);
  }

  @Override
  public int getOrder() {
    return -1;
  }
}
