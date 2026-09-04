package pl.j.reinmar.io.util;

public class Escape {

    /**
     * Escapuje tekst na potrzeby formatu CSV.
     * <p>Zasady:</p>
     * <ul>
     *     <li>jeśli tekst zawiera przecinek, cudzysłów lub znak nowej linii,
     *         zostaje ujęty w cudzysłowy,</li>
     *     <li>podwójne cudzysłowy są zastępowane sekwencją <code>""</code>.</li>
     * </ul>
     *
     * @param s tekst wejściowy
     * @return tekst poprawnie escapowany dla CSV
     */
    public static String csv(String s) {
        if (s == null) return "";
        boolean need = s.contains(",") || s.contains("\"") || s.contains("\n");
        String esc = s.replace("\"", "\"\"");
        return need ? "\"" + esc + "\"" : esc;
    }

    /**
     * Escapuje tekst na potrzeby formatu JSON.
     * <p>Konwertowane są m.in.:</p>
     * <ul>
     *     <li>backslash → <code>\\\\</code>,</li>
     *     <li>cudzysłów → <code>\\\"</code>.</li>
     * </ul>
     *
     * @param s tekst wejściowy
     * @return tekst poprawnie escapowany dla JSON
     */
    public static String json(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\")
                .replace("\"", "\\\"");
    }

    /**
     * Escapuje tekst na potrzeby formatu XML.
     * <p>Konwertowane są m.in.:</p>
     * <ul>
     *     <li>& → <code>&amp;</code></li>
     *     <li>< → <code>&lt;</code></li>
     *     <li>> → <code>&gt;</code></li>
     *     <li>" → <code>&quot;</code></li>
     * </ul>
     *
     * @param s tekst wejściowy
     * @return tekst poprawnie escapowany dla XML
     */
    public static String xml(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;");
    }
}