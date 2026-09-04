package pl.j.reinmar.model;

public record TextStats (
        int charsWithSpaces,
        int charsWithoutSpaces,
        int words,
        int sentences
) {

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