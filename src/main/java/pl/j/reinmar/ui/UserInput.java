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

    public Path askOutputPath(String defaultFileName) {
        System.out.print("Podaj nazwę pliku wyjściowego (ENTER = " + defaultFileName + "): ");
        String name = sc.nextLine().trim();
        String finalName = name.isEmpty() ? defaultFileName : name;

        // Jeśli użytkownik nie podał rozszerzenia, użyj wartości domyślnej
        if (!name.isEmpty() && !hasValidExtension(finalName)) {
            finalName = defaultFileName;
        }

        // automatyczny katalog output/
        return Path.of("output", finalName);
    }

    private boolean hasValidExtension(String fileName) {
        String lower = fileName.toLowerCase(Locale.ROOT);
        return lower.endsWith(".csv") || lower.endsWith(".txt") ||
                lower.endsWith(".json") || lower.endsWith(".xml");
    }
}