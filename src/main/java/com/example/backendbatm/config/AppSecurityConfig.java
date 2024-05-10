package com.example.backendbatm.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class AppSecurityConfig {
  @Bean // Authentication
  public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
      throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
  }

    @Bean // Authorization
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeHttpRequests((auth) -> {
                    try {
                        auth
                                .antMatchers("/api/**").permitAll()
                                .antMatchers("/auth/login/form", "/auth/forgotPassword", "/auth/resetPassword").permitAll()
                                .antMatchers("/auth/change/form", "/auth/change/submit").authenticated()
                                .antMatchers("/user/**", "/region/**").hasRole("Manager")
                                .antMatchers("/bot/**").hasAnyRole("Manager", "Admin")
                                .antMatchers("/category/**").authenticated()
                                .anyRequest().permitAll()
                                .and()
                                .formLogin()
                                .loginPage("/auth/login/form")
                                .usernameParameter("email")
                                .loginProcessingUrl("/auth/login/submit")
                                .failureUrl("/auth/login/form?error=true")
                                .defaultSuccessUrl("/auth/")
                                .and()
                                .httpBasic()
                                .and()
                                .logout();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
        return http.build();
    }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}