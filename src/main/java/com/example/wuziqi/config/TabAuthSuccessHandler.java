package com.example.wuziqi.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Component
public class TabAuthSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static final String TAB_PREFIX = "tab:";
    private static final int TAB_TTL_HOURS = 2;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        String tabId = request.getParameter("tabId");
        String username = authentication.getName();

        if (tabId != null && !tabId.isEmpty()) {
            redisTemplate.opsForValue().set(TAB_PREFIX + tabId, username, TAB_TTL_HOURS, TimeUnit.HOURS);
        }

        String redirectUrl = "/?tabId=" + (tabId != null ? tabId : "");
        response.sendRedirect(redirectUrl);
    }
}