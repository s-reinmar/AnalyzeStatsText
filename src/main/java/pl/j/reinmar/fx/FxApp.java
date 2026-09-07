package pl.j.reinmar.fx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Główna klasa uruchomieniowa dla interfejsu graficznego (GUI) opartego na technologii JavaFX.
 * <p>
 * Odpowiada za inicjalizację środowiska JavaFX, załadowanie głównego widoku FXML
 * ({@code /fxml/main-view.fxml}), utworzenie sceny oraz wyświetlenie głównego okna aplikacji.
 * </p>
 *
 * @author Sławek Reinmar
 * @version 1.0
 */
public class FxApp extends Application {

    /**
     * Domyślny konstruktor klasy wywoływalnej aplikacji JavaFX.
     */
    public FxApp() {
    }

    /**
     * Inicjalizuje i wyświetla główne okno (Stage) aplikacji graficznej.
     * <p>
     * Metoda wczytuje układ interfejsu użytkownika z pliku FXML, tworzy nową scenę
     * o wymiarach 920x620 pikseli i ustawia tytuł okna.
     * </p>
     *
     * @param stage główne okno aplikacji (Stage) dostarczane przez środowisko JavaFX
     * @throws Exception jeśli wystąpi błąd podczas ładowania pliku FXML lub tworzenia sceny
     */
    @Override
    public void start(Stage stage) throws Exception {

        FXMLLoader loader = new FXMLLoader(FxApp.class.getResource("/fxml/main-view.fxml"));

        Scene scene = new Scene(loader.load(), 920, 620);
        stage.setTitle("Text Analyzer - JavaFX");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Główny punkt wejścia (Main Entry Point) dla wersji graficznej (GUI) aplikacji.
     *
     * @param args argumenty wiersza poleceń przekazywane przy uruchamianiu programu
     */
    public static void main(String[] args) {
        launch(args);
    }
}