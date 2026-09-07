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
 * Komponent odpowiedzialny za koordynację procesu generowania oraz zapisu raportów z analizy tekstu do pliku.
 * <p>
 * Klasa spaja funkcjonalności silnika analitycznego ({@link TextAnalyzer}), budowniczego treści ({@link ReportBuilder})
 * oraz modułu zapisu ({@link ReportWriter}), automatyzując pobieranie statystyk, filtrowanie słów i formatowanie wyjścia.
 * </p>
 *
 * @author Sławek Reinmar
 * @version 1.0
 */
public class ReportSaver {

    /** Silnik analityczny wykorzystywany do przetwarzania plików tekstowych. */
    private final TextAnalyzer analyzer;

    /**
     * Tworzy nową instancję komponentu zapisującego raporty.
     *
     * @param analyzer silnik analityczny {@link TextAnalyzer} do przeprowadzania analizy plików
     */
    public ReportSaver(TextAnalyzer analyzer) {
        this.analyzer = analyzer;
    }

    /**
     * Przeprowadza pełny proces generowania oraz zapisu raportu na podstawie podanych parametrów.
     * <p>
     * Metoda pobiera statystyki podstawowe oraz mapę częstotliwości słów z wybranego pliku wejściowego,
     * aplikuje kryteria filtrowania (stop-words, minimalna długość słowa) i sortowania, a następnie
     * buduje oraz zapisuje raport pod wskazaną ścieżką w wybranym formacie wyjściowym.
     * Wszelkie błędy odczytu lub zapisu są wyłapywane i wypisywane na strumień błędów.
     * </p>
     *
     * @param outputPath    docelowa ścieżka {@link Path} do zapisywanego pliku raportu
     * @param inputPath     ścieżka do pliku źródłowego z tekstem do analizy
     * @param type          typ i zakres generowanego raportu ({@link ReportType})
     * @param stopWords     zbiór słów ignorowanych lub {@code null}, jeśli brak filtrowania
     * @param minWordLength minimalna długość uwzględnianych słów
     * @param sortMode      tryb sortowania wyników ({@link WordSort})
     * @param topN          maksymalna liczba słów (używana w odpowiednich trybach analizy)
     * @param format        format wyjściowy pliku ({@link ReportWriter.Format})
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