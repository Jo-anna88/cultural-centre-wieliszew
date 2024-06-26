package pl.joannaz.culturalcentrewieliszew.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeProfile { // DTO
    private UUID id;
    private String firstName;
    private String lastName;
    private String headshot;
    private String position;
    private String description;
}
