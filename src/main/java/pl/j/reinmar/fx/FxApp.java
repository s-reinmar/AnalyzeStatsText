package pl.j.reinmar.fx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class FxApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        FXMLLoader loader = new FXMLLoader(FxApp.class.getResource("/fxml/main-view.fxml"));


        Scene scene = new Scene(loader.load(), 920, 620);
        stage.setTitle("Text Analyzer - JavaFX");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}