package pl.j.reinmar.app;

public class TextApp {

    /**
     * countWords -> dla tekstu wykonuje zamianę wszystkich znaków interpunkcyjnych na spacje
     * oraz usuwa spację
     * @param text
     * @return ilość słów
     */
    private static int countWords(String text) {
        if (text == null) return 0;
        String trimmed = text.trim();
        if (trimmed.isEmpty()) return 0;

        trimmed = trimmed.replaceAll("[\\p{Punct}„”»«]", " ");
        String[] parts = trimmed.split("\\s+");
        return parts.length;
    }

    /**
     * Liczy znaki ze spacjami
     * @param text
     * @return ilość znaków ze spacjami
     */
    private static int countCharsWithSpaces(String text) {
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
    private static int countCharsWithoutSpaces(String text) {
        if (text == null) return 0;
        String withoutWhiteSpace = text.replaceAll("\\s+","");
        return withoutWhiteSpace.length();
    }

    public static void main(String[] args) {
        // Przykładowy tekst — później możesz podawać go z args lub Scannerem
        String text = "Ala,          ma kota! A               kot ma   Alę.\nTo jest test.";
        System.out.println("Tekst: " + text);
        System.out.println(" ");
        System.out.println(" ");
        System.out.println("Słowa: " + countWords(text));
        System.out.println("Znaki (ze spacjami): " + countCharsWithSpaces(text));
        System.out.println("Znaki (bez spacji): " + countCharsWithoutSpaces(text));
    }
}