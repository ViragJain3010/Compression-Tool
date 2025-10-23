import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class CharCount {
    private final String PathToFile;

    public CharCount(String PathToFile) {
        this.PathToFile = PathToFile;
    }

    /**
     * Generates a frequency table of characters in the file.
     * 
     * - Paths.get(pathToFile) → creates a Path object representing the file system
     * path.
     * - Files.lines(...) → reads all lines from the file as a Stream of strings.
     * - String::chars is a method reference. For a given String, it returns an
     * IntStream of Unicode code points (the integer representation of each
     * character).
     * Example: "Hi!".chars() // returns IntStream.of(72, 105, 33)
     * - The .flatMapToInt() takes each element of the outer stream (Stream<String>)
     * and applies the function (String::chars) that produces an IntStream for each
     * string.
     * Then, it flattens these IntStreams into one continuous IntStream of all
     * character codes from all lines.
     * - .filter(Character::isLetterOrDigit) → keeps only alphanumeric characters
     * (ignores spaces, punctuation, etc.).
     * - .map(Character::toLowerCase) → converts each character to lowercase to
     * ensure
     * case-insensitive counting.
     * - .forEach(...) → iterates over each character in the stream and updates the
     * frequency count in the charCount map.
     * - If a character doesn’t exist in the map, it’s inserted with value 1.
     * - If a character already exists, then `Integer::sum` combines the existing
     * value
     * with the new one → effectively oldValue + 1.
     * 
     * @param pathToFile Path to the file
     * @return Map with character as key and its frequency as value
     */
    public Map FrequencyTable() {
        Map<Character, Integer> charCount = new HashMap<>();
        try (Stream<String> lines = Files.lines(Paths.get(PathToFile))) {
            lines.flatMapToInt(String::chars)
                    .filter(Character::isLetterOrDigit)
                    .map(Character::toLowerCase)
                    .forEach(c -> charCount.merge((char) c, 1, Integer::sum));
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }

        return charCount;
    }
}
