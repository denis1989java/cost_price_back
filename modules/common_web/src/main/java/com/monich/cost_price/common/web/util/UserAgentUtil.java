package com.monich.cost_price.common.web.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class UserAgentUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static Map<String, String> getUserData(HttpServletRequest request) {
        String initialUserData = request.getHeader("Initial-User-Data");
        if (StringUtils.isNotBlank(initialUserData)) {
            try {
                return objectMapper.readValue(initialUserData, new TypeReference<HashMap<String, String>>() {
                });
            } catch (IOException e) {
                log.warn("UserData parse error", e);
            }
        }
        return null;
    }

    public static String getUserAgent(HttpServletRequest request) {
        Map<String, String> userData = getUserData(request);
        if (userData != null) {
            return userData.get(HttpHeaders.USER_AGENT);
        } else {
            return request.getHeader(HttpHeaders.USER_AGENT);
        }
    }
}
