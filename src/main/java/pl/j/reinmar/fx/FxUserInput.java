package pl.j.reinmar.fx;

import pl.j.reinmar.io.ReportWriter;
import pl.j.reinmar.model.WordSort;
import pl.j.reinmar.ui.UserInput;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Window;

import java.nio.file.Path;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Scanner;

/**
 * Adapter klasy {@link UserInput} dostosowany do interfejsu graficznego JavaFX.
 * <p>
 * Zastępuje konsolowe metody pobierania danych od użytkownika graficznymi oknami dialogowymi
 * ({@link TextInputDialog} oraz {@link ChoiceDialog}). Klasa umożliwia również przypisanie
 * okna nadrzędnego ({@link Window}), aby dialogi były modalne względem głównego okna aplikacji.
 * </p>
 *
 * @author Sławek Reinmar
 * @version 1.0
 */
public class FxUserInput extends UserInput {

    /** Okno nadrzędne (owner) dla wyświetlanych okien dialogowych JavaFX. */
    private Window owner;

    /**
     * Tworzy nową instancję adaptera graficznego pobierania danych.
     * Inicjalizuje klasę nadrzędną atrapą obiektu {@link Scanner}.
     */
    public FxUserInput() {
        // Bazowa klasa wymaga Scanner; tu używamy dialogów JavaFX.
        super(new Scanner(System.in));
    }

    /**
     * Ustawia okno nadrzędne dla okien dialogowych generowanych przez tę klasę.
     *
     * @param owner okno {@link Window} stanowiące kontekst nadrzędny dla dialogów
     */
    public void setOwner(Window owner) {
        this.owner = owner;
    }

    /**
     * Odczytuje linię tekstu od użytkownika za pomocą okna dialogowego {@link TextInputDialog}.
     *
     * @return wprowadzony tekst lub pusty ciąg znaków, jeśli dialog został anulowany
     */
    @Override
    public String readLine() {
        TextInputDialog dialog = new TextInputDialog("");
        dialog.setTitle("Wejście");
        dialog.setHeaderText("Podaj wartość");
        if (owner != null) {
            dialog.initOwner(owner);
        }
        return dialog.showAndWait().orElse("");
    }

    /**
     * Wyświetla okno dialogowe z zapytaniem o liczbę całkowitą (N).
     *
     * @param prompt   komunikat/etykieta zapytania
     * @param fallback wartość domyślna zwracana w przypadku anulowania lub błędnych danych
     * @return wprowadzona dodatnia liczba całkowita lub wartość {@code fallback}
     */
    @Override
    public int askInt(String prompt, int fallback) {
        TextInputDialog dialog = new TextInputDialog(String.valueOf(fallback));
        dialog.setTitle("Liczba");
        dialog.setHeaderText(prompt + " (ENTER = " + fallback + ")");
        if (owner != null) {
            dialog.initOwner(owner);
        }

        Optional<String> result = dialog.showAndWait();
        if (result.isEmpty()) {
            return fallback;
        }

        try {
            int v = Integer.parseInt(result.get().trim());
            return v > 0 ? v : fallback;
        } catch (Exception e) {
            return fallback;
        }
    }

    /**
     * Wyświetla okno dialogowe do zmiany minimalnej długości uwzględnianych słów.
     *
     * @param current aktualnie ustawiona minimalna długość słowa
     * @return nowa długość słowa (minimum 1) lub wartość {@code current} w przypadku błędu/anulowania
     */
    @Override
    public int askMinWordLength(int current) {
        TextInputDialog dialog = new TextInputDialog(String.valueOf(current));
        dialog.setTitle("Minimalna długość słowa");
        dialog.setHeaderText("Nowa minimalna długość słowa (obecnie " + current + ")");
        if (owner != null) {
            dialog.initOwner(owner);
        }

        Optional<String> result = dialog.showAndWait();
        if (result.isEmpty()) {
            return current;
        }

        try {
            int v = Integer.parseInt(result.get().trim());
            return Math.max(1, v);
        } catch (Exception e) {
            return current;
        }
    }

    /**
     * Wyświetla okno wyboru rozwijalnego ({@link ChoiceDialog}) z dostępnymi formatami raportów.
     *
     * @return wybrana wartość wyliczeniowa {@link ReportWriter.Format} (domyślnie {@code TXT})
     */
    @Override
    public ReportWriter.Format askReportFormat() {
        List<String> choices = List.of("txt", "csv", "json", "xml");
        ChoiceDialog<String> dialog = new ChoiceDialog<>("txt", choices);
        dialog.setTitle("Format raportu");
        dialog.setHeaderText("Wybierz format raportu");
        if (owner != null) {
            dialog.initOwner(owner);
        }

        String selected = dialog.showAndWait().orElse("txt").toLowerCase(Locale.ROOT);
        return switch (selected) {
            case "csv" -> ReportWriter.Format.CSV;
            case "json" -> ReportWriter.Format.JSON;
            case "xml" -> ReportWriter.Format.XML;
            default -> ReportWriter.Format.TXT;
        };
    }

