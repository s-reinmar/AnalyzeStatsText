package pl.j.reinmar.app.menu.actions;

import pl.j.reinmar.app.menu.MenuAction;

public class ExitAction implements MenuAction {

    @Override
    public void execute() {
        System.out.println("Koniec. Do zobaczenia!");
        System.exit(0);
    }

    @Override
    public String label() {
        return "0) Wyjście";
    }
}