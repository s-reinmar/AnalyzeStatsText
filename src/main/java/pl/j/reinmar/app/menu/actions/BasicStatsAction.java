package pl.j.reinmar.app.menu.actions;

import pl.j.reinmar.app.menu.MenuAction;
import pl.j.reinmar.core.TextAnalyzer;
import pl.j.reinmar.ui.StatsPrinter;

public class BasicStatsAction implements MenuAction {

    private final TextAnalyzer analyzer;
    private final StatsPrinter printer;
    private final String path;

    public BasicStatsAction(TextAnalyzer analyzer, StatsPrinter printer, String path) {
        this.analyzer = analyzer;
        this.printer = printer;
        this.path = path;
    }

    @Override
    public void execute() {
        printer.printBasicStats(analyzer, path);
    }

    @Override
    public String label() {
        return "1) Podstawowe statystyki";
    }
}