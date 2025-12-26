package src.test.java.compressor;

import org.junit.jupiter.api.Test;
import src.main.java.compressor.HuffmanBaseNode;
import src.main.java.compressor.HuffmanTree;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class HuffmanTreeEdgeCasesTest {

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

        HuffmanTree table =new HuffmanTree(freq);

        for (String code1 : table.prefixTable.values()) {
            for (String code2 : table.prefixTable.values()) {
                if (!code1.equals(code2)) {
                    assertFalse(code2.startsWith(code1));
                }
            }
        }
    }
}