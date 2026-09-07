package pl.j.reinmar.core;

import pl.j.reinmar.io.FileReader;
import pl.j.reinmar.model.TextStats;
import pl.j.reinmar.model.WordCount;
import pl.j.reinmar.model.WordSort;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Główny silnik analityczny odpowiedzialny za przetwarzanie tekstu oraz obliczanie statystyk.
 * <p>
 * Klasa integruje komponenty normalizacji ({@link Normalizer}), tokenizacji słów ({@link Tokenizer})
 * oraz tokenizacji zdań ({@link SentenceTokenizer}). Dostarcza metody do ogólnej analizy
 * ilościowej (liczba znaków, słów, zdań) oraz zaawansowanej analizy częstotliwości słów z uwzględnieniem
 * filtrowania (stop-words, minimalna długość) oraz sortowania.
 * </p>
 *
 * @author Sławek Reinmar
 * @version 1.0
 */
public class TextAnalyzer {

    /** Komponent do normalizacji tekstu wejściowego. */
    private final Normalizer normalizer;

    /** Komponent do podziału znormalizowanego tekstu na słowa. */
    private final Tokenizer tokenizer;

    /** Komponent do podziału surowego tekstu na zdania. */
    private final SentenceTokenizer sentenceTokenizer;

    /**
     * Tworzy nową instancję analizatora tekstu z wybranymi komponentami wykonawczymi.
     *
     * @param normalizer        komponent {@link Normalizer} usuwający interpunkcję i standaryzujący wielkość liter
     * @param tokenizer         komponent {@link Tokenizer} wyodrębniający poszczególne słowa
     * @param sentenceTokenizer komponent {@link SentenceTokenizer} dzielący tekst na zdania
     * @throws NullPointerException jeśli którykolwiek z przekazanych argumentów jest równy {@code null}
     */
    public TextAnalyzer(Normalizer normalizer,
                        Tokenizer tokenizer,
                        SentenceTokenizer sentenceTokenizer) {
        this.normalizer = Objects.requireNonNull(normalizer);
        this.tokenizer = Objects.requireNonNull(tokenizer);
        this.sentenceTokenizer = Objects.requireNonNull(sentenceTokenizer);
    }

    // ===================== STATYSTYKI =====================

    /**
     * Przeprowadza podstawową analizę ilościową podanego ciągu znaków.
     *
     * @param text surowy tekst wejściowy do przeanalizowania
     * @return obiekt {@link TextStats} zawierający zliczone znaki, słowa oraz zdania
     */
    public TextStats analyze(String text) {
        String original = Objects.requireNonNullElse(text, "");

        int charsWithSpaces = original.length();
        int charsWithoutSpaces = original.replaceAll("\\s+", "").length();

        String normalized = normalizer.normalize(original);
        List<String> words = tokenizer.words(normalized);
        List<String> sentences = sentenceTokenizer.sentences(original);

        return new TextStats(charsWithSpaces, charsWithoutSpaces, words.size(), sentences.size());
    }

    /**
     * Wczytuje plik z zasobów i przeprowadza jego podstawową analizę ilościową.
     *
     * @param path ścieżka do pliku źródłowego w zasobach
     * @return obiekt {@link TextStats} z wynikami analizy
     * @throws IOException jeśli wystąpi błąd odczytu pliku
     */
    public TextStats analyzeFile(String path) throws IOException {
        return analyze(FileReader.readResource(path));
    }

    // ===================== ANALIZA SŁÓW (NOWE API) =====================

