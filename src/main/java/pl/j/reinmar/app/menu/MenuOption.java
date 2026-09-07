package pl.j.reinmar.app.menu;

/**
 * Reprezentuje dostępne opcje w menu aplikacji wraz z przypisanymi do nich klawiszami wyboru.
 * <p>
 * Każda wartość enuma odpowiada konkretnemu poleceniu wybieranemu przez użytkownika
 * (np. w interfejsie konsolowym CLI).
 * </p>
 *
 * @author Sławek Reinmar
 * @version 1.0
 */
public enum MenuOption {

    /** Wyświetlenie podstawowych statystyk tekstu. */
    BASIC_STATS("1"),

    /** Wyświetlenie najczęściej występujących słów. */
    TOP_WORDS("2"),

    /** Analiza częstotliwości występowania fragmentów słów. */
    FREQUENCY_FRAGMENT("3"),

    /** Zmiana minimalnej długości słowa uwzględnianego w analizie. */
    CHANGE_MIN_LENGTH("4"),

    /** Przełączenie (włączenie/wyłączenie) filtrowania słów ignorowanych (stop-words). */
    TOGGLE_STOPWORDS("5"),

    /** Zapisanie podstawowego raportu do pliku. */
    SAVE_BASIC("6"),

    /** Zapisanie pełnego raportu do pliku. */
    SAVE_FULL("7"),

    /** Zapisanie raportu częstotliwości do pliku. */
    SAVE_FREQUENCY("8"),

    /** Wyjście z aplikacji. */
    EXIT("0");

    /**
     * Klawisz lub symbol identyfikujący daną opcję w menu konsolowym.
     */
    public final String key;

    /**
     * Tworzy opcję menu i przypisuje do niej klawisz wyboru.
     *
     * @param key znak lub ciąg znaków używany do wyboru opcji
     */
    MenuOption(String key) {
        this.key = key;
    }

    /**
     * Zwraca opcję menu odpowiadającą podanemu klawiszowi wyboru.
     *
     * @param key klawisz wyboru w postaci ciągu znaków
     * @return pasująca wartość {@link MenuOption} lub {@code null}, jeśli opcja nie istnieje
     */
    public static MenuOption fromKey(String key) {
        for (MenuOption o : values()) {
            if (o.key.equals(key)) return o;
        }
        return null;
    }
}