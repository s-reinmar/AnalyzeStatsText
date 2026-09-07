package pl.j.reinmar.cli;

import pl.j.reinmar.app.menu.MenuAction;
import pl.j.reinmar.app.menu.MenuOption;
import pl.j.reinmar.ui.UserInput;

import java.util.Map;

/**
 * Pętla głównego menu interfejsu konsolowego (CLI).
 * <p>
 * Odpowiada za cykliczne wyświetlanie dostępnych opcji menu, pobieranie wyboru
 * od użytkownika za pomocą {@link UserInput} oraz wywoływanie przypisanych
 * akcji {@link MenuAction}.
 * </p>
 *
 * @author Sławek Reinmar
 * @version 1.0
 */
public class TextMenu {

    /** Komponent obsługujący wprowadzanie danych przez użytkownika. */
    private final UserInput input;

    /** Mapa wiążąca opcje menu ze skojarzonymi z nimi akcjami. */
    private final Map<MenuOption, MenuAction> actions;

    /**
     * Tworzy nową instancję menu konsolowego.
     *
     * @param input   komponent {@link UserInput} służący do odczytywania wyborów użytkownika
     * @param actions mapa {@link Map} powiązująca wartości {@link MenuOption} z instancjami {@link MenuAction}
     */
    public TextMenu(UserInput input, Map<MenuOption, MenuAction> actions) {
        this.input = input;
        this.actions = actions;
    }

    /**
     * Uruchamia nieskończoną pętlę obsługi menu konsolowego.
     * <p>
     * W każdej iteracji metoda wyświetla listę opcji, pobiera wprowadzony klawisz,
     * weryfikuje jego poprawność i uruchamia odpowiadającą akcję.
     * Pętla jest przerywana, gdy wywołana akcja zakończy działanie programu (np. wywołanie {@code System.exit(0)}).
     * </p>
     */
    public void run() {
        while (true) {
            printMenu();
            String key = input.readLine().trim();
            MenuOption option = MenuOption.fromKey(key);

            if (option == null) {
                System.out.println("Nieznana opcja.");
                continue;
            }

            actions.get(option).execute();
        }
    }

    /**
     * Drukuje w konsoli nagłówek oraz etykiety wszystkich zarejestrowanych akcji menu.
     */
    private void printMenu() {
        System.out.println("\n=== MENU ===");
        actions.values().forEach(a -> System.out.println(a.label()));
        System.out.print("Wybór: ");
    }
}