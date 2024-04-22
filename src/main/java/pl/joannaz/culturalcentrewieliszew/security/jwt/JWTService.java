package pl.joannaz.culturalcentrewieliszew.security.jwt;

import io.jsonwebtoken.*;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.WebUtils;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Base64;
import java.util.Date;

@Service
public class JWTService {
    private static final Logger logger = LoggerFactory.getLogger(JWTService.class);
    RSAPrivateKey privateKey;
    RSAPublicKey publicKey;
    private long expTimeMs = 1800000; // 1800s, czyli 30 min. lub 86400000 ms, 86400 s, czyli 24h
    @Value("${jwtCookieName}")
    private String jwtCookieName;

    @PostConstruct
    private void initKeys() throws NoSuchAlgorithmException {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048);
        KeyPair keyPair = generator.genKeyPair();
        privateKey  = (RSAPrivateKey)keyPair.getPrivate();
        publicKey = (RSAPublicKey)keyPair.getPublic();
        // for testing/debugging purposes:
        String encodedPublicKey = Base64.getEncoder().encodeToString(publicKey.getEncoded());
        logger.info("Encoded public key: {}", encodedPublicKey);
        String encodedPrivateKey = Base64.getEncoder().encodeToString(privateKey.getEncoded());
        logger.info("Encoded private key: {}", encodedPrivateKey);
    }
    public String generateToken(String username) {
        logger.info("Generating JWT token");
        long currentTimeMs = System.currentTimeMillis();
        return Jwts.builder()
                .setIssuedAt(new Date(currentTimeMs))
                .setExpiration(new Date(currentTimeMs + expTimeMs))
                .claim("name", username)
                .signWith(privateKey, SignatureAlgorithm.RS256)
                .compact();
    }

    public Cookie generateJwtCookie(String cookieName, String cookieValue) {
        logger.info("Generating JWT cookie");
        Cookie jwtCookie = new Cookie(cookieName, cookieValue);
        jwtCookie.setPath("/api");
        jwtCookie.setMaxAge((int) (expTimeMs/1000));
        jwtCookie.setHttpOnly(true);
        //jwtCookie.setSecure(true); // in production
        return jwtCookie;
    }

    public Cookie cleanJwtCookie(String cookieName) {
        logger.info("Cleaning JWT cookie");
        Cookie jwtCookie = new Cookie(cookieName, null);
        jwtCookie.setPath("/api");
        jwtCookie.setMaxAge(0);
        jwtCookie.setHttpOnly(true);
        // jwtCookie.setSecure(true); // in production
        return jwtCookie;
    }

    // token validation from bezkoder:
    public boolean validateJwtToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(publicKey).build().parse(token);
            return true;
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }

    /* token validation from Udemy:
    public String validateToken(String token) throws JWTVerificationException {

        String encodedPayload = JWT.require(Algorithm.RSA256(publicKey, privateKey))
                .build()
                .verify(token)
                .getPayload();

        return new String(Base64.getDecoder().decode(encodedPayload));
    }
    */

    public String extractUsernameFromToken(String token) {
        logger.info("Extracting username from token: {}", token);
        return Jwts.parserBuilder().setSigningKey(publicKey).build()
                .parseClaimsJws(token).getBody().get("name").toString();
    }

    public String getJwtCookieValue(HttpServletRequest request) {
        logger.info("Getting JWT cookie value from request: {}", request);
        Cookie cookie = WebUtils.getCookie(request, jwtCookieName);
        return (cookie != null) ? cookie.getValue() : null;
    }
}
