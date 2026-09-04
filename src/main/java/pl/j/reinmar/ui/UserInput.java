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

    public ReportWriter.Format askFormat() {
        System.out.print("Wybierz format (csv/txt/json/xml): ");
        String f = sc.nextLine().trim().toLowerCase(Locale.ROOT);

        return switch (f) {
            case "csv" -> ReportWriter.Format.CSV;
            case "txt" -> ReportWriter.Format.TXT;
            case "json" -> ReportWriter.Format.JSON;
            case "xml" -> ReportWriter.Format.XML;
            default -> {
                System.out.println("Nieznany format, domyślnie JSON");
                yield ReportWriter.Format.JSON;
            }
        };
    }

    // ===================== SORTOWANIE =====================

    public WordSort askSortMode() {
        System.out.print("Wybierz sortowanie (alpha / freq-desc / freq-asc): ");
        String s = sc.nextLine().trim().toLowerCase(Locale.ROOT);

        return switch (s) {
            case "alpha", "alf", "alphabetic", "alfabetycznie" -> WordSort.ALPHABETIC;
            case "freq-desc", "desc", "malejąco" -> WordSort.FREQUENCY_DESC;
            case "freq-asc", "asc", "rosnąco" -> WordSort.FREQUENCY_ASC;
            default -> {
                System.out.println("Nieznany tryb, domyślnie freq-desc.");
                yield WordSort.FREQUENCY_DESC;
            }
        };
    }

    // ===================== ŚCIEŻKI =====================

    public Path askOutputPath(String defaultFileName) {
        System.out.print("Podaj nazwę pliku wyjściowego (ENTER = " + defaultFileName + "): ");
        String name = sc.nextLine().trim();
        String finalName = name.isEmpty() ? defaultFileName : name;

        // automatyczny katalog output/
        return Path.of("output", finalName);
    }
}