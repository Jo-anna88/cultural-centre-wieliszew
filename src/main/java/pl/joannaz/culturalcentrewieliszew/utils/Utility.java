package pl.joannaz.culturalcentrewieliszew.utils;

import java.text.Normalizer;
import java.util.regex.Pattern;

public final class Utility {
    private Utility() {}

    public static String removeDiacritics(String input) {
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(normalized).replaceAll("");
    }
}
