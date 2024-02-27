package pl.joannaz.culturalcentrewieliszew.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
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
import pl.joannaz.culturalcentrewieliszew.user.Role;
import pl.joannaz.culturalcentrewieliszew.user.User;
import pl.joannaz.culturalcentrewieliszew.user.UserDTO;
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

    @PostMapping(path="/login") // authenticate user
    public UserDTO login (@RequestBody Map<String,String> credentials, HttpServletResponse response) {
        //credentials - LinkedHashMap, keys: username and password
        Authentication authentication = authManager
                .authenticate(new UsernamePasswordAuthenticationToken(credentials.get("username"), credentials.get("password")));

        SecurityContext sc = SecurityContextHolder.getContext();
                sc.setAuthentication(authentication);

        User currentUser = (User) authentication.getPrincipal();
        // or just (against these three lines above): User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String username = currentUser.getUsername();
        //String role = currentUser.getAuthorities().toArray()[0].toString().substring(5);

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

        return new UserDTO(currentUser);
        // WITHOUT SECURITY CONTEXT AND WITHOUT JWT
       /*
        Optional<User> user = userService.findUserByUsername(credentials.get("username"));
        if (user.isPresent()) {
            if (passwordEncoder.matches(credentials.get("password"), user.get().getPassword())) {
                System.out.println("Password is correct");
                return new UserDTO(user.get());
                // Passwords match, proceed with authentication
                // Create an Authentication object, set it in the SecurityContext, etc.
            } else {
                System.out.println("Password is incorrect");
                return null;
                // Error: Passwords do not match, handle unsuccessful login
            }
        }
        // Error: User not found, handle unsuccessful login.
        System.out.println("User not found. Please, try again with other username.");
        return null;
        //return credentials;
        */
    }

    @PostMapping(path="/signup") // id=null
    public UserDTO signup (@RequestBody User newUser) {
        if (userService.existsByUsername(newUser.getUsername())) {
            throw new Error("Username already exists.");
            //return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
        }
        newUser.setId(UUID.randomUUID()); //is it needed?
        String encodedPassword = passwordEncoder.encode(newUser.getPassword());
        newUser.setPassword(encodedPassword);
        userService.addUser(newUser);
        return new UserDTO(newUser);
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
        Collection<? extends GrantedAuthority> roles = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        String role = ""; // if user is not authenticated, it has no role, so this method's response will be this empty string
        if (!roles.isEmpty()) {
            SimpleGrantedAuthority sga = (SimpleGrantedAuthority) roles.iterator().next();
            String authRole = sga.getAuthority().substring(5);
            if (!authRole.equals("ANONYMOUS")) role = authRole; // when authentication is instanceof AnonymousAuthenticationToken
        }
        Map<String, String> response = new HashMap<>(1);
        response.put("role", role);
        return response;
    }

    @GetMapping("/status")
    public Map<String,Boolean> getIsAuthenticated() {
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
