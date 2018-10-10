package com.monich.cost_price.oauth.config;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import java.util.stream.Collectors;

class AuthoritiesTokenEnhancer implements TokenEnhancer {
    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        ((DefaultOAuth2AccessToken) accessToken).setScope(
                authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet()));
        return accessToken;
    }
}
