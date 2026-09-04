package pl.j.reinmar.app;

import pl.j.reinmar.core.DefaultNormalizer;
import pl.j.reinmar.core.TextAnalyzer;
import pl.j.reinmar.core.WhitespaceTokenizer;
import pl.j.reinmar.model.TextStats;

public class TextApp {
    public static void main(String[] args) {
        // jeśli implementacje są publiczne:
        TextAnalyzer analyzer = new TextAnalyzer(new DefaultNormalizer(), new WhitespaceTokenizer());

        // analiza stringa
        TextStats stats = analyzer.analyze("To jest przykładowe zdanie.");
        System.out.println(stats);

        // analiza pliku z resources (obsługa IOException)
        try {
            TextStats fileStats = analyzer.analyzeFile("file.txt"); // ścieżka w resources
            System.out.println(fileStats);
        } catch (Exception e) {
            System.err.println("Błąd podczas czytania zasobu: " + e.getMessage());
        }
    }
}