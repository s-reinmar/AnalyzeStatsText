package pl.j.reinmar.app.menu.actions;

import pl.j.reinmar.app.menu.MenuAction;
import pl.j.reinmar.ui.UserInput;

public class ChangeMinWordLengthAction implements MenuAction {

    private final UserInput input;
    private final int[] minWordLengthRef;

    public ChangeMinWordLengthAction(UserInput input, int[] minWordLengthRef) {
        this.input = input;
        this.minWordLengthRef = minWordLengthRef;
    }

    @Override
    public void execute() {
        minWordLengthRef[0] = input.askMinWordLength(minWordLengthRef[0]);
    }

    @Override
    public String label() {
        return "4) Zmień minWordLength";
    }
}