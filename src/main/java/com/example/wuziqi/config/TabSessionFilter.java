package com.example.wuziqi.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.Principal;

@Component
@Order(1)
public class TabSessionFilter implements Filter {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static final String TAB_PREFIX = "tab:";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String tabId = httpRequest.getParameter("tabId");

        if (tabId != null && !tabId.isEmpty()) {
            Object usernameObj = redisTemplate.opsForValue().get(TAB_PREFIX + tabId);
            if (usernameObj != null) {
                String username = usernameObj.toString();
                request = new TabRequestWrapper(httpRequest, username);
            }
        }

        chain.doFilter(request, response);
    }

    private static class TabRequestWrapper extends HttpServletRequestWrapper {
        private final String username;

        public TabRequestWrapper(HttpServletRequest request, String username) {
            super(request);
            this.username = username;
        }

        @Override
        public Principal getUserPrincipal() {
            return () -> username;
        }

        @Override
        public String getRemoteUser() {
            return username;
        }
    }
}