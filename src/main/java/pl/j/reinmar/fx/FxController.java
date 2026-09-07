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

/**
 * Kontroler widoku FXML dla interfejsu graficznego aplikacji (JavaFX).
 * <p>
 * Klasa odpowiada za obsługę zdarzeń interfejsu użytkownika, zarządzanie wybranym plikiem źródłowym,
 * przekierowanie strumieni wyjściowych {@link System#out} i {@link System#err} do komponentu {@link TextArea},
 * oraz dynamiczne budowanie i wykonywanie akcji menu ({@link MenuAction}) za pomocą {@link FxUserInput}.
 * </p>
 *
 * @author Sławek Reinmar
 * @version 1.0
 */
public class FxController {

    /** Pole tekstowe służące do wprowadzania lub wyświetlania ścieżki pliku źródłowego. */
    @FXML
    private TextField baseNameField;

    /** Etykieta informacyjna wyświetlająca aktualny status aplikacji. */
    @FXML
    private Label statusLabel;

    /** Lista komponentów GUI wyświetlająca dostępne akcje menu. */
    @FXML
    private ListView<MenuAction> menuList;

    /** Obszar tekstowy stanowiący konsolę wyjściową dla wyników analizy i błędów. */
    @FXML
    private TextArea logArea;

    /** Główny silnik analityczny tekstu. */
    private TextAnalyzer analyzer;

    /** Obiekt globalnych ustawień aplikacji. */
    private Settings settings;

    /** Komponent odpowiedzialny za zapis raportów do plików. */
    private ReportSaver saver;

    /** Adapter interakcji z użytkownikiem dostosowany do okien dialogowych JavaFX. */
    private FxUserInput input;

    /** Aktualnie załadowana ścieżka do pliku tekstowego. */
    private String currentPath;

    /**
     * Domyślny konstruktor kontrolera widoku JavaFX.
     */
    public FxController() {
    }

    /**
     * Inicjalizuje kontroler po załadowaniu pliku FXML.
     * <p>
     * Metoda konfiguruje silnik analityczny, widok komórek w {@link ListView}, przekierowuje standardowe
     * strumienie wyjściowe do obszaru {@link TextArea} oraz rejestruje słuchacza sceny w celu ustawienia
     * okna nadrzędnego (Owner Window) dla dialogów interaktywnych.
     * </p>
     */
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

    /**
     * Obsługuje zdarzenie załadowania pliku wprowadzonego w polu tekstowym.
     * <p>
     * Normalizuje podaną nazwę (dodaje rozszerzenie {@code .txt}, jeśli nie zostało podane)
     * i odświeża listę dostępnych akcji dla wybranej ścieżki.
     * </p>
     */
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

    /**
     * Obsługuje zdarzenie otwarcia okna wyboru pliku ({@link FileChooser}).
     * <p>
     * Pozwala użytkownikowi na wskazanie pliku z dysku, po czym aktualizuje pole tekstowe
     * i ładuje opcje dla wybranej ścieżki bezwzględnej.
     * </p>
     */
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

    /**
     * Wykonuje zaznaczoną na liście opcję menu ({@link MenuAction}).
     * <p>
     * Przed uruchomieniem czyszczony jest obszar konsoli wyjściowej ({@link TextArea}).
     * Wszelkie wyjątki rzucane podczas wykonywania akcji są wyłapywane i sygnalizowane na pasku statusu.
     * </p>
     */
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

    /**
     * Odświeża mapowanie akcji menu dla nowo wybranej ścieżki do pliku.
     */
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

    /**
     * Przekierowuje strumienie {@link System#out} oraz {@link System#err} do komponentu {@link TextArea}.
     */
    private void installConsoleRedirect() {
        PrintStream out = new PrintStream(new TextAreaOutputStream(logArea), true, StandardCharsets.UTF_8);
        PrintStream err = new PrintStream(new TextAreaOutputStream(logArea), true, StandardCharsets.UTF_8);
        System.setOut(out);
        System.setErr(err);
    }

    /**
     * Pomocniczy strumień wyjściowy buforujący bajty i dopisujący pełne linie do obiektu {@link TextArea}
     * w wątku graficznym ({@link Platform#runLater(Runnable)}).
     */
    private static final class TextAreaOutputStream extends OutputStream {

        /** Komponent tekstu, do którego dopisywane są wyjściowe linie. */
        private final TextArea textArea;

        /** Wewnętrzny bufor bajtów. */
        private final ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        /**
         * Tworzy strumień dopisujący wyjście do podanego pola tekstowego.
         *
         * @param textArea komponent {@link TextArea} stanowiący cel wyjścia
         */
        private TextAreaOutputStream(TextArea textArea) {
            this.textArea = textArea;
        }

        /**
         * Zapisuje pojedynczy bajt do bufora. Jeśli napotka znak nowej linii, wypłukuje bufor.
         *
         * @param b zapisywany bajt
         */
        @Override
        public synchronized void write(int b) {
            if (b == '\n') {
                flushBuffer();
            } else if (b != '\r') {
                buffer.write(b);
            }
        }

        /**
         * Wypłukuje zawartość bufora do komponentu tekstowego.
         */
        @Override
        public synchronized void flush() {
            flushBuffer();
        }

        /**
         * Opróżnia wewnętrzny bufor i dopisuje sformatowaną linię tekstu do {@link TextArea}
         * w wątku aplikacji JavaFX.
         */
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