package src.main.java.compressor;

import java.util.*;

import static java.lang.System.out;

/**
 * Builds a Huffman tree and prefix-table from a byte-frequency table.
 * Symbols are integers in the range 0–255.
 */
public class HuffmanTree {

    private final HuffmanBaseNode root;
    private final Map<Integer, String> prefixTable;

    public HuffmanBaseNode getRoot() {return root;}

    public Map<Integer, String> getPrefixTable() {return prefixTable;}

    public HuffmanTree(Map<Integer, Integer> freqTable) {
        this.root = buildHuffmanTree(freqTable);
        this.prefixTable = buildPrefixTable(this.root);
    }

    /*
     * Builds the Huffman tree from the frequency table.
     */
    private static HuffmanBaseNode buildHuffmanTree(Map<Integer, Integer> freqTable) {
        out.println("3. Building Huffman Tree...");
        PriorityQueue<HuffmanBaseNode> pq = new PriorityQueue<>(Comparator.comparingInt(HuffmanBaseNode::getWeight));

        // Initialize priority queue with leaf nodes
        for (Map.Entry<Integer, Integer> entry : freqTable.entrySet()) {
            pq.add(new HuffmanLeafNode(entry.getKey(), entry.getValue()));
        }

        // Edge case empty file
        if (pq.isEmpty()) {
            return null;
        }

        while (pq.size() > 1) {
            HuffmanBaseNode left = pq.poll();
            HuffmanBaseNode right = pq.poll();
            pq.add(new HuffmanInternalNode(left, right));
        }
        out.println("   Huffman Tree built successfully.");
        return pq.poll();
    }

    /*
     * Builds the prefix table from the Huffman tree.
     */
    private static Map<Integer, String> buildPrefixTable(HuffmanBaseNode root) {
        out.println("3. Building Prefix Table...");
        Map<Integer, String> prefixTable = new HashMap<>();
        if (root != null) {
            buildPrefixTableHelper(root, "", prefixTable);
        }
        out.println("   Prefix Table built successfully.");
        printPrefixTable(prefixTable);
        return prefixTable;
    }

    /* Prints the prefix table for debugging purposes */
    private static void printPrefixTable(Map<Integer, String> prefixTable) {
        out.println("   Prefix Table Contents:");
        for (Map.Entry<Integer, String> entry : prefixTable.entrySet()) {
            out.printf("      Byte: %d Code: %s%n", entry.getKey(), entry.getValue());
        }
    }

    /* Recursive helper to build the prefix table */
    private static void buildPrefixTableHelper(HuffmanBaseNode node,String prefixCode,Map<Integer, String> prefixTable) {
        if (node.isLeaf()) {
            HuffmanLeafNode leaf = (HuffmanLeafNode) node;
            // If the file has only one unique byte, Huffman would otherwise assign an empty code
            prefixTable.put(leaf.getSymbol(), prefixCode.length() > 0 ? prefixCode : "0");
            return;
        }

        buildPrefixTableHelper(node.getLeft(), prefixCode + "0", prefixTable);
        buildPrefixTableHelper(node.getRight(), prefixCode + "1", prefixTable);
    }
}
