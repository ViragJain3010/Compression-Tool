package src.test.java.compressor;

import org.junit.jupiter.api.Test;
import src.main.java.compressor.FileValidator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class FileValidatorTest {

    @Test
    void validFile_passesValidation() throws IOException {
        Path file = Files.createTempFile("valid", ".txt");
        Files.writeString(file, "hello");

        assertDoesNotThrow(() -> FileValidator.validate(file));
    }

    @Test
    void nonExistentFile_throwsException() {
        Path path = Path.of("missing-file.txt");

        IllegalArgumentException ex =
                assertThrows(IllegalArgumentException.class,
                        () -> FileValidator.validate(path));

        assertTrue(ex.getMessage().toLowerCase().contains("exist"));
    }

    @Test
    void directoryPath_throwsException() throws IOException {
        Path dir = Files.createTempDirectory("folder");

        IllegalArgumentException ex =
                assertThrows(IllegalArgumentException.class,
                        () -> FileValidator.validate(dir));

        assertTrue(ex.getMessage().toLowerCase().contains("directory"));
    }
}
