package pl.joannaz.culturalcentrewieliszew.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserBasicInfo { // DTO
    private UUID id;
    private String firstName;
    private String lastName;
}
