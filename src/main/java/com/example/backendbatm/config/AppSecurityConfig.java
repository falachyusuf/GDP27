package com.example.backendbatm.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.backendbatm.config.jwt.JwtAuthenticationEntryPoint;
import com.example.backendbatm.config.jwt.JwtRequestFilter;

@Configuration
public class AppSecurityConfig {
  @Autowired
  private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

  @Autowired
	private JwtRequestFilter jwtRequestFilter;

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
                .antMatchers("/api/auth/login").permitAll()
                .antMatchers("/auth/login/form", "/auth/forgot-password/form", "/auth/forgot-password/submit")
                .permitAll()
                .antMatchers("/auth/change/form", "/auth/change/submit").authenticated()
                .antMatchers("/user/**", "/region/**").hasRole("Manager")
                .antMatchers("/bot/**").hasAnyRole("Manager", "Admin")
                .antMatchers("/category/**").authenticated()
                .antMatchers("/api/employee/**").hasRole("Manager")
                // .anyRequest().permitAll()
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
                .exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint).and().sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

                http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
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