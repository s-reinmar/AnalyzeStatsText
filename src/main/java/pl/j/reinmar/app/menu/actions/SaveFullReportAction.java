package pl.j.reinmar.app.menu.actions;

import pl.j.reinmar.app.Settings;
import pl.j.reinmar.app.menu.MenuAction;
import pl.j.reinmar.io.ReportWriter;
import pl.j.reinmar.io.builder.ReportType;
import pl.j.reinmar.model.WordSort;
import pl.j.reinmar.ui.ReportSaver;
import pl.j.reinmar.ui.UserInput;

import java.nio.file.Path;

public class SaveFullReportAction implements MenuAction {

    private final ReportSaver saver;
    private final UserInput input;
    private final String path;
    private final Settings settings;

    public SaveFullReportAction(ReportSaver saver, UserInput input, String path, Settings settings) {
        this.saver = saver;
        this.input = input;
        this.path = path;
        this.settings = settings;
    }

    @Override
    public void execute() {
        Path output = input.askOutputPath("full_report.txt");
        ReportWriter.Format format = input.askReportFormat();
        WordSort sort = input.askSortMode();
        saver.saveReport(output, path, ReportType.FULL, settings.getStopWords(),
                settings.getMinWordLength(), sort, 0, format);
    }

    @Override
    public String label() {
        return "7) Zapisz pełne statystyki";
    }
}