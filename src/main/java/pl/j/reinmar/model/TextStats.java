package pl.j.reinmar.model;

/**
 * Rekord reprezentujący podstawowe statystyki ilościowe przeanalizowanego tekstu.
 * <p>
 * Przechowuje niemutowalne metryki tekstowe, takie jak całkowita liczba znaków
 * ze spacjami i bez nich, liczba wyodrębnionych słów oraz liczba zdań.
 * </p>
 *
 * @param charsWithSpaces    całkowita liczba znaków w tekście uwzględniająca białe znaki
 * @param charsWithoutSpaces liczba znaków po odrzuceniu wszystkich białych znaków
 * @param words              łączna liczba wyodrębnionych słów w tekście
 * @param sentences          łączna liczba wyodrębnionych zdań w tekście
 *
 * @author Sławek Reinmar
 * @version 1.0
 */
public record TextStats(
        int charsWithSpaces,
        int charsWithoutSpaces,
        int words,
        int sentences
) {

    /**
     * Zwraca czytelną dla człowieka reprezentację tekstową podsumowania statystyk.
     *
     * @return sformatowany blok tekstu zawierający poszczególne metryki
     */
    @Override
    public String toString() {
        return """
                === STATYSTYKI ===
                Słowa: %d
                Znaki (ze spacjami): %d
                Znaki (bez spacji): %d
                Zdania: %d
                """.formatted(words, charsWithSpaces, charsWithoutSpaces, sentences);
    }
}