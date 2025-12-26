package src.test.java.compressor;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import src.main.java.compressor.FileValidator;
import src.main.java.compressor.HuffmanBaseNode;
import src.main.java.compressor.HuffmanTree;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class FileValidatorTest {

    @TempDir
    Path tempDir;

    @Test
    void validFile_doesNotThrow() throws IOException {
        Path file = tempDir.resolve("valid.txt");
        Files.writeString(file, "content");

        assertDoesNotThrow(() -> FileValidator.validate(file));
    }

    @Test
    void nonExistentFile_throwsException() {
        Path missing = tempDir.resolve("nope.txt");

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> FileValidator.validate(missing));
        assertTrue(ex.getMessage().contains("doesn't exist"));
    }

    @Test
    void directory_throwsException() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> FileValidator.validate(tempDir));
        assertTrue(ex.getMessage().contains("directory"));
    }

    @Test
    void nonReadableFile_throwsException() throws IOException {
        Path file = tempDir.resolve("secret.txt");
        Files.writeString(file, "hidden");
        Files.setPosixFilePermissions(file, java.nio.file.attribute.PosixFilePermissions.fromString("---------"));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> FileValidator.validate(file));
        assertTrue(ex.getMessage().contains("not readable") || ex.getMessage().contains("permissions"));
    }

    static class HuffmanTreeEdgeCasesTest {

        @Test
        void buildTree_emptyFrequencyTable_returnsNull() {
            Map<Character, Integer> empty = new HashMap<>();
            assertNull(HuffmanTree.buildHuffmanTree(empty));
        }

        @Test
        void buildTree_twoEqualFrequencies_createsBalancedTree() {
            Map<Character, Integer> freq = Map.of('a', 10, 'b', 10);

            HuffmanBaseNode root = HuffmanTree.buildHuffmanTree(freq);

            assertEquals(20, root.getWeight());
            assertNotNull(root.getLeft());
            assertNotNull(root.getRight());
            assertTrue(root.getLeft().isLeaf() || root.getRight().isLeaf());
        }

        @Test
        void buildPrefixTable_veryLongCodes_stillValid() {
            Map<Character, Integer> freq = new HashMap<>();
            for (int i = 0; i < 20; i++) {
                freq.put((char) ('a' + i), 1 << (19 - i)); // very skewed
            }

            HuffmanTree table = new HuffmanTree(freq);

            for (String code1 : table.prefixTable.values()) {
                for (String code2 : table.prefixTable.values()) {
                    if (!code1.equals(code2)) {
                        assertFalse(code2.startsWith(code1));
                    }
                }
            }
        }
    }
}