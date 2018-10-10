package com.monich.cost_price.service;

import com.monich.cost_price.core.repository.RegisteredUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final RegisteredUserRepository userRepository;
    private final HttpServletRequest request;


    public RegisteredUser getUserByUsername(String username) {
        return userRepository.findByPrincipalEmail(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        String deviceId = request.getParameter("device");
        if (deviceId == null) {
            throw OAuth2Exception.create(OAuth2Exception.INVALID_REQUEST, "Request parameter 'device' is missing");
        }


        RegisteredUser user = getUserByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User with username " + username + " not found");
        }
        boolean passwordExpired = user.getPasswordExpirationDate() != null && user.getPasswordExpirationDate().before(new Date());


        List<? extends GrantedAuthority> authorities;
        if (passwordExpired) {
            authorities = AuthorityUtils.createAuthorityList("password");
        } else {
            authorities = AuthorityUtils.createAuthorityList("all");
        }
        return User.withUsername(user.getPrincipal().getEmail())
                .password(user.getPassword())
                .authorities(authorities)
                .build();

    }
}
