package pl.joannaz.culturalcentrewieliszew.utils;

import java.text.Normalizer;
import java.util.regex.Pattern;

public final class Utility {
    private Utility() {}

    public static String removeDiacritics(String input) {
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFKD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        String inputWithoutDiacritics = pattern.matcher(normalized).replaceAll("");
        // Check if inputNormalized contains 'ł' or 'Ł' characters before replacing
        // (cause: code above does not work for these letters)
        if (inputWithoutDiacritics.contains("ł") || inputWithoutDiacritics.contains("Ł")) {
            inputWithoutDiacritics = inputWithoutDiacritics.replace('ł', 'l').replace('Ł', 'L');
        }
        return inputWithoutDiacritics;
    }
}
