package pl.joannaz.culturalcentrewieliszew.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CORSConfig implements WebMvcConfigurer {
    String devUrl = "http://localhost:4200";
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/classes/**")
                .allowedMethods("GET","POST","PUT","DELETE")
                .allowedOrigins(devUrl);                         // <- it could be an app's property to easy swap out from dev to production Url
    }
}
