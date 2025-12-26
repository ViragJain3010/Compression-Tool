package src.test.java.compressor;

import org.junit.jupiter.api.Test;
import src.main.java.compressor.CompressionTool;
import src.main.java.compressor.Encoder;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class CompressionToolTest {



    @Test
    void ignoresNonAlphanumericCharacters() throws Exception {
        Path tempFile = Files.createTempFile("testfile", ".txt");
        Files.writeString(tempFile, "hello world!!!");

        Encoder tool = new Encoder(tempFile);
        var freq = tool.buildFrequencyTable();

        assertFalse(freq.containsKey(' '));
        assertFalse(freq.containsKey('!'));
        assertEquals(3, freq.get('l'));

        Files.deleteIfExists(tempFile);
    }

    @Test
    void run_withNoArguments_printsErrorMessage() {
        ByteArrayOutputStream err = new ByteArrayOutputStream();
        CompressionTool tool = new CompressionTool(System.out, new PrintStream(err));

        tool.run(new String[]{});

        assertTrue(err.toString().contains("No file path provided"));
    }

    @Test
    void run_withNonExistentFile_printsError() {
        ByteArrayOutputStream err = new ByteArrayOutputStream();
        CompressionTool tool = new CompressionTool(System.out, new PrintStream(err));

        tool.run(new String[]{"does-not-exist.txt"});

        assertTrue(err.toString().contains("doesn't exist"));
    }
}