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
    void printsFrequencyOfCommonCharacter() throws Exception {
        Path file = tempDir.resolve("test.txt");
        Files.writeString(file, "hello world this is a test");

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayOutputStream err = new ByteArrayOutputStream();

        CompressionTool tool = new CompressionTool(new PrintStream(out), new PrintStream(err));
        tool.run(new String[]{file.toString()});

        String output = out.toString();
        assertTrue(output.contains("Frequency of 't' = 4"));
        assertTrue(err.toString().isEmpty());
    }

    @Test
    void handlesEmptyFile() throws Exception {
        Path file = tempDir.resolve("empty.txt");
        Files.createFile(file);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayOutputStream err = new ByteArrayOutputStream();

        CompressionTool tool = new CompressionTool(new PrintStream(out), new PrintStream(err));
        tool.run(new String[]{file.toString()});

        assertTrue(out.toString().contains("Frequency of 't' = 0"));
    }
}