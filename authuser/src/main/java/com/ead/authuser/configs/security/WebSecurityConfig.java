package com.ead.authuser.configs.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import jakarta.servlet.DispatcherType;

@Configuration
@EnableMethodSecurity(prePostEnabled = true) // neste caso redundante apenas para fins didaticos (prePostEnabled = true)
@EnableWebSecurity
public class WebSecurityConfig {

    private final authenticationEntryPointImpl authenticationEntryPointImpl;
    private final UserDetailsServiceImpl detailsServiceImpl;

    private static final String[] AUTH_WHITELIST = {
            "/auth/**"
    };

    public WebSecurityConfig(UserDetailsServiceImpl detailsServiceImpl,
            authenticationEntryPointImpl authenticationEntryPointImpl) {
        this.detailsServiceImpl = detailsServiceImpl;
        this.authenticationEntryPointImpl = authenticationEntryPointImpl;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((authorize) -> authorize
                .dispatcherTypeMatchers(DispatcherType.ERROR).permitAll()
                .requestMatchers(AUTH_WHITELIST).permitAll()
                .requestMatchers(HttpMethod.GET, "/users/**").hasRole("ADMIN")
                // .requestMatchers(HttpMethod.GET, "/users/**").hasAnyAuthority("ROLE_ADMIN") -
                // Outra abordagem
                .anyRequest()
                .authenticated())
                .httpBasic(basic -> basic.authenticationEntryPoint(authenticationEntryPointImpl))
                .formLogin(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
