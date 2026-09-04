package pl.j.reinmar.io;


import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class FileReader {

    /**
     * Odczytuje plik tekstowy z katalogu zasobów (Resources), zwraca jego zawartość jako ciąg znaków (String) z UTF-8
     * @param path nazwa pliku w zasobach (np. "file.txt")
     * @return zawartość pliku jako String
     */
    public static String readResource(String path) throws IOException {

        InputStream in = FileReader.class
                .getClassLoader()
                .getResourceAsStream(path);

        if (in == null) {
            throw new RuntimeException("Zasób został nieznaleziony: " + path);
        }
        try (InputStream input = in) {
            return new String(input.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Błąd ładowania zasobów: " + path, e);
        }
    }
}