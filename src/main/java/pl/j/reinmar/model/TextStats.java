package pl.j.reinmar.model;

public record TextStats (int charsWithSpaces, int charsWithoutSpaces, int words) {

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("=== STATYSTYKI ===").append(System.lineSeparator());
        sb.append("Słowa: ").append(words).append(System.lineSeparator());
        sb.append("Znaki (ze spacjami): ").append(charsWithSpaces).append(System.lineSeparator());
        sb.append("Znaki (bez spacji): ").append(charsWithoutSpaces).append(System.lineSeparator());
        return sb.toString();
    }
}