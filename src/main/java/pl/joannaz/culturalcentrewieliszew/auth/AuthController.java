package pl.joannaz.culturalcentrewieliszew.auth;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import pl.joannaz.culturalcentrewieliszew.security.jwt.JWTService;
import pl.joannaz.culturalcentrewieliszew.user.User;
import pl.joannaz.culturalcentrewieliszew.user.UserBasicInfo;
import pl.joannaz.culturalcentrewieliszew.user.UserService;

import java.util.*;

@RestController
@RequestMapping(path = "/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationManager authManager;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final JWTService jwtService;
    @Value("${jwtCookieName}")
    private String jwtCookieName; // = "jwt";
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @PostMapping(path="/login") // authenticate user
    public UserBasicInfo login (@RequestBody Map<String,String> credentials, HttpServletResponse response) {
        //credentials - LinkedHashMap, keys: username and password
        logger.info("Fetching user from Security Context Holder");
        Authentication authentication = authManager
                .authenticate(new UsernamePasswordAuthenticationToken(credentials.get("username"), credentials.get("password")));
        SecurityContext sc = SecurityContextHolder.getContext();
                sc.setAuthentication(authentication);
        User currentUser = (User) authentication.getPrincipal();

        String username = currentUser.getUsername();

        String token = jwtService.generateToken(username);

//        ResponseCookie jwtCookie = ResponseCookie.from(jwtCookieName, token)
//                .path("/api")
//                .maxAge(jwtService.getExpTimeMs()/1000) //the same as expiration time for jwt but in seconds against milliseconds
//                .httpOnly(true)
//                .build();
//
//        ResponseEntity responseEntity = ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
//                .body(new UserDTO(currentUser));
//
//        return responseEntity;

        response.addCookie(jwtService.generateJwtCookie(jwtCookieName, token));

        return new UserBasicInfo(null, currentUser.getFirstName(), currentUser.getLastName());
    }

    @PostMapping(path="/signup") // id=null
    public void signup (@RequestBody User newUser) {
        logger.info("Validating new user's username: {} for uniqueness constraint", newUser.getUsername());
        if (userService.existsByUsername(newUser.getUsername())) {
            logger.error("Username with username {} already exists.", newUser.getUsername());
            throw new Error("Username already exists.");
            //return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
        }

        // Set password
        logger.debug("Encoding user's password: {}", newUser.getPassword());
        String encodedPassword = passwordEncoder.encode(newUser.getPassword());
        logger.debug("Encoded password: {}", encodedPassword);
        newUser.setPassword(encodedPassword);

        logger.info("Saving new user with username {} to the database", newUser.getUsername());
        userService.addUser(newUser);
    }

    @PostMapping(path="/logout")
    public void logout(HttpServletResponse response) {
        response.addCookie(jwtService.cleanJwtCookie(jwtCookieName));
        /*
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new MessageResponse("You've been signed out!"));
         */
        SecurityContextHolder.getContext().setAuthentication(null); // z Udemy, brak na bezkoder
    }

    @GetMapping("/role")
    public Map<String,String> getUserRole() {
        logger.info("Checking whether user is logged in user and what is the user's role.");
        String role = ""; // if user is not authenticated, it has no role, so this method's response will be this empty string
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (! (authentication instanceof AnonymousAuthenticationToken)) { // UsernamePasswordAuthenticationToken
            Collection<? extends GrantedAuthority> roles = authentication.getAuthorities();
            SimpleGrantedAuthority sga = (SimpleGrantedAuthority) roles.iterator().next();
            role = sga.getAuthority().substring(5);
        }
        logger.info("User's role: {}", role);

        Map<String, String> response = new HashMap<>(1);
        response.put("role", role);
        return response;
    }

    @GetMapping("/status")
    public Map<String,Boolean> getIsAuthenticated() {
        logger.info("Checking is user is an authenticated user.");
        Map<String,Boolean> response = new HashMap<>(1);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() != "anonymousUser")
            {
                response.put("result", authentication.isAuthenticated());
                return response;
            }
        response.put("result", false);
        return response;
    }
}
