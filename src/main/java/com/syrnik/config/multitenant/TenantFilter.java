package com.syrnik.config.multitenant;

import java.io.IOException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.syrnik.security.UserDetailsImpl;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class TenantFilter extends OncePerRequestFilter {

    private static final String REGISTER_ENDPOINT = "/api/register";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
          throws ServletException, IOException {
        String requestURI = request.getRequestURI();

        if(requestURI.equals(REGISTER_ENDPOINT) && request.getMethod().equals("POST")) {
            String tenantName = request.getHeader("X-TenantName");
            TenantContext.setCurrentTenant(tenantName);

            try {
                filterChain.doFilter(request, response);
            } finally {
                TenantContext.setCurrentTenant("");
            }
        } else {
            // For other endpoints, retrieve the logged-in user information from the Spring Security context
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if(authentication != null && authentication.isAuthenticated()) {
                Object principal = authentication.getPrincipal();
                if(principal instanceof UserDetailsImpl userDetails) {
                    // Extract the branch information from the UserDetails object and set the tenant
                    String branch = userDetails.getBranch();
                    TenantContext.setCurrentTenant(branch);
                }
            }

            // Continue processing the request
            filterChain.doFilter(request, response);
        }
    }
}