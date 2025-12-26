package src.test.java.compressor;

import org.junit.jupiter.api.Test;
import src.main.java.compressor.CompressionTool;
import src.main.java.compressor.Encoder;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class EncoderTest {
    @Test
    void countsCharactersCorrectly() throws Exception {
        Path tempFile = Files.createTempFile("testfile", ".txt");
        Files.writeString(tempFile, "aaAbBcC123!!");

        Encoder tool = new Encoder(tempFile);
        var freq = tool.buildFrequencyTable();

        assertEquals(2, freq.get('a'));
        assertEquals(1, freq.get('A'));
        assertEquals(1, freq.get('b'));
        assertEquals(1, freq.get('B'));
        assertEquals(1, freq.get('c'));
        assertEquals(1, freq.get('C'));
        assertEquals(1, freq.get('1'));
        assertEquals(1, freq.get('2'));
        assertEquals(1, freq.get('3'));

        Files.deleteIfExists(tempFile);
    }
}
