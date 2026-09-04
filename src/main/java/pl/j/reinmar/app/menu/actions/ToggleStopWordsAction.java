package pl.j.reinmar.app.menu.actions;

import pl.j.reinmar.app.Settings;
import pl.j.reinmar.app.menu.MenuAction;

public class ToggleStopWordsAction implements MenuAction {

    private final Settings settings;

    public ToggleStopWordsAction(Settings settings) {
        this.settings = settings;
    }

    @Override
    public void execute() {
        settings.toggleStopWords();
    }

    @Override
    public String label() {
        return "5) Włącz/wyłącz stop‑words";
    }
}