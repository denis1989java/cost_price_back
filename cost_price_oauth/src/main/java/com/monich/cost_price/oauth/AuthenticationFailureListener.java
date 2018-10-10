package com.monich.cost_price.oauth;

import com.monich.cost_price.common.web.util.UserAgentUtil;
import com.monich.cost_price.service.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthenticationFailureListener implements ApplicationListener<AuthenticationFailureBadCredentialsEvent> {

    private final UserDetailsServiceImpl userService;
    private final HttpServletRequest request;

    @Override
    public void onApplicationEvent(AuthenticationFailureBadCredentialsEvent e) {
        String username = (String) e.getAuthentication().getPrincipal();
        RegisteredUser user = userService.getUserByUsername(username);
        Map<String, String> initialUserData = UserAgentUtil.getUserData(request);

        if (user == null) {
            log.info("Attempt to log in with unknown user. Username: '{}', initialUserData: '{}'",
                    username,
                    initialUserData);
        }
    }
}
