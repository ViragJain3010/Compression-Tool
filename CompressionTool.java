import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class CompressionTool {

    public static void main(String[] args) {
        if(args.length == 0){
            System.out.println("No file path provided");
        }else if(args.length == 1){
            File file = new File(args[0]);
            if (!file.exists()){
                System.err.println("File doesnt exist at the provided path.");
            }else if(!file.canRead()){
                System.err.println("Error reading the file. Please check permissions.");
            }else if(!file.isFile()){
                System.err.println("Given path is a directory.");
            }else{
                Map<Character, Integer> result = FrequencyTable(args[0]);
                System.out.println(result.get('t'));
            }
        } else {
            System.err.println("Invalid arguments.");
        }
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
    private static Map FrequencyTable(String PathToFile) {
        Map<Character, Integer> charCount = new HashMap<>();
        try (Stream<String> lines = Files.lines(Paths.get(PathToFile))) {
            lines.flatMapToInt(String::chars)
                    .filter(Character::isLetterOrDigit)
                    // .map(Character::toLowerCase)
                    .forEach(c -> charCount.merge((char) c, 1, Integer::sum));
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }

        return charCount;
    }
}