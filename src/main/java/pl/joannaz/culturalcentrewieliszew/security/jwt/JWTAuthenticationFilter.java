package pl.joannaz.culturalcentrewieliszew.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor // constructor for private final fields
public class JWTAuthenticationFilter extends OncePerRequestFilter { //extends BasicAuthenticationFilter (BAF extends OncePerRequestFilter)
    private final JWTService jwtService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain chain) throws ServletException, IOException {

        Cookie[] cookies = request.getCookies();
        if (cookies == null || cookies.length == 0) {
            chain.doFilter(request, response);
            return;
        }

        Cookie tokenCookie = null;
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("token")) {
                tokenCookie = cookie;
            }
        }

        if (tokenCookie == null) {
            chain.doFilter(request, response);
            return;
        }

        if (jwtService == null) {
            ServletContext servletContext = request.getServletContext();
            WebApplicationContext wac  = WebApplicationContextUtils.getWebApplicationContext(servletContext);
          //  jwtService = wac.getBean(JWTService.class);
        }

        //UsernamePasswordAuthenticationToken authentication = getAuthentication(tokenCookie.getValue());
        //SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(request, response);
    }
    //it should be fired every time there is a request to DB, that's why we use OncePerRequestFilter
    //we can use it e.g. for add additional headers
}
