package pl.joannaz.culturalcentrewieliszew.test;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(path = "/api/test")
@Configuration
@EnableMethodSecurity
public class TestController {

    @GetMapping("/client")
    @PreAuthorize("hasRole('CLIENT')") // it looks like because lacking annotations @Configuration and @EnableMethodSecurity, it enabled ADMIN and EMPLOYEE to get access to this endpoint
    public Map<String, String> getClientDashboard() {
        Map<String,String> results = new HashMap<>();
        results.put("text", "client dashboard");
        return results;
    }

    @GetMapping("/employee")
    public Map<String, String> getEmployeeDashboard() {
        Map<String,String> results = new HashMap<>();
        results.put("text", "employee dashboard");
        return results;
    }

    @GetMapping("/admin")
    public Map<String, String> getAdminDashboard() {
        Map<String,String> results = new HashMap<>();
        results.put("text", "admin dashboard");
        return results;
    }
}
