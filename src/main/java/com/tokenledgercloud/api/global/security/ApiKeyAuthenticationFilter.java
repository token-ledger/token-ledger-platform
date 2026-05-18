package com.tokenledgercloud.api.global.security;

import com.tokenledgercloud.api.domain.member.Member;
import com.tokenledgercloud.api.service.ApiKeyService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@RequiredArgsConstructor
public class ApiKeyAuthenticationFilter extends OncePerRequestFilter {

    private final ApiKeyService apiKeyService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        String apiKey = resolveApiKey(request);

        if (StringUtils.hasText(apiKey)) {
            try {
                Member member = apiKeyService.verifyApiKey(apiKey);
                
                UserDetails userDetails = new User(
                        member.getEmail(),
                        "",
                        Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + member.getRole().name()))
                );

                Authentication authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (Exception e) {
                // Invalid API key - we can either throw error or just let it pass to be caught by security chain
                // Here we just don't set the authentication
            }
        }

        filterChain.doFilter(request, response);
    }

    private String resolveApiKey(HttpServletRequest request) {
        return request.getHeader("X-API-KEY");
    }
}