    /**
     * Wyświetla okno wyboru rozwijalnego ({@link ChoiceDialog}) z dostępnymi kryteriami sortowania słów.
     *
     * @return wybrana wartość wyliczeniowa {@link WordSort} (domyślnie {@code FREQUENCY_DESC})
     */
    @Override
    public WordSort askSortMode() {
        List<String> choices = List.of(
                "Alfabetycznie (A -> Z)",
                "Częstotliwość malejąco",
                "Częstotliwość rosnąco"
        );

        ChoiceDialog<String> dialog = new ChoiceDialog<>("Częstotliwość malejąco", choices);
        dialog.setTitle("Sortowanie");
        dialog.setHeaderText("Wybierz sposób sortowania słów");
        if (owner != null) {
            dialog.initOwner(owner);
        }

        String selected = dialog.showAndWait().orElse("Częstotliwość malejąco");
        return switch (selected) {
            case "Alfabetycznie (A -> Z)" -> WordSort.ALPHABETIC;
            case "Częstotliwość rosnąco" -> WordSort.FREQUENCY_ASC;
            default -> WordSort.FREQUENCY_DESC;
        };
    }

    /**
     * Pobiera od użytkownika ścieżkę wyjściową dla pliku raportu z domyślną nazwą pliku.
     *
     * @param defaultFileName proponowana, domyślna nazwa pliku
     * @return ścieżka {@link Path} do pliku w katalogu {@code output/}
     */
    @Override
    public Path askOutputPath(String defaultFileName) {
        return askOutputPath(defaultFileName, null);
    }

    /**
     * Pobiera od użytkownika ścieżkę wyjściową dla pliku raportu z uwzględnieniem wybranego formatu.
     *
     * @param defaultFileName proponowana, domyślna nazwa pliku
     * @param format          format raportu do automatycznego dopasowania rozszerzenia (może być {@code null})
     * @return ścieżka {@link Path} do pliku w katalogu {@code output/}
     */
    public Path askOutputPath(String defaultFileName, ReportWriter.Format format) {
        String suggestedName = defaultFileName;
        if (format != null) {
            suggestedName = ensureExtension(defaultFileName, format);
        }

        TextInputDialog dialog = new TextInputDialog(suggestedName);
        dialog.setTitle("Plik wyjściowy");
        dialog.setHeaderText("Podaj nazwę pliku wyjściowego");
        if (owner != null) {
            dialog.initOwner(owner);
        }

        String name = dialog.showAndWait().orElse(suggestedName).trim();
        String finalName = resolveFileName(name, suggestedName, format);

        return Path.of("output", finalName);
    }

    /**
     * Rozstrzyga ostateczną nazwę pliku, zapewniając odpowiednie rozszerzenie.
     *
     * @param name            nazwa wprowadzona przez użytkownika
     * @param defaultFileName domyślna nazwa zastępcza
     * @param format          wybrany format wyjściowy
     * @return kompletna nazwa pliku wraz z rozszerzeniem
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
        return name + defaultExtension(defaultFileName);
    }

    /**
     * Zapewnia, że proponowana nazwa pliku posiada rozszerzenie zgodne z formatem.
     *
     * @param fileName nazwa pliku wejściowa
     * @param format   format wyjściowy
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
     * Zwraca rozszerzenie pliku odpowiadające danemu formatowi raportu.
     *
     * @param format format raportu
     * @return ciąg znaków z rozszerzeniem (np. {@code .csv})
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
     * Wyodrębnia domyślne rozszerzenie z podanej nazwy pliku.
     *
     * @param defaultFileName domyślna nazwa pliku
     * @return odnalezione rozszerzenie lub {@code .txt}, jeśli brak kropki
     */
    private String defaultExtension(String defaultFileName) {
        int idx = defaultFileName.lastIndexOf('.');
        return idx >= 0 ? defaultFileName.substring(idx) : ".txt";
    }

    /**
     * Sprawdza, czy podana nazwa pliku kończy się jednym ze wspieranych rozszerzeń (.txt, .csv, .json, .xml).
     *
     * @param fileName nazwa pliku do weryfikacji
     * @return {@code true}, jeśli nazwa posiada akceptowalne rozszerzenie; w przeciwnym razie {@code false}
     */
    private boolean hasValidExtension(String fileName) {
        String lower = fileName.toLowerCase(Locale.ROOT);
        return lower.endsWith(".csv")
                || lower.endsWith(".txt")
                || lower.endsWith(".json")
                || lower.endsWith(".xml");
    }
}