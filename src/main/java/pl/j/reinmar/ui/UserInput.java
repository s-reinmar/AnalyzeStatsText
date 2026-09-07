package pl.j.reinmar.ui;

import pl.j.reinmar.io.ReportWriter;
import pl.j.reinmar.model.WordSort;

import java.nio.file.Path;
import java.util.Locale;
import java.util.Scanner;

/**
 * Konsolowa klasa pomocnicza odpowiedzialna za interakcję z użytkownikiem i pobieranie danych wejściowych.
 * <p>
 * Klasa wykorzystuje obiekt {@link Scanner} do odczytywania wyborów z wiersza poleceń (CLI).
 * Obsługuje pobieranie wartości liczbowych z obsługą wartości domyślnych (fallback),
 * wybór formatów raportów, trybów sortowania słów oraz bezpieczne budowanie ścieżek wyjściowych.
 * </p>
 *
 * @author Sławek Reinmar
 * @version 1.0
 */
public class UserInput {

    /** Obiekt skanera konsolowego służący do odczytywania strumienia wejściowego. */
    private final Scanner sc;

    /**
     * Tworzy nową instancję klasy pobierającej dane z podanego skanera.
     *
     * @param sc skaner konsolowy (np. opakowujący {@link System#in})
     */
    public UserInput(Scanner sc) {
        this.sc = sc;
    }

    // ===================== PODSTAWOWE WEJŚCIE =====================

    /**
     * Odczytuje pojedynczą linię tekstu ze strumienia wejściowego.
     *
     * @return wprowadzony przez użytkownika ciąg znaków
     */
    public String readLine() {
        return sc.nextLine();
    }

    /**
     * Zadaje użytkownikowi pytanie i oczekuje na wprowadzenie liczby całkowitej.
     *
     * @param prompt   komunikat/etykieta zapytania
     * @param fallback wartość domyślna zwracana w przypadku braku wprowadzenia liczby lub błędu
     * @return wprowadzona dodatnia liczba całkowita lub wartość {@code fallback}
     */
    public int askInt(String prompt, int fallback) {
        System.out.print(prompt + " (ENTER = " + fallback + "): ");
        String s = sc.nextLine().trim();
        try {
            int v = Integer.parseInt(s);
            return v > 0 ? v : fallback;
        } catch (Exception e) {
            return fallback;
        }
    }

    /**
     * Pobiera nową minimalną długość uwzględnianych słów z konsoli.
     *
     * @param current aktualnie ustawiona długość minimalna
     * @return nowa minimalna długość (co najmniej 1) lub wartość {@code current} w przypadku błędu
     */
    public int askMinWordLength(int current) {
        System.out.print("Nowa minimalna długość słowa (obecnie " + current + "): ");
        String s = sc.nextLine().trim();
        try {
            int v = Integer.parseInt(s);
            return Math.max(1, v);
        } catch (Exception e) {
            return current;
        }
    }

    // ===================== FORMAT RAPORTU =====================

    /**
     * Pobiera od użytkownika wybrany format raportu z konsoli.
     *
     * @return wartość wyliczeniowa {@link ReportWriter.Format} odpowiadająca wyborowi (domyślnie {@code TXT})
     */
    public ReportWriter.Format askReportFormat() {
        System.out.print("Wybierz format (csv/txt/json/xml): ");
        String f = sc.nextLine().trim().toLowerCase(Locale.ROOT);

        return switch (f) {
            case "csv" -> ReportWriter.Format.CSV;
            case "txt" -> ReportWriter.Format.TXT;
            case "json" -> ReportWriter.Format.JSON;
            case "xml" -> ReportWriter.Format.XML;
            default -> {
                System.out.println("Nieznany format, domyślnie JSON");
                yield ReportWriter.Format.TXT;
            }
        };
    }

    // ===================== SORTOWANIE =====================

    /**
     * Pobiera od użytkownika preferowany sposób sortowania słów.
     * <p>
     * Jeśli użytkownik nie poda poprawnej opcji, zostanie użyta wartość domyślna (częstotliwość malejąco).
     * </p>

     * @return wybrany tryb sortowania {@link WordSort}
     */
    public WordSort askSortMode() {
        System.out.println("""
            Wybierz sortowanie:
            1) Alfabetycznie (A → Z)
            2) Częstotliwość malejąco
            3) Częstotliwość rosnąco
            Wybór (ENTER = 2): """);

        String s = sc.nextLine().trim();

        return switch (s) {
            case "1" -> WordSort.ALPHABETIC;
            case "2" -> WordSort.FREQUENCY_DESC;
            case "3" -> WordSort.FREQUENCY_ASC;
            default -> {
                System.out.println("Nieznana opcja, używam: częstotliwość malejąco.");
                yield WordSort.FREQUENCY_DESC;
            }
        };
    }

