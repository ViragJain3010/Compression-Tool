package src.test.java.compressor;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import src.main.java.compressor.CompressionTool;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class DecoderIntegrationTest {

    @TempDir
    Path tempDir;

    @Test
    void compressThenDecode_restoresOriginalFile() throws Exception {
        Path input = tempDir.resolve("sample.txt");
        Files.writeString(input, "huffman decoding works!");

        CompressionTool tool = new CompressionTool();

        tool.run(new String[]{input.toString()});

        Path compressed = tempDir.resolve("sample.cx");
        assertTrue(Files.exists(compressed));

        tool.run(new String[]{compressed.toString()});

        Path restored = tempDir.resolve("sample.txt");
        assertTrue(Files.exists(restored));

        assertEquals(
                "huffman decoding works!",
                Files.readString(restored)
        );
    }
}
