package pl.j.reinmar.app.menu;

public enum MenuOption {
    BASIC_STATS("1"),
    TOP_WORDS("2"),
    FREQUENCY_FRAGMENT("3"),
    CHANGE_MIN_LENGTH("4"),
    TOGGLE_STOPWORDS("5"),
    SAVE_BASIC("6"),
    SAVE_FULL("7"),
    SAVE_FREQUENCY("8"),
    EXIT("0");

    public final String key;

    MenuOption(String key) {
        this.key = key;
    }

    public static MenuOption fromKey(String key) {
        for (MenuOption o : values()) {
            if (o.key.equals(key)) return o;
        }
        return null;
    }
}