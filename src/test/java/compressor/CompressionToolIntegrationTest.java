package src.test.java.compressor;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import src.main.java.compressor.CompressionTool;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class CompressionToolIntegrationTest {

    @TempDir
    Path tempDir;

    @Test
    void runningWithoutArgumentsPrintsError() {
        ByteArrayOutputStream err = new ByteArrayOutputStream();
        CompressionTool tool = new CompressionTool(System.out, new PrintStream(err));

        tool.run(new String[]{});

        assertTrue(err.toString().toLowerCase().contains("specify"));
    }

    @Test
    void compressionCreatesCxFile() throws Exception {
        Path input = tempDir.resolve("file.txt");
        Files.writeString(input, "hello compression");

        CompressionTool tool = new CompressionTool();

        tool.run(new String[]{input.toString()});

        assertTrue(Files.exists(tempDir.resolve("file.cx")));
    }
}
