package pl.j.reinmar.app.menu.actions;

import pl.j.reinmar.app.menu.MenuAction;

/**
 * Akcja menu odpowiedzialna za bezpieczne zakończenie działania aplikacji.
 * <p>
 * Klasa wyświetla komunikat pożegnalny w konsoli, a następnie zamyka
 * proces wirtualnej maszyny Javy (JVM) ze statusem {@code 0}.
 * </p>
 *
 * @author Sławek Reinmar
 * @version 1.0
 */
public class ExitAction implements MenuAction {

    /**
     * Domyślny konstruktor akcji zakończenia programu.
     */
    public ExitAction() {
    }

    /**
     * Wyświetla komunikat końcowy na standardowym wyjściu i zatrzymuje aplikację.
     */
    @Override
    public void execute() {
        System.out.println("Koniec. Do zobaczenia!");
        System.exit(0);
    }

    /**
     * Zwraca etykietę opcji wyjścia wyświetlaną w menu.
     *
     * @return etykieta tekstowa opcji wyjścia
     */
    @Override
    public String label() {
        return "0) Wyjście";
    }
}