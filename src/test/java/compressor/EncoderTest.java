package src.test.java.compressor;

import org.junit.jupiter.api.Test;
import src.main.java.compressor.Encoder;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class EncoderTest {

    @Test
    void frequencyTableCountsBytesCorrectly() throws Exception {
        Path temp = Files.createTempFile("freq", ".txt");
        Files.write(temp, new byte[]{'a', 'a', 'b', 'c', 'c', 'c'});

        Encoder encoder = new Encoder(temp);
        Map<Integer, Integer> freq = encoder.buildFrequencyTable();

        assertEquals(2, freq.get((int) 'a'));
        assertEquals(1, freq.get((int) 'b'));
        assertEquals(3, freq.get((int) 'c'));
    }

    @Test
    void emptyFileThrowsException() throws Exception {
        Path temp = Files.createTempFile("empty", ".txt");

        Encoder encoder = new Encoder(temp);

        assertThrows(Exception.class, encoder::runEncoder);
    }
}
