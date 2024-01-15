package pl.joannaz.culturalcentrewieliszew.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
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

    @Bean //?
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
        return new JWTAuthenticationFilter(jwtService);
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // against method reference we could use lambda: (csrf -> csrf.disable())
//                .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers(HttpMethod.GET, "/api/classes/**")
//                        .hasRole("CLIENT")
//                )
        ;
        http.authenticationProvider(authenticationProvider());
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
//                .authorizeHttpRequests((authz) -> authz
//                        .anyRequest().authenticated()
//                )
//                .httpBasic(withDefaults());
        return http.build();
    }

    /* from Bezkoder
    @Configuration
public class WebSecurityConfig {

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.csrf(csrf -> csrf.disable())
        .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(auth -> auth.requestMatchers("/api/auth/**").permitAll()
        .requestMatchers("/api/test/**").permitAll()
        .anyRequest().authenticated());

    // http....;

    return http.build();
  }
  */
    /* from Baeldung:
    @Bean
public AuthenticationManager authenticationManager(HttpSecurity http, BCryptPasswordEncoder bCryptPasswordEncoder, UserDetailService userDetailService)
  throws Exception {
    return http.getSharedObject(AuthenticationManagerBuilder.class)
      .userDetailsService(userDetailsService)
      .passwordEncoder(bCryptPasswordEncoder)
      .and()
      .build();
}
     */

    // https://www.bezkoder.com/websecurityconfigureradapter-deprecated-spring-boot/ using configure(AuthenticationManagerBuilder) is deprecated
    /* from Udemy course:
    @Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication()
			.withUser("matt").password("{noop}secret").authorities("ROLE_ADMIN")
			.and()
			.withUser("jane").password("{noop}secret").authorities("ROLE_USER");
		//warning!: This password should be encoded
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception { //deprecated!! Instead of using override method configure(HttpSecurity http), we declare SecurityFilterChain bean.
		http
			.csrf().disable()
			.authorizeRequests()
			.antMatchers(HttpMethod.OPTIONS, "/api/basicAuth/**").permitAll()
			.antMatchers("/api/basicAuth/**").hasAnyRole("ADMIN","USER")
			.and().httpBasic();

		http
			.csrf().disable()
			.authorizeRequests()
			.antMatchers(HttpMethod.OPTIONS, "/api/**").permitAll()
			.antMatchers(HttpMethod.GET, "/api/bookings/**").permitAll()
			.antMatchers(HttpMethod.GET, "/api/**").hasAnyRole("ADMIN", "USER")
			.antMatchers("/api/**").hasRole("ADMIN")
			.and()
			.addFilter(new JWTAuthorizationFilter(authenticationManager()));

	}
     */

    /*
    z chat openai:
    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserDetailsService)
            .passwordEncoder(passwordEncoder());
    }
     */
}