    /**
     * Przeprowadza zaawansowaną analizę częstotliwości słów w tekście według podanych kryteriów i trybu.
     * <p>
     * Metoda normalizuje tekst, wyodrębnia słowa, a następnie nakłada filtry:
     * <ul>
     *     <li>odrzuca słowa znajdujące się w zbiorze {@code stopWords} (jeśli zbiór nie jest {@code null}),</li>
     *     <li>odrzuca słowa krótsze niż {@code minWordLength} (minimum 1 znak).</li>
     * </ul>
     * Zwracana struktura danych zależy od wybranego trybu {@link WordAnalysisMode}:
     * <ul>
     *     <li>{@link WordAnalysisMode#FREQUENCY_MAP} – nieposortowana mapa {@code Map<String, Integer>},</li>
     *     <li>{@link WordAnalysisMode#FREQUENCY_SORTED} – posortowana mapa {@code Map<String, Integer>},</li>
     *     <li>{@link WordAnalysisMode#TOP_WORDS} – ograniczona lista {@code List<WordCount>} (maksymalnie {@code topN} elementów),</li>
     *     <li>{@link WordAnalysisMode#ALL_WORDS_SORTED} – pełna posortowana lista {@code List<WordCount>}.</li>
     * </ul>
     * </p>
     *
     * @param text          tekst wejściowy do analizy
     * @param stopWords     zbiór słów ignorowanych lub {@code null}, jeśli brak filtrowania
     * @param minWordLength minimalna długość uwzględnianych słów
     * @param sortMode      tryb sortowania wyników ({@link WordSort})
     * @param topN          maksymalna liczba zwracanych słów (używana tylko w trybie {@link WordAnalysisMode#TOP_WORDS})
     * @param mode          tryb determinujący strukturę zwracanych danych ({@link WordAnalysisMode})
     * @return struktura danych ({@link Map} lub {@link List}) uzależniona od wybranego parametru {@code mode}
     */
    public Object analyzeWords(String text,
                               Set<String> stopWords,
                               int minWordLength,
                               WordSort sortMode,
                               int topN,
                               WordAnalysisMode mode) {

        String normalized = normalizer.normalize(Objects.requireNonNullElse(text, ""));
        List<String> words = tokenizer.words(normalized);

        Map<String, Integer> freq = words.stream()
                .filter(w -> stopWords == null || !stopWords.contains(w))
                .filter(w -> w.length() >= Math.max(1, minWordLength))
                .collect(Collectors.toMap(
                        w -> w,
                        w -> 1,
                        Integer::sum
                ));

        return switch (mode) {

            case FREQUENCY_MAP -> freq;

            case FREQUENCY_SORTED -> freq.entrySet().stream()
                    .map(e -> new WordCount(e.getKey(), e.getValue()))
                    .sorted(sortMode.comparator())
                    .collect(Collectors.toMap(
                            WordCount::word,
                            WordCount::count,
                            (a, b) -> a,
                            LinkedHashMap::new
                    ));

            case TOP_WORDS -> freq.entrySet().stream()
                    .map(e -> new WordCount(e.getKey(), e.getValue()))
                    .sorted(sortMode.comparator())
                    .limit(Math.max(1, topN))
                    .collect(Collectors.toList());

            case ALL_WORDS_SORTED -> freq.entrySet().stream()
                    .map(e -> new WordCount(e.getKey(), e.getValue()))
                    .sorted(sortMode.comparator())
                    .collect(Collectors.toList());
        };
    }

    /**
     * Wczytuje plik z zasobów i przeprowadza zaawansowaną analizę częstotliwości słów.
     *
     * @param path          ścieżka do pliku źródłowego w zasobach
     * @param stopWords     zbiór słów ignorowanych lub {@code null}
     * @param minWordLength minimalna długość uwzględnianych słów
     * @param sortMode      tryb sortowania wyników ({@link WordSort})
     * @param topN          maksymalna liczba zwracanych słów w trybie TOP_WORDS
     * @param mode          tryb wyjściowy struktury danych ({@link WordAnalysisMode})
     * @return struktura danych uzależniona od wybranego parametru {@code mode}
     * @throws IOException jeśli wystąpi błąd odczytu pliku
     */
    public Object analyzeWordsFromFile(String path,
                                       Set<String> stopWords,
                                       int minWordLength,
                                       WordSort sortMode,
                                       int topN,
                                       WordAnalysisMode mode) throws IOException {

        return analyzeWords(
                FileReader.readResource(path),
                stopWords,
                minWordLength,
                sortMode,
                topN,
                mode
        );
    }
}