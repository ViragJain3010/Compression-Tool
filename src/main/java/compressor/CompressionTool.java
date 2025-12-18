// This is the main file for the compression tool.
package src.main.java.compressor;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

/**
 * CompressionTool: counts character frequencies in a file.
 * Part of a compression pipeline (Step 1).
 */
public class CompressionTool {

    private final PrintStream out;
    private final PrintStream err;

    // Dependency injection for testability
    public CompressionTool(PrintStream out, PrintStream err) {
        this.out = out;
        this.err = err;
    }

    // Default constructor for normal use
    public CompressionTool() {
        this(System.out, System.err);
    }

    public static void main(String[] args) {
        new CompressionTool().run(args);
    }

    public void run(String[] args) {
        if (args.length == 0) {
            err.println("No file path provided");
            return;
        }

        Path path = Path.of(args[0]);
        try {
            validateFile(path);

            Map<Character, Integer> result = buildFrequencyTable(path);

            // Example of output; you can later replace with a real compression call
            out.println("Frequency of 't' = " + result.getOrDefault('t', 0));

        } catch (IllegalArgumentException e) {
            err.println(e.getMessage());
        } catch (IOException e) {
            err.println("Error reading file: " + e.getMessage());
        }
    }

    /** Validate that the given path points to a readable file. */
    private void validateFile(Path path) {
        if (!Files.exists(path)) {
            throw new IllegalArgumentException("File doesn't exist at the provided path.");
        }
        if (!Files.isReadable(path)) {
            throw new IllegalArgumentException("Error reading the file. Please check permissions.");
        }
        if (Files.isDirectory(path)) {
            throw new IllegalArgumentException("Given path is a directory.");
        }
    }

    /** Builds a frequency table of all letters and digits in the given file.
     * @return Map of character frequencies
     */
    public Map<Character, Integer> buildFrequencyTable(Path pathToFile) throws IOException {
        Map<Character, Integer> charCount = new HashMap<>();

        try (Stream<String> lines = Files.lines(pathToFile)) {
            lines.flatMapToInt(String::chars)
                    .filter(Character::isLetterOrDigit)
                    .forEach(c -> charCount.merge((char) c, 1, Integer::sum));
        }

        return charCount;
    }
}
