package pl.joannaz.culturalcentrewieliszew.user;

import lombok.Data;

import java.util.UUID;

@Data
public class User { // model
    private final UUID uuid;
    private final String name;
    private final String login; // e.g. email
}
