module pl.j.reinmar {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    exports pl.j.reinmar.app to javafx.graphics;
    opens pl.j.reinmar.app to javafx.fxml;
    exports pl.j.reinmar.fx to javafx.graphics;
    opens pl.j.reinmar.fx to javafx.fxml;
    exports pl.j.reinmar.cli to javafx.graphics;
    opens pl.j.reinmar.cli to javafx.fxml;
}