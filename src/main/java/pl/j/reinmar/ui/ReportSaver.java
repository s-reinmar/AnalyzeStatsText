package pl.j.reinmar.ui;

import pl.j.reinmar.core.TextAnalyzer;
import pl.j.reinmar.core.WordAnalysisMode;
import pl.j.reinmar.io.ReportWriter;
import pl.j.reinmar.io.builder.ReportBuilder;
import pl.j.reinmar.io.builder.ReportType;
import pl.j.reinmar.model.TextStats;
import pl.j.reinmar.model.WordSort;

import java.nio.file.Path;
import java.util.Map;
import java.util.Set;

/**
 * Klasa odpowiedzialna za orkiestrację procesu tworzenia raportów:
 * - pobiera dane z TextAnalyzer,
 * - deleguje budowanie treści do ReportBuilder,
 * - deleguje zapis do ReportWriter.
 */
public class ReportSaver {

    private final TextAnalyzer analyzer;

    public ReportSaver(TextAnalyzer analyzer) {
        this.analyzer = analyzer;
    }

    /**
     * Tworzy i zapisuje raport do pliku.
     *
     * @param outputPath   ścieżka docelowa
     * @param inputPath    ścieżka do pliku wejściowego
     * @param type         typ raportu (BASIC, FULL, FREQUENCY)
     * @param stopWords    zbiór stop-words
     * @param minWordLength minimalna długość słowa
     * @param sortMode     tryb sortowania słów
     * @param topN         liczba słów dla TOP_WORDS (ignorowane dla innych typów)
     * @param format       format raportu (CSV, TXT, JSON, XML)
     */
    public void saveReport(Path outputPath,
                           String inputPath,
                           ReportType type,
                           Set<String> stopWords,
                           int minWordLength,
                           WordSort sortMode,
                           int topN,
                           ReportWriter.Format format) {

        try {
            // 1. Statystyki
            TextStats stats = analyzer.analyzeFile(inputPath);

            // 2. Częstotliwości słów (zawsze potrzebne)
            @SuppressWarnings("unchecked")
            Map<String, Integer> freq = (Map<String, Integer>) analyzer.analyzeWordsFromFile(
                    inputPath,
                    stopWords,
                    minWordLength,
                    sortMode,
                    topN,
                    WordAnalysisMode.FREQUENCY_MAP
            );

            // 3. Budowanie treści raportu
            String content = ReportBuilder.build(type, stats, freq, ReportWriter.formatter(format));

            // 4. Zapis do pliku
            ReportWriter.write(outputPath, content);

            System.out.println("✔ Raport zapisany: " + outputPath);

        } catch (Exception e) {
            System.err.println("❌ Błąd zapisu raportu: " + e.getMessage());
        }
    }
}