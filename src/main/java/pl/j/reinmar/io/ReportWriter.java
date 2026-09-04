package pl.j.reinmar.io;

import pl.j.reinmar.io.builder.ReportBuilder;
import pl.j.reinmar.io.builder.ReportType;
import pl.j.reinmar.io.format.*;
import pl.j.reinmar.model.TextStats;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

/**
 * Klasa odpowiedzialna za zapis gotowych raportów do plików.
 * Nie analizuje tekstu i nie buduje treści raportu — jedynie
 * deleguje formatowanie do odpowiedniego Formattera oraz
 * zapisuje wynik na dysku.
 */
public class ReportWriter {

    /**
     * Obsługiwane formaty raportów.
     */
    public enum Format {
        CSV, TXT, JSON, XML
    }

    /**
     * Zapisuje raport do pliku.
     *
     * @param outputPath ścieżka docelowa
     * @param type       typ raportu (BASIC, FULL, FREQUENCY)
     * @param stats      statystyki tekstu
     * @param freq       mapa częstotliwości słów
     * @param format     format raportu
     */
    public static void writeReport(Path outputPath,
                                   ReportType type,
                                   TextStats stats,
                                   Map<String, Integer> freq,
                                   Format format) {

        try {
            Formatter formatter = formatter(format);

            String content = ReportBuilder.build(
                    type,
                    stats,
                    freq,
                    formatter
            );

            write(outputPath, content);

        } catch (Exception e) {
            throw new RuntimeException("Nie udało się zapisać raportu: " + e.getMessage(), e);
        }
    }

    /**
     * Zwraca odpowiedni formatter na podstawie formatu.
     */
    public static Formatter formatter(Format format) {
        return switch (format) {
            case CSV -> new CsvFormatter();
            case TXT -> new TxtFormatter();
            case JSON -> new JsonFormatter();
            case XML -> new XmlFormatter();
        };
    }

    /**
     * Zapisuje treść do pliku, tworząc katalogi jeśli trzeba.
     */
    public static void write(Path path, String content) throws IOException {
        Files.createDirectories(path.getParent());
        Files.writeString(path, content);
    }
}