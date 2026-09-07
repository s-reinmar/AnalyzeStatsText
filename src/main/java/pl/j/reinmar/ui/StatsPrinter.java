package pl.j.reinmar.ui;

import pl.j.reinmar.core.TextAnalyzer;
import pl.j.reinmar.core.WordAnalysisMode;
import pl.j.reinmar.model.TextStats;
import pl.j.reinmar.model.WordCount;
import pl.j.reinmar.model.WordSort;

import java.util.*;

/**
 * Komponent odpowiedzialny za formatowanie i drukowanie statystyk analizy tekstu na standardowe wyjście.
 * <p>
 * Klasa udostępnia metody wyjścia konsolowego przeznaczone do prezentacji statystyk podstawowych,
 * wykazów najczęstszych słów (Top N) oraz podglądu pełnych zestawień częstotliwościowych.
 * </p>

 * @author Sławek Reinmar
 * @version 1.0
 */
public class StatsPrinter {

    /**
     * Domyślny konstruktor klasy drukującej statystyki.
     */
    public StatsPrinter() {
    }

    /**
     * Wczytuje plik, oblicza i drukuje na standardowe wyjście podstawowe statystyki ilościowe.
     *
     * @param analyzer silnik analityczny {@link TextAnalyzer}
     * @param path     ścieżka do pliku źródłowego
     */
    public void printBasicStats(TextAnalyzer analyzer, String path) {
        try {
            TextStats stats = analyzer.analyzeFile(path);

            System.out.println("=== STATYSTYKI ===");
            System.out.println("Słowa: " + stats.words());
            System.out.println("Znaki (ze spacjami): " + stats.charsWithSpaces());
            System.out.println("Znaki (bez spacji): " + stats.charsWithoutSpaces());
            System.out.println("Zdania: " + stats.sentences());

        } catch (Exception e) {
            System.err.println("❌ Błąd odczytu pliku: " + e.getMessage());
        }
    }

    /**
     * Wczytuje plik, filtruje słowa i wyświetla listę ograniczoną do podanej liczby najczęstszych wyrazów (Top N).
     *
     * @param analyzer      silnik analityczny {@link TextAnalyzer}
     * @param path          ścieżka do pliku źródłowego
     * @param topN          liczba pozycji do wyświetlenia
     * @param stopWords     zbiór ignorowanych słów lub {@code null}
     * @param minWordLength minimalna długość uwzględnianych słów
     * @param sortMode      tryb sortowania wyników ({@link WordSort})
     */
    public void printTopWords(TextAnalyzer analyzer,
                              String path,
                              int topN,
                              Set<String> stopWords,
                              int minWordLength,
                              WordSort sortMode) {

        try {
            @SuppressWarnings("unchecked")
            List<WordCount> top = (List<WordCount>) analyzer.analyzeWordsFromFile(
                    path,
                    stopWords,
                    minWordLength,
                    sortMode,
                    topN,
                    WordAnalysisMode.TOP_WORDS
            );

            System.out.println("=== TOP " + topN + " słów — sortowanie: " + sortMode + " ===");
            top.forEach(wc -> System.out.printf(wc.word() + " : " + wc.count()));

        } catch (Exception e) {
            System.err.println("❌ Błąd odczytu pliku: " + e.getMessage());
        }
    }

    /**
     * Wczytuje plik i wyświetla skrócony podgląd (maksymalnie pierwsze 50 wpisów) zestawienia częstotliwości słów.
     *
     * @param analyzer      silnik analityczny {@link TextAnalyzer}
     * @param path          ścieżka do pliku źródłowego
     * @param stopWords     zbiór ignorowanych słów lub {@code null}
     * @param minWordLength minimalna długość uwzględnianych słów
     * @param sortMode      tryb sortowania wyników ({@link WordSort})
     */
    public void printFrequencyPreview(TextAnalyzer analyzer,
                                      String path,
                                      Set<String> stopWords,
                                      int minWordLength,
                                      WordSort sortMode) {

        try {
            @SuppressWarnings("unchecked")
            Map<String, Integer> freq = (Map<String, Integer>) analyzer.analyzeWordsFromFile(
                    path,
                    stopWords,
                    minWordLength,
                    sortMode,
                    0,
                    WordAnalysisMode.FREQUENCY_SORTED
            );

            List<Map.Entry<String, Integer>> sorted = new ArrayList<>(freq.entrySet());
            int limit = Math.min(50, sorted.size());

            System.out.println("=== Częstotliwości (pierwsze " + limit + ") ===");
            for (int i = 0; i < limit; i++) {
                var e = sorted.get(i);
                System.out.printf(e.getKey() + " : " + e.getValue());
            }

            if (sorted.size() > limit) {
                System.out.println("... (razem: " + sorted.size() + ")");
            }

        } catch (Exception e) {
            System.err.println("❌ Błąd odczytu pliku: " + e.getMessage());
        }
    }
}