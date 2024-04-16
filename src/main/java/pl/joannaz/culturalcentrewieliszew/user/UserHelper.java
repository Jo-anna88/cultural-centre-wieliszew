package pl.joannaz.culturalcentrewieliszew.user;

import pl.joannaz.culturalcentrewieliszew.utils.Utility;

public class UserHelper {
    protected static String createEmployeeUsername(String firstName, String lastName) {
        StringBuilder str = new StringBuilder(Utility.removeDiacritics(firstName.toLowerCase()));
        str.append(".");
        str.append(Utility.removeDiacritics(lastName.toLowerCase()));
        str.append("@ccw.pl");
        return str.toString();
    }

    protected static String createChildUsername(String parentUsername, String firstName, String lastName) {
        StringBuilder str = new StringBuilder(parentUsername);
        str.append("/");
        str.append(Utility.removeDiacritics(firstName));
        str.append(Utility.removeDiacritics(lastName));
        return str.toString();
    }

    protected static String updateChildUsername(String oldUsername, String firstName, String lastName) {
        String parentUsername = oldUsername.split("/")[0];
        return UserHelper.createChildUsername(parentUsername, firstName, lastName);
    }

    protected static boolean isFemale(String firstName) {
        return firstName.endsWith("a");
    }

    protected static String generateChildHeadshotValue(String firstName) {
        return UserHelper.isFemale(firstName) ? "assets/images/avatar-girl.svg" : "assets/images/avatar-boy.svg";
    }

    protected static User createEmployee(UserDTO userDTO) {
        return new User(
                userDTO.getFirstName(),
                userDTO.getLastName(),
                userDTO.getPhone(),
                String.valueOf(userDTO.getDob()),
                userDTO.getRole(),
                userDTO.getPosition(),
                userDTO.getDescription()
        );
    }

    protected static User createAdmin(UserDTO userDTO) {
        return UserHelper.createEmployee(userDTO);
    }
}