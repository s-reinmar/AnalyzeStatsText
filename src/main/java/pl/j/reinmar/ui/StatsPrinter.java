package pl.j.reinmar.ui;

import pl.j.reinmar.core.TextAnalyzer;
import pl.j.reinmar.model.TextStats;
import pl.j.reinmar.model.WordCount;
import pl.j.reinmar.model.WordSort;

import java.util.*;

public class StatsPrinter {

    // ===================== PODSTAWOWE STATYSTYKI =====================

    public void printBasic(TextAnalyzer analyzer, String path) {
        try {
            TextStats stats = analyzer.analyzeFile(path);

            System.out.println("=== STATYSTYKI ===");
            System.out.println("Słowa: " + stats.words());
            System.out.println("Znaki (ze spacjami): " + stats.charsWithSpaces());
            System.out.println("Znaki (bez spacji): " + stats.charsWithoutSpaces());
            System.out.println("Zdania: " + stats.sentences());

        } catch (Exception e) {
            System.err.println("Błąd odczytu pliku: " + e.getMessage());
        }
    }

    // ===================== TOP N SŁÓW =====================

    public void printTop(TextAnalyzer analyzer,
                         String path,
                         int n,
                         Set<String> stopWords,
                         int minWordLength,
                         WordSort sortMode) {

        try {
            List<WordCount> top = analyzer.topWordsFromFile(
                    path,
                    n,
                    stopWords != null && !stopWords.isEmpty() ? stopWords : null,
                    minWordLength,
                    sortMode
            );

            System.out.println("=== TOP " + n + " słów — sortowanie: " + sortMode + " ===");
            for (WordCount wc : top) {
                System.out.printf("%-20s : %d%n", wc.word(), wc.count());
            }

        } catch (Exception e) {
            System.err.println("Błąd odczytu pliku: " + e.getMessage());
        }
    }

    // ===================== FRAGMENT CZĘSTOTLIWOŚCI =====================

    public void printFrequencyFragment(TextAnalyzer analyzer,
                                       String path,
                                       Set<String> stopWords,
                                       int minWordLength) {

        try {
            Map<String, Integer> freq = analyzer.wordFrequencyFromFile(
                    path,
                    stopWords != null && !stopWords.isEmpty() ? stopWords : null,
                    minWordLength
            );

            List<Map.Entry<String, Integer>> sorted = new ArrayList<>(freq.entrySet());
            sorted.sort(Map.Entry.<String, Integer>comparingByValue().reversed()
                    .thenComparing(Map.Entry::getKey));

            int limit = Math.min(50, sorted.size());
            System.out.println("=== Częstotliwości (pierwsze " + limit + ") ===");

            for (int i = 0; i < limit; i++) {
                var e = sorted.get(i);
                System.out.printf("%-20s : %d%n", e.getKey(), e.getValue());
            }

            if (sorted.size() > limit) {
                System.out.println("... (razem: " + sorted.size() + ")");
            }

        } catch (Exception e) {
            System.err.println("Błąd odczytu pliku: " + e.getMessage());
        }
    }
}