package pl.j.reinmar.app.menu.actions;

import pl.j.reinmar.app.Settings;
import pl.j.reinmar.app.menu.MenuAction;
import pl.j.reinmar.io.ReportWriter;
import pl.j.reinmar.io.builder.ReportType;
import pl.j.reinmar.ui.ReportSaver;
import pl.j.reinmar.ui.UserInput;

import java.nio.file.Path;

public class SaveBasicReportAction implements MenuAction {

    private final ReportSaver saver;
    private final UserInput input;
    private final String path;
    private final Settings settings;

    public SaveBasicReportAction(ReportSaver saver, UserInput input, String path, Settings settings) {
        this.saver = saver;
        this.input = input;
        this.path = path;
        this.settings = settings;
    }

    @Override
    public void execute() {
        ReportWriter.Format format = input.askReportFormat();
        Path output = input.askOutputPath("basic_report.txt", format);
        saver.saveReport(output, path, ReportType.BASIC, settings.getStopWords(),
                settings.getMinWordLength(), null, 0, format);
    }

    @Override
    public String label() {
        return "6) Zapisz podstawowe statystyki";
    }
}