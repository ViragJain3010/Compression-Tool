package src.test.java.compressor;

import org.junit.jupiter.api.Test;
import java.util.Map;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import static org.junit.jupiter.api.Assertions.*;
import src.main.java.compressor.CompressionTool;
import src.main.java.compressor.HuffmanBaseNode;
import src.main.java.compressor.HuffmanTree;

public class CompressionToolTest {

    @Test
    void countsCharactersCorrectly() throws IOException {
        Path tempFile = Files.createTempFile("testfile", ".txt");
        Files.writeString(tempFile, "aAbBcC123!!");

        CompressionTool tool = new CompressionTool();
        Map<Character, Integer> freq = tool.buildFrequencyTable(tempFile);

        assertEquals(1, freq.get('a'));
        assertEquals(1, freq.get('b'));
        assertEquals(1, freq.get('c'));
        assertEquals(1, freq.get('1'));
        assertEquals(1, freq.get('2'));
        assertEquals(1, freq.get('3'));
        assertEquals(1, freq.get('A'));
        assertEquals(1, freq.get('B'));
        assertEquals(1, freq.get('C'));

        Files.deleteIfExists(tempFile);
    }

    @Test
    void ignoresNonAlphanumericCharacters() throws IOException {
        Path tempFile = Files.createTempFile("testfile", ".txt");
        Files.writeString(tempFile, "hello world!!!");

        CompressionTool tool = new CompressionTool();
        Map<Character, Integer> freq = tool.buildFrequencyTable(tempFile);

        assertFalse(freq.containsKey(' '));
        assertFalse(freq.containsKey('!'));
        assertEquals(3, freq.get('l'));

        Files.deleteIfExists(tempFile);
    }

    @Test
    void testSingleSymbolCreatingSingleNode() {

    }

    @Test
    /**
     *              26
     *              / \
     *            12   14
     *           'c'   / \
     *                5   9
     *               'a' 'b'
     */
    void testBuildTree(){
        Map<Character, Integer> freqTable = Map.of(
            'a', 5,
            'b', 9,
            'c', 12
        );

        HuffmanBaseNode root = HuffmanTree.buildTree(freqTable);
        assertNotNull(root);
        assertEquals(26, root.getWeight());
        assertNotNull(root.getLeft());
        assertNotNull(root.getRight());
        assertEquals(root.getWeight(), root.getLeft().getWeight() + root.getRight().getWeight());
        assertFalse(root.isLeaf()); // 26
        assertTrue(root.getLeft().isLeaf()); // 'c', 12
        assertTrue(root.getRight().getLeft().isLeaf()); // 'a', 5
        assertTrue(root.getRight().getLeft().isLeaf()); // 'b', 9

    }
}
