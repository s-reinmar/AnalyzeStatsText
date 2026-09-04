package pl.j.reinmar.app;

import java.util.ArrayList;

public class TextMethod {

    /**
     * countWords -> dla tekstu wykonuje zamianę wszystkich znaków interpunkcyjnych na spacje
     * oraz usuwa spację
     * @param text
     * @return ilość słów
     */
    static int countWords(String text) {
        if (text == null) return 0;
        String trimmed = text.trim();
        if (trimmed.isEmpty()) return 0;

        // ignoruje interpunkcje
        trimmed = trimmed.replaceAll("[\\p{Punct}„”»«]", " ");

        // rozbija po białych znakach
        String[] parts = trimmed.split("\\s+");

        // przenoszenie do ArrayList
        ArrayList<String> words = new ArrayList<String>();
        for (String part : parts) {
            if(!part.isBlank()) {
                words.add(part);
            }
        }
        return words.size();
    }

    /**
     * Liczy znaki ze spacjami
     * @param text
     * @return ilość znaków ze spacjami
     */
    static int countCharsWithSpaces(String text) {
        if(text == null) {
            return 0;
        }
        return text.length();
    }

    /**
     * Liczy znaki bez spacji
     * @param text
     * @return ilość znaków bez spacji
     */
    static int countCharsWithoutSpaces(String text) {
        if (text == null) return 0;
        String withoutWhiteSpace = text.replaceAll("\\s+","");
        return withoutWhiteSpace.length();
    }

}