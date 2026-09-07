package pl.j.reinmar.app.menu;

/**
 * Reprezentuje pojedynczą akcję menu (Command Pattern) w aplikacji.
 * <p>
 * Interfejs służy do definiowania wykonywalnych operacji dostępnych w menu
 * (zarówno w interfejsie konsolowym CLI, jak i graficznym GUI). Łączy etykietę
 * wyświetlaną użytkownikowi z logiką do uruchomienia.
 * </p>
 *
 * @author Sławek Reinmar
 * @version 1.0
 */
public interface MenuAction {

    /**
     * Wykonuje logikę przypisaną do danej akcji menu.
     */
    void execute();

    /**
     * Zwraca etykietę (tekst) opisującą opcję w menu.
     *
     * @return nazwa lub opis akcji przeznaczony do wyświetlenia użytkownikowi
     */
    String label();
}