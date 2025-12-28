package src.test.java.compressor;

import org.junit.jupiter.api.Test;
import src.main.java.compressor.*;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class HuffmanTreeTest {

    @Test
    void emptyFrequencyTable_createsNullRoot() {
        HuffmanTree tree = new HuffmanTree(Map.of());
        assertNull(tree.getRoot());
    }

    @Test
    void singleSymbol_createsSingleLeafNode() {
        HuffmanTree tree = new HuffmanTree(Map.of(65, 10));

        assertNotNull(tree.getRoot());
        assertTrue(tree.getRoot().isLeaf());
        assertEquals(10, tree.getRoot().getWeight());
    }

    @Test
    void multipleSymbols_produceValidTree() {
        Map<Integer, Integer> freq = Map.of(
                1, 5,
                2, 9,
                3, 12
        );

        HuffmanTree tree = new HuffmanTree(freq);

        assertNotNull(tree.getRoot());
        assertFalse(tree.getRoot().isLeaf());
        assertEquals(26, tree.getRoot().getWeight());
    }

    @Test
    void prefixTable_isPrefixFree() {
        Map<Integer, Integer> freq = Map.of(
                1, 5,
                2, 7,
                3, 10,
                4, 15
        );

        HuffmanTree tree = new HuffmanTree(freq);

        for (String a : tree.getPrefixTable().values()) {
            for (String b : tree.getPrefixTable().values()) {
                if (!a.equals(b)) {
                    assertFalse(b.startsWith(a));
                }
            }
        }
    }
}
