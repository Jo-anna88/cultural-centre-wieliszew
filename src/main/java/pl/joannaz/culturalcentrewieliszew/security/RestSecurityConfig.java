package pl.joannaz.culturalcentrewieliszew.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import pl.joannaz.culturalcentrewieliszew.security.jwt.JWTAuthenticationFilter;
import pl.joannaz.culturalcentrewieliszew.security.jwt.JWTService;
import pl.joannaz.culturalcentrewieliszew.user.UserService;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class RestSecurityConfig { // import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter - removed, because it was deprecated

    private final UserService userService;
    private final JWTService jwtService;
    //private final JWTAuthenticationEntryPoint unauthorizedHandler;

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userService);
        authProvider.setPasswordEncoder(encoder());

        return authProvider;
    }

    @Bean
    public JWTAuthenticationFilter jwtAuthenticationFilter() {
        return new JWTAuthenticationFilter(jwtService, userService);
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // against method reference we could use lambda: (csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // we want to re-authenticate the user on every request
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS).permitAll() // for pre-flight requests

                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll() // for development

                        .requestMatchers(HttpMethod.DELETE,"/api/events/**").hasAnyRole("EMPLOYEE", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/events/**").hasAnyRole("EMPLOYEE", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/events/**").hasAnyRole("EMPLOYEE", "ADMIN")

                        .requestMatchers("/api/booking/**").hasRole("CLIENT")

                        .requestMatchers(HttpMethod.DELETE,"/api/classes/**").hasAnyRole("EMPLOYEE", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/classes/**").hasAnyRole("EMPLOYEE", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/classes/**").hasAnyRole("EMPLOYEE", "ADMIN")

                        .requestMatchers(HttpMethod.GET,"/api/user/employee").permitAll()
                        .requestMatchers(HttpMethod.GET,"/api/user/employee/{id}/profile").permitAll()
                        .requestMatchers("/api/user/employee/**").hasRole("ADMIN") // only ADMIN can add, update and delete employee
                        .requestMatchers("/api/user/profile").hasAnyRole("CLIENT", "EMPLOYEE", "ADMIN")
                        .requestMatchers("/api/user/teachers").permitAll()
                        .requestMatchers("/api/user/**").hasRole("CLIENT")

                        .requestMatchers("/api/auth/**", "/api/address/**", "/api/contact",
                                "/api/events/**", "/api/classes/**").permitAll() // permitAll - requires no authorization
                                // and is a public endpoint; the Authentication is never retrieved from the session
                        .anyRequest().authenticated() // require authentication for ALL other requests
                );
//                .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
//        ;
        http.authenticationProvider(authenticationProvider());
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
