package pl.joannaz.culturalcentrewieliszew.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class RestSecurityConfig { // import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter - removed, because it was deprecated
    @Bean //?
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // against method reference we could use lambda: (csrf -> csrf.disable())
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers(HttpMethod.GET, "/api/classes/**")
//                        .hasRole("CLIENT")
//                )
        ;
//                .authorizeHttpRequests((authz) -> authz
//                        .anyRequest().authenticated()
//                )
//                .httpBasic(withDefaults());
        return http.build();
    }

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

    /* from Udemy course:
    @Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication()
			.withUser("matt").password("{noop}secret").authorities("ROLE_ADMIN")
			.and()
			.withUser("jane").password("{noop}secret").authorities("ROLE_USER");
		//TODO: This password should be encoded
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
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
