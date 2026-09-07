package pl.j.reinmar.app.menu.actions;

import pl.j.reinmar.app.Settings;
import pl.j.reinmar.app.menu.MenuAction;
import pl.j.reinmar.ui.UserInput;

/**
 * Akcja menu odpowiedzialna za zmianę minimalnej długości filtrowanych słów.
 * <p>
 * Klasa odczytuje aktualną wartość ustawienia z obiektu {@link Settings},
 * prosi użytkownika o podanie nowej wartości za pomocą komponentu {@link UserInput},
 * a następnie aktualizuje konfigurację aplikacji.
 * </p>
 *
 * @author Sławek Reinmar
 * @version 1.0
 */
public class ChangeMinWordLengthAction implements MenuAction {

    /** Komponent do pobierania wprowadzanych danych od użytkownika. */
    private final UserInput input;

    /** Obiekt globalnych ustawień aplikacji. */
    private final Settings settings;

    /**
     * Tworzy nową akcję zmiany minimalnej długości słowa.
     *
     * @param input    komponent {@link UserInput} obsługujący interakcję z użytkownikiem
     * @param settings obiekt {@link Settings} przechowujący aktualną konfigurację
     */
    public ChangeMinWordLengthAction(UserInput input, Settings settings) {
        this.input = input;
        this.settings = settings;
    }

    /**
     * Wykonuje interaktywną procedurę pobrania i aktualizacji parametru minimalnej długości słowa.
     */
    @Override
    public void execute() {
        int current = settings.getMinWordLength();
        int updated = input.askMinWordLength(current);
        settings.setMinWordLength(updated);
    }

    /**
     * Zwraca etykietę opcji wyświetlaną w menu.
     *
     * @return etykieta tekstowa opcji menu
     */
    @Override
    public String label() {
        return "4) Zmień minWordLength";
    }
}