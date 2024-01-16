package pl.joannaz.culturalcentrewieliszew.security.jwt;

import io.jsonwebtoken.*;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.lookup.DataSourceLookupFailureException;
import org.springframework.stereotype.Service;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.text.DateFormat;
import java.time.LocalDate;
import java.util.Base64;
import java.util.Date;

@Service
public class JWTService {
    private static final Logger logger = LoggerFactory.getLogger(JWTService.class);
    RSAPrivateKey privateKey;
    RSAPublicKey publicKey;
    private long expTimeMs = 1800000; // 1800s, czyli 30 min. lub 86400000 ms, 86400 s, czyli 24h

    @PostConstruct
    private void initKeys() throws NoSuchAlgorithmException {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048);
        KeyPair keyPair = generator.genKeyPair();
        privateKey  = (RSAPrivateKey)keyPair.getPrivate();
        publicKey = (RSAPublicKey)keyPair.getPublic();
        // for testing/debugging purposes:
        String encodedPublicKey = Base64.getEncoder().encodeToString(publicKey.getEncoded());
        String encodedPrivateKey = Base64.getEncoder().encodeToString(privateKey.getEncoded());
    }
    public String generateToken(String username) {
        long currentTimeMs = System.currentTimeMillis();
        return Jwts.builder()
                .setIssuedAt(new Date(currentTimeMs))
                .setExpiration(new Date(currentTimeMs + expTimeMs))
                .claim("name", username)
                .signWith(privateKey, SignatureAlgorithm.RS256)
                .compact();
    }

    public Cookie generateJwtCookie(String cookieName, String cookieValue) {
        Cookie jwtCookieTest = new Cookie(cookieName, cookieValue);
        jwtCookieTest.setPath("/api");
        jwtCookieTest.setMaxAge((int) (expTimeMs/1000));
        jwtCookieTest.setHttpOnly(true);
        //jwtCookieTest.setSecure(true); // in production
        return jwtCookieTest;
    }

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

    public String extractUsernameFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(publicKey).build()
                .parseClaimsJws(token).getBody().get("name").toString();
    }


}
