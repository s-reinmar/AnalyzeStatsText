package pl.j.reinmar.model;

/**
 * Rekord reprezentujący zliczenie pojedynczego słowa w tekście.
 * <p>
 * Przechowuje parę złożoną z danego słowa oraz liczby określającej,
 * ile razy wystąpiło ono w przeanalizowanym materiale tekstowym.
 * Klasa wykorzystywana jest głównie przy sortowaniu, filtrowaniu
 * oraz prezentacji wyników analizy częstotliwościowej.
 * </p>
 *
 * @param word  słowo podlegające zliczeniu
 * @param count liczba wystąpień słowa w tekście
 *
 * @author Sławek Reinmar
 * @version 1.0
 */
public record WordCount(String word, int count) {
}