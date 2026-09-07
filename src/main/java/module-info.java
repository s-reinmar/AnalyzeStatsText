/***
 *  Główny moduł aplikacji (wersja CLI oraz JavaFX).
 *  <p>
 *  Moduł stanowi podsumowanie i zwieńczenie projektu opartego na czystej Javie.
 *  Łączy w sobie interfejs wiersza poleceń (CLI) oraz interfejs graficzny oparty na JavaFX.
 *  </p>
 *
 *  @author Sławek Reinmar
 *  @version 1.0
 */
module pl.j.reinmar {

    // Wymagane moduły zewnętrzneq
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    // Udostępnianie klas dla JavaFX Graphics
    exports pl.j.reinmar.fx
            to javafx.graphics;

    // Udostępnianie refleksji dla JavaFX FXML
    opens pl.j.reinmar.fx
            to javafx.fxml;
}