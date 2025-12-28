package src.test.java.compressor;

import org.junit.jupiter.api.Test;
import src.main.java.compressor.HuffmanTree;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class HuffmanTreeEdgeCasesTest {

    @Test
    void verySkewedFrequenciesStillWork() {
        Map<Integer, Integer> freq = new HashMap<>();

        for (int i = 0; i < 20; i++) {
            freq.put(i, 1 << (19 - i));
        }

        HuffmanTree tree = new HuffmanTree(freq);

        assertNotNull(tree.getRoot());
        assertEquals(freq.size(), tree.getPrefixTable().size());
    }
}
