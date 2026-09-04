package pl.j.reinmar.core;

import pl.j.reinmar.io.FileReader;
import pl.j.reinmar.model.TextStats;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

/**
 * TextAnalyzer -> serce core/ scala wykorzystanie interfejsów i ich implementacji
 */

public class TextAnalyzer {
    private final Normalizer normalizer;
    private final Tokenizer tokenizer;

    public TextAnalyzer(Normalizer normalizer, Tokenizer tokenizer) {
        this.normalizer = Objects.requireNonNull(normalizer);
        this.tokenizer = Objects.requireNonNull(tokenizer);
    }


    /** Analiza tekstu (String) */
    public TextStats analyze(String text) {
        String original = Objects.requireNonNullElse(text, "");
        int charsWithSpaces = original.length();
        int charsWithoutSpaces = original.replaceAll("\\s+", "").length();

        // Normalizacja + tokenizacja
        String normalized = normalizer.normalize(original);
        List<String> words = tokenizer.words(normalized);

        return new TextStats(charsWithSpaces, charsWithoutSpaces, words.size());
    }


    /** Analiza pliku (delegacja do FileReader) */
    public TextStats analyzeFile(String path) throws IOException {
        String content = FileReader.readResource(path); // wywołanie static funkcji czytania pliku
        return analyze(content);
    }

}