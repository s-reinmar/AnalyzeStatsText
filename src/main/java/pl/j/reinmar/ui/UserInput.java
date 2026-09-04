package pl.j.reinmar.ui;

import pl.j.reinmar.io.ReportWriter;
import pl.j.reinmar.model.WordSort;

import java.nio.file.Path;
import java.util.Locale;
import java.util.Scanner;

public class UserInput {

    private final Scanner sc;

    public UserInput(Scanner sc) {
        this.sc = sc;
    }

    // ===================== PODSTAWOWE WEJŚCIE =====================

    public String readLine() {
        return sc.nextLine();
    }

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

    /***
     * Pobiera od użytkownika preferowany sposób sortowania słów. Jeśli użytkownik nie poda poprawnej opcji, zostanie użyta wartość domyślna (częstotliwość malejąco).
     * @return
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

    /***
     * Pobiera od użytkownika nazwę pliku wyjściowego, z opcją domyślną. Jeśli użytkownik nie poda rozszerzenia, zostanie użyta wartość domyślna.
     * @param defaultFileName
     * @return
     */

    public Path askOutputPath(String defaultFileName) {
        return askOutputPath(defaultFileName, null);
    }

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

    private String extensionFor(ReportWriter.Format format) {
        return switch (format) {
            case CSV -> ".csv";
            case TXT -> ".txt";
            case JSON -> ".json";
            case XML -> ".xml";
        };
    }

    private String defaultExtension(String defaultFileName) {
        int idx = defaultFileName.lastIndexOf('.');
        return idx >= 0 ? defaultFileName.substring(idx) : ".txt";
    }

    private boolean hasValidExtension(String fileName) {
        String lower = fileName.toLowerCase(Locale.ROOT);
        return lower.endsWith(".csv") || lower.endsWith(".txt") ||
                lower.endsWith(".json") || lower.endsWith(".xml");
    }
}