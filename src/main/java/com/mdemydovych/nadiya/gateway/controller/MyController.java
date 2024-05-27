package com.mdemydovych.nadiya.gateway.controller;

import com.mdemydovych.nadiya.model.user.UserDto;
import com.mdemydovych.nadiya.model.user.UserRole;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/my")
public class MyController {

  @GetMapping("/info")
  private Mono<UserDto> myInfo(@AuthenticationPrincipal DefaultOidcUser user) {
    return Mono.just(buildUserDto(user));
  }

  private UserDto buildUserDto(DefaultOidcUser user) {
    UserDto result = new UserDto();
    result.setId(user.getClaim("userId"));
    result.setEmail(user.getClaim("mail"));
    result.setRole(UserRole.valueOf(user.getClaim("authorities")));
    result.setUsername(user.getClaim("sub"));
    return result;
  }
}
