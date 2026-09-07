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
 * Klasa odpowiedzialna za generowanie i zapisywanie raportów do plików na dysku.
 * <p>
 * Odpowiada za dobór odpowiedniego formatera ({@link Formatter}), zbudowanie zawartości
 * raportu poprzez {@link ReportBuilder} oraz fizyczny zapis wygenerowanego ciągu znaków do wskazanego pliku.
 * </p>
 *
 * @author Sławek Reinmar
 * @version 1.0
 */
public class ReportWriter {

    /**
     * Domyślny konstruktor klasy zapisującej raporty.
     */
    public ReportWriter() {
    }

    /**
     * Dostępne formaty wyjściowe plików raportów.
     */
    public enum Format {
        /** Format wartości rozdzielanych przecinkami (CSV). */
        CSV,
        /** Format czystego tekstu (TXT). */
        TXT,
        /** Format strukturalny JSON. */
        JSON,
        /** Format strukturalny XML. */
        XML
    }

    /**
     * Generuje treść raportu w wybranym formacie i zapisuje go pod wskazaną ścieżką.
     *
     * @param outputPath ścieżka docelowa {@link Path} dla pliku raportu
     * @param type       typ i zakres generowanego raportu ({@link ReportType})
     * @param stats      podstawowe statystyki ilościowe tekstu ({@link TextStats})
     * @param freq       mapa częstotliwości występowania słów
     * @param format     format wyjściowy raportu ({@link Format})
     * @throws RuntimeException jeśli wystąpi błąd podczas budowania lub zapisu raportu
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
     * Zwraca odpowiednią instancję formatera ({@link Formatter}) dla wskazanego formatu wyjściowego.
     *
     * @param format żądany format wyjściowy ({@link Format})
     * @return instancja odpowiedniej klasy implementującej {@link Formatter}
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
     * Tworzy niezbędne katalogi nadrzędne i zapisuje podany ciąg znaków do pliku w kodowaniu UTF-8.
     *
     * @param path    ścieżka docelowa {@link Path} do pliku
     * @param content zawartość tekstowa do zapisania
     * @throws IOException jeśli wystąpi błąd wejścia/wyjścia podczas tworzenia katalogów lub zapisu pliku
     */
    public static void write(Path path, String content) throws IOException {
        Files.createDirectories(path.getParent());
        Files.writeString(path, content);
    }
}