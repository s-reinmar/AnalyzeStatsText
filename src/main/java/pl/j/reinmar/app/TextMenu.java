package pl.j.reinmar.app;

import pl.j.reinmar.app.menu.MenuAction;
import pl.j.reinmar.app.menu.MenuOption;
import pl.j.reinmar.ui.UserInput;

import java.util.Map;

public class TextMenu {

    private final UserInput input;
    private final Map<MenuOption, MenuAction> actions;

    public TextMenu(UserInput input, Map<MenuOption, MenuAction> actions) {
        this.input = input;
        this.actions = actions;
    }

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

    private void printMenu() {
        System.out.println("\n=== MENU ===");
        actions.values().forEach(a -> System.out.println(a.label()));
        System.out.print("Wybór: ");
    }
}