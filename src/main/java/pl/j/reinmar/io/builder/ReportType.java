package pl.j.reinmar.io.builder;

/**
 * Określa typ i zakres zawartości generowanego raportu z analizy tekstu.
 * <p>
 * Warianty tego typu wyliczeniowego są wykorzystywane m.in. przez {@link ReportBuilder}
 * do określenia, które sekcje statystyk (podstawowe, częstotliwościowe czy pełne)
 * powinny zostać włączone do wyjściowego dokumentu.
 * </p>
 *
 * @author Sławek Reinmar
 * @version 1.0
 */
public enum ReportType {

    /**
     * Raport podstawowy – zawiera wyłącznie ogólne statystyki ilościowe tekstu (znaki, słowa, zdania).
     */
    BASIC,

    /**
     * Pełny raport – zawiera zarówno podstawowe statystyki ilościowe, jak i zestawienie częstotliwości słów.
     */
    FULL,

    /**
     * Raport częstotliwościowy – zawiera wyłącznie analizę częstotliwości występowania poszczególnych słów.
     */
    FREQUENCY
}