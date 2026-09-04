package pl.j.reinmar.core;

import pl.j.reinmar.io.FileReader;
import pl.j.reinmar.model.TextStats;
import pl.j.reinmar.model.WordCount;

import java.io.IOException;
import java.util.*; // Object, List, Map etc.
import java.util.stream.Collectors;

/**
 * TextAnalyzer -> serce core/ scala wykorzystanie interfejsów i ich implementacji
 */

public class TextAnalyzer {
    private final Normalizer normalizer;
    private final Tokenizer tokenizer;
    private final SentenceTokenizer sentenceTokenizer;

    /** Konstruktor - wywołujący normalizera i tokenizery */
    public TextAnalyzer(Normalizer normalizer,
                        Tokenizer tokenizer,
                        SentenceTokenizer sentenceTokenizer) {
        this.normalizer = Objects.requireNonNull(normalizer, "normalizer must not be null");
        this.tokenizer = Objects.requireNonNull(tokenizer, "tokenizer must not be null");
        this.sentenceTokenizer = Objects.requireNonNull(sentenceTokenizer, "sentenceTokenizer must not be null");
    }

    /** metoda analizująca */
    public TextStats analyze (String text){
        String original = Objects.requireNonNullElse(text, "");

        int charsWithSpaces = original.length();
        int charsWithoutSpaces = original.replaceAll("\\s+", "").length();

        // Normalizacja + tokenizacja
        String normalized = normalizer.normalize(original);
        List<String> words = tokenizer.words(normalized);
        List<String> sentences = sentenceTokenizer.sentences(original);

        return new TextStats(charsWithSpaces, charsWithoutSpaces, words.size(), sentences.size());
    }

    /** Analiza pliku (delegacja do FileReader) */
    public TextStats analyzeFile (String path){
        String content = FileReader.readResource(path); // wywołanie static funkcji czytania pliku
        return analyze(content);
    }

    // ====== NOWE: częstotliwości słów ======

    /** Pełna mapa częstotliwości (po normalizacji), z opcjonalnymi stop‑words i minimalną długością słowa. */
    public Map<String, Integer> wordFrequencyFromText(String text,
                                                      Set<String> stopWords,
                                                      int minWordLength) {
        String normalized = normalizer.normalize(Objects.requireNonNullElse(text, ""));
        List<String> words = tokenizer.words(normalized);

        Map<String, Integer> freq = new HashMap<>();
        for (String w : words) {
            if ((stopWords == null || !stopWords.contains(w))
                    && w.length() >= Math.max(1, minWordLength)) {
                freq.merge(w, 1, Integer::sum);
            }
        }
        return freq;
    }

    /** Zwraca listę top N słów posortowaną po liczbie wystąpień malejąco, przy remisie alfabetycznie. */
    public List<WordCount> topWordsFromText(String text,
                                            int topN,
                                            Set<String> stopWords,
                                            int minWordLength) {
        Map<String, Integer> freq = wordFrequencyFromText(text, stopWords, minWordLength);

        Comparator<Map.Entry<String, Integer>> cmp =
                Map.Entry.<String, Integer>comparingByValue().reversed()
                        .thenComparing(Map.Entry::getKey);

        return freq.entrySet().stream()
                .sorted(cmp)
                .limit(Math.max(1, topN))
                .map(e -> new WordCount(e.getKey(), e.getValue()))
                .collect(Collectors.toList());
    }

    /** Wersje plikowe (delegują do readFileToString). */
    public Map<String, Integer> wordFrequencyFromFile(String path,
                                                      Set<String> stopWords,
                                                      int minWordLength) throws IOException {
        String content = FileReader.readResource(path);
        return wordFrequencyFromText(content, stopWords, minWordLength);
    }

    public List<WordCount> topWordsFromFile(String path,
                                            int topN,
                                            Set<String> stopWords,
                                            int minWordLength) throws java.io.IOException {
        String content = FileReader.readResource(path);
        return topWordsFromText(content, topN, stopWords, minWordLength);
    }

}