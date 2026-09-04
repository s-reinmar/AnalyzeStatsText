package pl.j.reinmar.app.menu.actions;

import pl.j.reinmar.app.menu.MenuAction;

import java.util.Set;

public class ToggleStopWordsAction implements MenuAction {

    private final Set<String> stopWords;
    private final Set<String> defaultWords;

    public ToggleStopWordsAction(Set<String> stopWords, Set<String> defaultWords) {
        this.stopWords = stopWords;
        this.defaultWords = defaultWords;
    }

    @Override
    public void execute() {
        if (stopWords.isEmpty()) {
            stopWords.addAll(defaultWords);
            System.out.println("Stop‑words: WŁĄCZONE");
        } else {
            stopWords.clear();
            System.out.println("Stop‑words: WYŁĄCZONE");
        }
    }

    @Override
    public String label() {
        return "5) Włącz/wyłącz stop‑words";
    }
}