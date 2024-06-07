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

import com.example.backendbatm.config.jwt.JwtAuthEntryPoint;
import com.example.backendbatm.config.jwt.JwtRequestFilter;


@Configuration
public class AppSecurityConfig {

  @Autowired
  private JwtAuthEntryPoint jwtAuthEntryPoint;

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
                .antMatchers("/api/**").permitAll()
                .antMatchers("/auth/login/form", "/auth/forgot-password/form", "/auth/forgot-password/submit").permitAll()
                .antMatchers("/auth/change/form", "/auth/change/submit").authenticated()
                .antMatchers("/user/**", "/region/**").authenticated()
                .antMatchers("/bot/**").hasAnyRole("Manager", "Admin")
                .antMatchers("/category/**").authenticated()
                .antMatchers("/api/department/**").hasAuthority("Manager")
                // .anyRequest().permitAll()
                .and()
                .formLogin()
                .loginPage("/auth/login/form")
                .usernameParameter("email")
                .loginProcessingUrl("/auth/login/submit")
                .failureUrl("/auth/login/form?error=true")
                .defaultSuccessUrl("/auth/")
                .and()
                .exceptionHandling().authenticationEntryPoint(jwtAuthEntryPoint).and().sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                // Add a filter to validate the tokens with every request
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