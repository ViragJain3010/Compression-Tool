package src.main.java.compressor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Responsible for validating file paths before processing.
 */
public class FileValidator {

    public static void validate(Path path) throws IOException, IllegalArgumentException {
        if (!Files.exists(path)) {
            throw new IllegalArgumentException("File doesn't exist at the provided path.");
        }
        if (Files.isDirectory(path)) {
            throw new IllegalArgumentException("Given path is a directory, not a file.");
        }
        if (!Files.isReadable(path)) {
            throw new IllegalArgumentException("File is not readable. Check permissions.");
        }
    }
}