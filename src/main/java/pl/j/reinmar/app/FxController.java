package pl.j.reinmar.app;

import pl.j.reinmar.app.menu.MenuAction;
import pl.j.reinmar.app.menu.MenuActionFactory;
import pl.j.reinmar.app.menu.MenuOption;
import pl.j.reinmar.core.DefaultNormalizer;
import pl.j.reinmar.core.DefaultSentenceTokenizer;
import pl.j.reinmar.core.TextAnalyzer;
import pl.j.reinmar.core.WhitespaceTokenizer;
import pl.j.reinmar.ui.ReportSaver;
import pl.j.reinmar.ui.StatsPrinter;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Window;

import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FxController {

    @FXML
    private TextField baseNameField;
    @FXML
    private ListView<MenuAction> menuList;
    @FXML
    private TextArea logArea;
    @FXML
    private Label statusLabel;

    private TextAnalyzer analyzer;
    private Settings settings;
    private ReportSaver saver;
    private FxUserInput input;
    private String currentPath;

    @FXML
    public void initialize() {
        analyzer = new TextAnalyzer(
                new DefaultNormalizer(),
                new WhitespaceTokenizer(),
                new DefaultSentenceTokenizer()
        );

        settings = new Settings();
        saver = new ReportSaver(analyzer);
        input = new FxUserInput();

        menuList.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(MenuAction item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.label());
            }
        });

        menuList.setItems(FXCollections.observableArrayList());
        menuList.setPlaceholder(new Label("Najpierw podaj nazwę pliku i kliknij Załaduj"));
        installConsoleRedirect();

        statusLabel.setText("Gotowe. Wpisz nazwę pliku bez .txt");
        baseNameField.setText("file");

        // owner dla dialogów dostępny dopiero po osadzeniu node w scenie
        logArea.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                Window window = newScene.getWindow();
                if (window != null) {
                    input.setOwner(window);
                } else {
                    newScene.windowProperty().addListener((o, oldWin, newWin) -> input.setOwner(newWin));
                }
            }
        });
    }

    @FXML
    public void onLoadFile() {
        String baseName = baseNameField.getText() == null ? "" : baseNameField.getText().trim();
        if (baseName.isEmpty()) {
            statusLabel.setText("Podaj nazwę pliku (bez .txt).");
            return;
        }

        currentPath = baseName + ".txt";
        StatsPrinter printer = new StatsPrinter();

        Map<MenuOption, MenuAction> actions = MenuActionFactory.create(
                analyzer,
                currentPath,
                input,
                printer,
                saver,
                settings
        );

        List<MenuAction> ordered = new ArrayList<>();
        for (MenuOption option : MenuOption.values()) {
            MenuAction action = actions.get(option);
            if (action != null) {
                ordered.add(action);
            }
        }

        menuList.setItems(FXCollections.observableArrayList(ordered));
        statusLabel.setText("Załadowano: " + currentPath);
        System.out.println("Aktywny plik wejściowy: " + currentPath);
    }

    @FXML
    public void onExecuteSelected() {
        MenuAction selected = menuList.getSelectionModel().getSelectedItem();
        if (selected == null) {
            statusLabel.setText("Wybierz opcję z listy.");
            return;
        }

        if (currentPath == null || currentPath.isBlank()) {
            statusLabel.setText("Najpierw załaduj plik wejściowy.");
            return;
        }

        try {
            selected.execute();
            statusLabel.setText("Wykonano: " + selected.label());
        } catch (Exception e) {
            statusLabel.setText("Błąd wykonania akcji.");
            System.err.println("❌ Błąd akcji: " + e.getMessage());
        }
    }

    private void installConsoleRedirect() {
        PrintStream out = new PrintStream(new TextAreaOutputStream(logArea), true, StandardCharsets.UTF_8);
        PrintStream err = new PrintStream(new TextAreaOutputStream(logArea), true, StandardCharsets.UTF_8);
        System.setOut(out);
        System.setErr(err);
    }

    private static final class TextAreaOutputStream extends OutputStream {
        private final TextArea textArea;
        private final java.io.ByteArrayOutputStream buffer = new java.io.ByteArrayOutputStream();

        private TextAreaOutputStream(TextArea textArea) {
            this.textArea = textArea;
        }

        @Override
        public synchronized void write(int b) {
            if (b == '\n') {
                flushBuffer();
            } else if (b != '\r') {
                buffer.write(b);
            }
        }

        @Override
        public synchronized void flush() {
            flushBuffer();
        }

        private void flushBuffer() {
            if (buffer.size() == 0) {
                return;
            }
            String line = buffer.toString(java.nio.charset.StandardCharsets.UTF_8);
            buffer.reset();

            Platform.runLater(() -> {
                textArea.appendText(line);
                textArea.appendText(System.lineSeparator());
            });
        }
    }
}