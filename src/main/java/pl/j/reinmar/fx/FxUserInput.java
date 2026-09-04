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

public class FxUserInput extends UserInput {

    private Window owner;

    public FxUserInput() {
        // Bazowa klasa wymaga Scanner; tu używamy dialogów JavaFX.
        super(new Scanner(System.in));
    }

    public void setOwner(Window owner) {
        this.owner = owner;
    }

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

    @Override
    public Path askOutputPath(String defaultFileName) {
        TextInputDialog dialog = new TextInputDialog(defaultFileName);
        dialog.setTitle("Plik wyjściowy");
        dialog.setHeaderText("Podaj nazwę pliku wyjściowego");
        if (owner != null) {
            dialog.initOwner(owner);
        }

        String name = dialog.showAndWait().orElse(defaultFileName).trim();
        String finalName = name.isEmpty() ? defaultFileName : name;

        if (!hasValidExtension(finalName)) {
            finalName = defaultFileName;
        }

        return Path.of("output", finalName);
    }

    private boolean hasValidExtension(String fileName) {
        String lower = fileName.toLowerCase(Locale.ROOT);
        return lower.endsWith(".csv")
                || lower.endsWith(".txt")
                || lower.endsWith(".json")
                || lower.endsWith(".xml");
    }
}