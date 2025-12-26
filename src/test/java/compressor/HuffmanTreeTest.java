package src.test.java.compressor;

import org.junit.jupiter.api.Test;
import src.main.java.compressor.HuffmanBaseNode;
import src.main.java.compressor.HuffmanLeafNode;
import src.main.java.compressor.HuffmanTree;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class HuffmanTreeTest {

    @Test
    void buildTree_createsValidRootWithCorrectWeight() {
        Map<Character, Integer> freqTable = Map.of(
                'a', 5,
                'b', 9,
                'c', 12
        );

        HuffmanBaseNode root = HuffmanTree.buildHuffmanTree(freqTable);

        assertNotNull(root);
        assertEquals(26, root.getWeight());
        assertFalse(root.isLeaf());
        assertNotNull(root.getLeft());
        assertNotNull(root.getRight());
    }

    @Test
    void buildTree_singleSymbol_returnsSingleLeafNode() {
        Map<Character, Integer> freqTable = Map.of('x', 42);

        HuffmanBaseNode root = HuffmanTree.buildHuffmanTree(freqTable);

        assertNotNull(root);
        assertTrue(root.isLeaf());
        assertEquals(42, root.getWeight());

        HuffmanLeafNode leaf = (HuffmanLeafNode) root;
        assertEquals('x', leaf.getElement());
    }

    @Test
    void buildPrefixTable_matchesExpectedCodes() {
        Map<Character, Integer> freqTable = new HashMap<>();
        freqTable.put('E', 120);
        freqTable.put('U', 37);
        freqTable.put('D', 42);
        freqTable.put('L', 42);
        freqTable.put('C', 32);
        freqTable.put('Z', 2);
        freqTable.put('K', 7);
        freqTable.put('M', 24);

        HuffmanTree root = new HuffmanTree(freqTable);
//        Map<Character, String> prefixTable = HuffmanTree.buildPrefixTable(root);

        // 1. Verify prefix-free property
        assertTrue(isPrefixFree(root.prefixTable.values()));

        // 2. Verify weighted path length is optimal (same as reference)
        long expectedWeightedLength = 0;
        expectedWeightedLength += 120 * 1; // E: 1 bit
        expectedWeightedLength += 37 * 3;  // U: 3 bits
        expectedWeightedLength += 42 * 3;  // D: 3 bits
        expectedWeightedLength += 42 * 3;  // L: 3 bits
        expectedWeightedLength += 32 * 4;  // C: 4 bits
        expectedWeightedLength += 2 * 6;   // Z: 6 bits
        expectedWeightedLength += 7 * 6;   // K: 6 bits
        expectedWeightedLength += 24 * 5;  // M: 5 bits

        long actualWeightedLength = calculateWeightedPathLength(root.prefixTable, freqTable);
        assertEquals(expectedWeightedLength, actualWeightedLength, "Optimal weighted path length not achieved");
    }

    /*
     * To check no code is a prefix of any other code.
     * e.g. 0, 01 is not prefix-free since 0 is a prefix of 01
     */
    private boolean isPrefixFree(Collection<String> codes) {
        for (String code1 : codes) {
            for (String code2 : codes) {
                if (!code1.equals(code2) && code2.startsWith(code1)) {
                    return false;
                }
            }
        }
        return true;
    }

    // Helper: compute total weighted path length
    private long calculateWeightedPathLength(Map<Character, String> prefixTable, Map<Character, Integer> freq) {
        long total = 0;
        for (Map.Entry<Character, String> entry : prefixTable.entrySet()) {
            char ch = entry.getKey();
            int length = entry.getValue().length();
            total += (long) freq.get(ch) * length;
        }
        return total;
    }

    @Test
    void buildPrefixTable_producesPrefixFreeCodes() {
        Map<Character, Integer> freqTable = Map.of(
                'a', 5,
                'b', 9,
                'c', 12,
                'd', 13
        );

        HuffmanTree root = new HuffmanTree(freqTable);

        for (String code1 : root.prefixTable.values()) {
            for (String code2 : root.prefixTable.values()) {
                if (!code1.equals(code2)) {
                    assertFalse(code2.startsWith(code1),
                            () -> code1 + " is a prefix of " + code2);
                }
            }
        }
    }
}
