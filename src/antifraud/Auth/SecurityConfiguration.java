package antifraud.Auth;

import antifraud.Model.ApiRoutes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class SecurityConfiguration {

    private final RestAuthenticationEntryPoint restAuthenticationEntryPoint;
    private final UserDetailsService userDetailsService;

    @Autowired
    public SecurityConfiguration(RestAuthenticationEntryPoint restAuthenticationEntryPoint, UserDetailsService userDetailsService) {
        this.restAuthenticationEntryPoint = restAuthenticationEntryPoint;
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, PasswordEncoder bCryptPasswordEncoder)
        throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
            .userDetailsService(userDetailsService)
            .passwordEncoder(bCryptPasswordEncoder)
            .and()
            .build();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.httpBasic()
            .authenticationEntryPoint(restAuthenticationEntryPoint) // Handles auth error
            .and()
            .csrf()
            .disable()
            .headers()
            .frameOptions()
            .disable() // for Postman, the H2 console
            .and()
            .authorizeRequests() // manage access
            .antMatchers(HttpMethod.POST, ApiRoutes.USER)
            .permitAll()
            .mvcMatchers("/actuator/shutdown")
            .permitAll() // needs to run test
            // other matchers
            .mvcMatchers(HttpMethod.GET, ApiRoutes.USER_LIST)
            .hasAnyRole(Role.ADMINISTRATOR.name(), Role.SUPPORT.name())
            .mvcMatchers(HttpMethod.DELETE, ApiRoutes.USERNAME, "/api/auth/user")
            .hasRole(Role.ADMINISTRATOR.name())
            .mvcMatchers(HttpMethod.POST, ApiRoutes.TRANSACTION)
            .hasRole(Role.MERCHANT.name())
            .mvcMatchers(HttpMethod.PUT, ApiRoutes.USER_ACCESS)
            .hasRole(Role.ADMINISTRATOR.name())
            .mvcMatchers(HttpMethod.PUT, ApiRoutes.USER_ROLE)
            .hasRole(Role.ADMINISTRATOR.name())
            .mvcMatchers(ApiRoutes.SUSPICIOUS_IP + "/**")
            .hasRole(Role.SUPPORT.name())
            .mvcMatchers(ApiRoutes.STOLEN_CARD, ApiRoutes.ANTIFRAUD_HISTORY, ApiRoutes.ANTIFRAUD_HISTORY + "/**")
            .hasRole(Role.SUPPORT.name())
            .mvcMatchers(HttpMethod.PUT, ApiRoutes.TRANSACTION)
            .hasRole(Role.SUPPORT.name())
            .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS); // no session

        return http.build();
    }
}