    // ===================== ŚCIEŻKI =====================

    /**
     * Pobiera od użytkownika nazwę pliku wyjściowego, z opcją domyślną.
     *
     * @param defaultFileName proponowana domyślna nazwa pliku
     * @return ścieżka {@link Path} do pliku w katalogu {@code output/}
     */
    public Path askOutputPath(String defaultFileName) {
        return askOutputPath(defaultFileName, null);
    }

    /**
     * Pobiera od użytkownika nazwę pliku wyjściowego z uwzględnieniem wybranego formatu raportu.
     *
     * @param defaultFileName proponowana domyślna nazwa pliku
     * @param format          format raportu ustalający docelowe rozszerzenie (może być {@code null})
     * @return ścieżka {@link Path} do pliku w katalogu {@code output/}
     */
    public Path askOutputPath(String defaultFileName, ReportWriter.Format format) {
        String suggestedName = defaultFileName;
        if (format != null) {
            suggestedName = ensureExtension(defaultFileName, format);
        }

        System.out.print("Podaj nazwę pliku wyjściowego (ENTER = " + suggestedName + "): ");
        String name = sc.nextLine().trim();
        String finalName = resolveFileName(name, suggestedName, format);

        // automatyczny katalog output/
        return Path.of("output", finalName);
    }

    /**
     * Ustala ostateczną nazwę pliku w oparciu o wejście użytkownika, nazwę sugerowaną oraz format.
     *
     * @param name            wprowadzona nazwa pliku
     * @param defaultFileName nazwa domyślna
     * @param format          wybrany format wyjściowy
     * @return kompletna nazwa pliku wraz z odpowiednim rozszerzeniem
     */
    private String resolveFileName(String name, String defaultFileName, ReportWriter.Format format) {
        if (name.isEmpty()) {
            return defaultFileName;
        }
        if (hasValidExtension(name)) {
            return name;
        }

        if (format != null) {
            return name + extensionFor(format);
        }

        // Użytkownik podał nazwę bez (poprawnego) rozszerzenia -> dodaj rozszerzenie domyślne
        return name + defaultExtension(defaultFileName);
    }

    /**
     * Zapewnia, że proponowana nazwa pliku posiada rozszerzenie pasujące do podanego formatu.
     *
     * @param fileName nazwa pliku wejściowa
     * @param format   wybrany format
     * @return nazwa pliku z odpowiednim rozszerzeniem
     */
    private String ensureExtension(String fileName, ReportWriter.Format format) {
        String extension = extensionFor(format);
        if (fileName == null || fileName.isBlank()) {
            return "report" + extension;
        }

        if (hasValidExtension(fileName)) {
            return fileName;
        }

        return fileName + extension;
    }

    /**
     * Zwraca ciąg znaków reprezentujący rozszerzenie pliku dla wskazanego formatu.
     *
     * @param format format raportu
     * @return kropka i rozszerzenie pliku (np. {@code .csv})
     */
    private String extensionFor(ReportWriter.Format format) {
        return switch (format) {
            case CSV -> ".csv";
            case TXT -> ".txt";
            case JSON -> ".json";
            case XML -> ".xml";
        };
    }

    /**
     * Wyznacza domyślne rozszerzenie na podstawie nazwy pliku wzorcowego.
     *
     * @param defaultFileName domyślna nazwa pliku
     * @return rozszerzenie pliku wraz z kropką lub {@code .txt}
     */
    private String defaultExtension(String defaultFileName) {
        int idx = defaultFileName.lastIndexOf('.');
        return idx >= 0 ? defaultFileName.substring(idx) : ".txt";
    }

    /**
     * Sprawdza, czy podana nazwa pliku posiada wspierane rozszerzenie (.csv, .txt, .json, .xml).
     *
     * @param fileName nazwa pliku do sprawdzenia
     * @return {@code true}, jeśli nazwa kończy się prawidłowym rozszerzeniem; {@code false} w przeciwnym razie
     */
    private boolean hasValidExtension(String fileName) {
        String lower = fileName.toLowerCase(Locale.ROOT);
        return lower.endsWith(".csv") || lower.endsWith(".txt") ||
                lower.endsWith(".json") || lower.endsWith(".xml");
    }
}