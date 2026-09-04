package pl.j.reinmar.ui;

import pl.j.reinmar.io.ReportWriter;
import pl.j.reinmar.model.WordSort;

import java.nio.file.Path;
import java.util.Locale;
import java.util.Scanner;

/**
 * Klasa odpowiedzialna za obsługę wejścia użytkownika w aplikacji konsolowej.
 * Udostępnia metody pobierające:
 * <ul>
 *     <li>wartości liczbowe (z obsługą wartości domyślnych),</li>
 *     <li>minimalną długość słowa,</li>
 *     <li>format raportu ({@link com.cohenm.analyzer.io.ReportWriter.Format}),</li>
 *     <li>tryb sortowania ({@link com.cohenm.analyzer.model.WordSort}),</li>
 *     <li>ścieżkę wyjściową dla raportu.</li>
 * </ul>
 *
 * <p>Klasa pełni rolę warstwy UI — pobiera dane od użytkownika,
 * waliduje je w podstawowym zakresie i zwraca wartości gotowe do użycia
 * przez komponenty takie jak {@link com.cohenm.analyzer.ui.ReportSaver}.</p>
 *
 * <p>Wszystkie operacje wejścia realizowane są poprzez przekazany
 * w konstruktorze obiekt {@link Scanner}.</p>
 *
 * @see com.cohenm.analyzer.io.ReportWriter
 * @see com.cohenm.analyzer.model.WordSort
 * @see com.cohenm.analyzer.ui.ReportSaver
 */
public class UserInput {

    private final Scanner sc;

    /**
     * Tworzy obiekt odpowiedzialny za pobieranie danych od użytkownika.
     *
     * @param sc obiekt {@link Scanner} używany do odczytu z konsoli
     */
    public UserInput(Scanner sc) {
        this.sc = sc;
    }

    // ===================== PODSTAWOWE WEJŚCIE =====================

    /**
     * Odczytuje jedną linię tekstu z wejścia.
     *
     * @return wprowadzona linia tekstu
     */
    public String readLine() {
        return sc.nextLine();
    }

    /**
     * Pyta użytkownika o liczbę całkowitą, umożliwiając pozostawienie
     * wartości domyślnej poprzez naciśnięcie ENTER.
     *
     * @param prompt   komunikat wyświetlany użytkownikowi
     * @param fallback wartość domyślna
     * @return liczba podana przez użytkownika lub wartość domyślna
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
     * Pyta użytkownika o minimalną długość słowa.
     * Wartość musi być co najmniej 1.
     *
     * @param current aktualna wartość
     * @return nowa wartość lub poprzednia, jeśli podano niepoprawną
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
     * Pyta użytkownika o format raportu.
     * Obsługiwane wartości: csv, txt, json, xml.
     * W przypadku nieznanej wartości zwracany jest format JSON.
     *
     * @return wybrany format raportu
     */
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

    /**
     * Alias dla askFormat(), wymagany przez TextMenu po refaktorze.
     */
    public ReportWriter.Format askReportFormat() {
        return askFormat();
    }


    // ===================== SORTOWANIE =====================

    /**
     * Pyta użytkownika o tryb sortowania słów.
     * Obsługiwane skróty i aliasy:
     * <ul>
     *     <li>alpha / alf / alphabetic / alfabetycznie → ALPHABETIC</li>
     *     <li>freq-desc / desc / malejąco → FREQUENCY_DESC</li>
     *     <li>freq-asc / asc / rosnąco → FREQUENCY_ASC</li>
     * </ul>
     *
     * @return wybrany tryb sortowania
     */
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

    /**
     * Pyta użytkownika o nazwę pliku wyjściowego.
     * Jeśli użytkownik naciśnie ENTER, używana jest wartość domyślna.
     * Jeśli podano nazwę bez rozszerzenia, zostanie użyta nazwa domyślna.
     *
     * <p>Plik jest automatycznie umieszczany w katalogu <code>output/</code>.</p>
     *
     * @param defaultFileName proponowana nazwa pliku
     * @return ścieżka do pliku wyjściowego
     */
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

    /**
     * Sprawdza, czy nazwa pliku posiada jedno z obsługiwanych rozszerzeń.
     *
     * @param fileName nazwa pliku
     * @return true, jeśli rozszerzenie jest poprawne
     */
    private boolean hasValidExtension(String fileName) {
        String lower = fileName.toLowerCase(Locale.ROOT);
        return lower.endsWith(".csv") || lower.endsWith(".txt") ||
                lower.endsWith(".json") || lower.endsWith(".xml");
    }
}