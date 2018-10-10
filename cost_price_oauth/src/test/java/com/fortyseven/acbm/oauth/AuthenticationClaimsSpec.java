package com.fortyseven.acbm.oauth;

import com.monich.cost_price.CostPriceOauthServer;
import com.monich.cost_price.core.repository.PrincipalRepository;
import com.monich.cost_price.core.repository.RegisteredUserRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.ResourceAccessException;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@RunWith(SpringRunner.class)
@SpringBootTest(
        classes = CostPriceOauthServer.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = {"classpath:application.properties"})
public class AuthenticationClaimsSpec {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    PrincipalRepository principalRepository;

    @Autowired
    private RegisteredUserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${security.oauth2.client.client-id}")
    private String clientId;
    @Value("${security.oauth2.client.client-secret}")
    private String clientSecret;

    private static String userName;
    private static final String DEVICE = "auth-device-1";
    private static final String USER_PASSWORD = "password";
    private static final String DEVICE_PASSWORD = "dasdasdasdasdas";

    @Before
    public void init() {

        userName = RandomStringUtils.randomAlphabetic(20) + "@domain.com";
        String phone = RandomStringUtils.randomNumeric(10);

        Principal principal = principalRepository.save(Principal.builder()
                .email(userName)
                .phone(phone)
                .build());

        RegisteredUser user = new RegisteredUser();
        user.setPrincipal(principal);
        user.setPassword(passwordEncoder.encode(USER_PASSWORD));
        userRepository.save(user);

    }

    @Test
    public void authOK() {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // Authorize for users registration
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "client_credentials");

        Map<String, String> result = testRestTemplate
                .withBasicAuth(clientId, clientSecret)
                .exchange("/oauth/token", HttpMethod.POST, new HttpEntity<>(body, headers),
                        new ParameterizedTypeReference<Map<String, String>>() {
                        })
                .getBody();
        assertThat(result.get("scope")).isEqualTo("registry");
        assertThat(result.get("access_token")).isNotNull();


        // Authorize for full control
        body = new LinkedMultiValueMap<>();
        body.add("username", userName);
        body.add("password", USER_PASSWORD);
        body.add("device", "device-1");
        body.add("grant_type", "password");

        result = testRestTemplate
                .withBasicAuth(clientId, clientSecret)
                .exchange("/oauth/token", HttpMethod.POST, new HttpEntity<>(body, headers),
                        new ParameterizedTypeReference<Map<String, String>>() {
                        })
                .getBody();
        assertThat(result.get("scope")).isEqualTo("all");
        assertThat(result.get("access_token")).isNotNull();

        // Refresh token
        String refreshToken = result.get("refresh_token");
        assertThat(refreshToken).isNotNull();

        body = new LinkedMultiValueMap<>();
        body.add("refresh_token", refreshToken);
        body.add("grant_type", "refresh_token");
        body.add("device", "device-1");
        result = testRestTemplate
                .withBasicAuth(clientId, clientSecret)
                .exchange("/oauth/token", HttpMethod.POST, new HttpEntity<>(body, headers),
                        new ParameterizedTypeReference<Map<String, String>>() {
                        })
                .getBody();
        assertThat(result.get("scope")).isEqualTo("all");
        assertThat(result.get("access_token")).isNotNull();


        // Now trying to login from another device
        body = new LinkedMultiValueMap<>();
        body.add("username", userName);
        body.add("password", USER_PASSWORD);
        body.add("device", "device-2");
        body.add("grant_type", "password");

        result = testRestTemplate
                .withBasicAuth(clientId, clientSecret)
                .exchange("/oauth/token", HttpMethod.POST, new HttpEntity<>(body, headers),
                        new ParameterizedTypeReference<Map<String, String>>() {
                        })
                .getBody();
        assertThat(result.get("scope")).isEqualTo("device");
        assertThat(result.get("access_token")).isNotNull();


        // Check access token
        String accessToken = result.get("access_token");
        body = new LinkedMultiValueMap<>();
        body.add("token", accessToken);

        ResponseEntity<Map<String, ?>> resultEntity = testRestTemplate
                .withBasicAuth(clientId, clientSecret)
                .exchange("/oauth/check_token", HttpMethod.POST, new HttpEntity<>(body, headers),
                        new ParameterizedTypeReference<Map<String, ?>>() {
                        });

