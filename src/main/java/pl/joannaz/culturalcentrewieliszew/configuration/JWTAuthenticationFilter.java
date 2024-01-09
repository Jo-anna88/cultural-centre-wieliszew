//package pl.joannaz.culturalcentrewieliszew.configuration;
//
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.RequiredArgsConstructor;
//import org.springframework.lang.NonNull;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//
//@Component
//@RequiredArgsConstructor // constructor for private final fields
//public class JWTAuthenticationFilter extends OncePerRequestFilter { //extends BasicAuthenticationFilter (BAF extends OncePerRequestFilter)
//    private final JWTService jwtService;
//
//    @Override
//    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
//       final String authHeader = request.getHeader("Authorization");
//       final String jwt;
//       final String userEmail;
//       if(authHeader == null || !authHeader.startsWith("Bearer ")){
//           filterChain.doFilter(request, response);
//           return;
//       }
//       jwt = authHeader.substring(7); //after 'Bearer '
//       userEmail = jwtService.extractUsername(jwt);//extract it from jwt
//    }
//    //it should be fired every time there is a request to DB, that's why we use OncePerRequestFilter
//    //we can use it e.g. for add additional headers
//}
