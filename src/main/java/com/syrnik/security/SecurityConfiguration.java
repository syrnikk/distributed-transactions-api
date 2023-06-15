package com.syrnik.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationEntryPointFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

import com.syrnik.config.multitenant.TenantFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final AuthenticationSuccessHandler authenticationSuccessHandler;
    private final AuthenticationEntryPoint authenticationEntryPoint;
    private final TenantFilter tenantFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
              .csrf(AbstractHttpConfigurer::disable)
              .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
              .formLogin(httpSecurityFormLoginConfigurer -> httpSecurityFormLoginConfigurer
                    .usernameParameter("login")
                    .passwordParameter("password")
                    .loginProcessingUrl("/api/login")
                    .successHandler(authenticationSuccessHandler)
                    .failureHandler(new AuthenticationEntryPointFailureHandler(authenticationEntryPoint))
                    .permitAll())
              .logout(httpSecurityLogoutConfigurer -> httpSecurityLogoutConfigurer
                    .logoutUrl("/api/logout")
                    .deleteCookies("JSESSIONID")
                    .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler(HttpStatus.OK))
                    .permitAll())
              .exceptionHandling(
                    httpSecurityExceptionHandlingConfigurer -> httpSecurityExceptionHandlingConfigurer.authenticationEntryPoint(
                          authenticationEntryPoint))
              .addFilterAfter(tenantFilter, UsernamePasswordAuthenticationFilter.class)
              .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}