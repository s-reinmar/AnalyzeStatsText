package pl.j.reinmar.fx;

import pl.j.reinmar.app.Settings;
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
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Window;

import java.io.ByteArrayOutputStream;
import java.io.File;
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
    private Label statusLabel;
    @FXML
    private ListView<MenuAction> menuList;
    @FXML
    private TextArea logArea;

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

        // Owner dla dialogów dostępny dopiero po osadzeniu node w scenie.
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

        if (baseName.endsWith(".txt") || baseName.contains("\\") || baseName.contains("/")) {
            currentPath = baseName;
        } else {
            currentPath = baseName + ".txt";
        }

        refreshActionsForCurrentPath();
    }

    @FXML
    public void onBrowseFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Wybierz plik tekstowy");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Pliki tekstowe", "*.txt"),
                new FileChooser.ExtensionFilter("Wszystkie pliki", "*.*")
        );

        File initialDir = new File(System.getProperty("user.home"));
        if (initialDir.exists()) {
            fileChooser.setInitialDirectory(initialDir);
        }

        Window window = baseNameField.getScene() != null ? baseNameField.getScene().getWindow() : null;
        File selectedFile = fileChooser.showOpenDialog(window);

        if (selectedFile == null) {
            return;
        }

        currentPath = selectedFile.getAbsolutePath();
        baseNameField.setText(currentPath);
        refreshActionsForCurrentPath();
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

        // Każde uruchomienie opcji zaczyna z czystym obszarem wynikowym.
        logArea.clear();

        try {
            selected.execute();
            statusLabel.setText("Wykonano: " + selected.label());
        } catch (Exception e) {
            statusLabel.setText("Błąd wykonania akcji.");
            System.err.println("Błąd akcji: " + e.getMessage());
        }
    }

    private void refreshActionsForCurrentPath() {
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

    private void installConsoleRedirect() {
        PrintStream out = new PrintStream(new TextAreaOutputStream(logArea), true, StandardCharsets.UTF_8);
        PrintStream err = new PrintStream(new TextAreaOutputStream(logArea), true, StandardCharsets.UTF_8);
        System.setOut(out);
        System.setErr(err);
    }

    private static final class TextAreaOutputStream extends OutputStream {
        private final TextArea textArea;
        private final ByteArrayOutputStream buffer = new ByteArrayOutputStream();

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
            String line = buffer.toString(StandardCharsets.UTF_8);
            buffer.reset();

            Platform.runLater(() -> {
                textArea.appendText(line);
                textArea.appendText(System.lineSeparator());
            });
        }

    }
}