        assertThat(resultEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void mobileAuthTest() {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();

        body.add("username", "device");
        body.add("password", DEVICE_PASSWORD);
        body.add("device", DEVICE);
        body.add("grant_type", "password");

        Map<String, String> result = testRestTemplate
                .withBasicAuth(clientId, clientSecret)
                .exchange("/oauth/token", HttpMethod.POST, new HttpEntity<>(body, headers),
                        new ParameterizedTypeReference<Map<String, String>>() {
                        })
                .getBody();
        assertThat(result.get("scope")).isEqualTo("all");
        assertThat(result.get("access_token")).isNotNull();

        // Refresh token
        String refreshToken = result.get("refresh_token");
        assertThat(refreshToken).isNotNull();

        body = new LinkedMultiValueMap<>();
        body.add("refresh_token", refreshToken);
        body.add("grant_type", "refresh_token");
        body.add("device", DEVICE);
        result = testRestTemplate
                .withBasicAuth(clientId, clientSecret)
                .exchange("/oauth/token", HttpMethod.POST, new HttpEntity<>(body, headers),
                        new ParameterizedTypeReference<Map<String, String>>() {
                        })
                .getBody();
        assertThat(result.get("scope")).isEqualTo("all");
        assertThat(result.get("access_token")).isNotNull();
    }

    @Test
    public void timeoutTest() throws InterruptedException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // Authorize for full control
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("username", userName);
        body.add("password", USER_PASSWORD);
        body.add("device", "device-1");
        body.add("grant_type", "password");

        Map<String, Object> result = testRestTemplate
                .withBasicAuth(clientId, clientSecret)
                .exchange("/oauth/token", HttpMethod.POST, new HttpEntity<>(body, headers),
                        new ParameterizedTypeReference<Map<String, Object>>() {
                        })
                .getBody();
        assertThat(result.get("scope")).isEqualTo("all");
        assertThat(result.get("access_token")).isNotNull();
        assertThat(result.get("expires_in")).isEqualTo(4);
        String refreshToken1 = result.get("refresh_token").toString();

        body = new LinkedMultiValueMap<>();
        body.add("token", result.get("access_token").toString());
        result = testRestTemplate
                .withBasicAuth(clientId, clientSecret)
                .exchange("/oauth/check_token", HttpMethod.POST, new HttpEntity<>(body, headers),
                        new ParameterizedTypeReference<Map<String, Object>>() {
                        })
                .getBody();
        assertThat(result.get("error")).isNull();

        // Sleep for 5 seconds - original token expired
        Thread.sleep(5000L);
        result = testRestTemplate
                .withBasicAuth(clientId, clientSecret)
                .exchange("/oauth/check_token", HttpMethod.POST, new HttpEntity<>(body, headers),
                        new ParameterizedTypeReference<Map<String, Object>>() {
                        })
                .getBody();
        assertThat(result.get("error")).isEqualTo("invalid_token");
        assertThat(result.get("error_description")).isEqualTo("Token has expired");
        // refresh it now
        body = new LinkedMultiValueMap<>();
        body.add("refresh_token", refreshToken1);
        body.add("grant_type", "refresh_token");
        body.add("device", "device-1");
        result = testRestTemplate
                .withBasicAuth(clientId, clientSecret)
                .exchange("/oauth/token", HttpMethod.POST, new HttpEntity<>(body, headers),
                        new ParameterizedTypeReference<Map<String, Object>>() {
                        })
                .getBody();
        assertThat(result.get("scope")).isEqualTo("all");
        assertThat(result.get("access_token")).isNotNull();
        String refreshToken2 = result.get("refresh_token").toString();

        // Sleep for 5 seconds once again - original refresh token expired
        Thread.sleep(5000L);
        final MultiValueMap<String, String> body1 = new LinkedMultiValueMap<>();
        body1.add("refresh_token", refreshToken1);
        body1.add("grant_type", "refresh_token");
        body1.add("device", "device-1");
        assertThatExceptionOfType(ResourceAccessException.class).isThrownBy(() -> {
            testRestTemplate
                    .withBasicAuth(clientId, clientSecret)
                    .exchange("/oauth/token", HttpMethod.POST, new HttpEntity<>(body1, headers),
                            new ParameterizedTypeReference<Map<String, Object>>() {
                            })
                    .getBody();
        });
        // But last token still alive
        body = new LinkedMultiValueMap<>();
        body.add("refresh_token", refreshToken2);
        body.add("grant_type", "refresh_token");
        body.add("device", "device-1");
        result = testRestTemplate
                .withBasicAuth(clientId, clientSecret)
                .exchange("/oauth/token", HttpMethod.POST, new HttpEntity<>(body, headers),
                        new ParameterizedTypeReference<Map<String, Object>>() {
                        })
                .getBody();
        assertThat(result.get("scope")).isEqualTo("all");
        assertThat(result.get("access_token")).isNotNull();
    }
}
