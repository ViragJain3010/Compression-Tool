// This file is responsible for Tree manipulation algorithms
package src.main.java.compressor;

import java.util.*;
import src.main.java.compressor.HuffmanBaseNode;
import src.main.java.compressor.HuffmanInternalNode;
import src.main.java.compressor.HuffmanLeafNode;

/**
 * Builds a Huffman tree from a frequency table.
 */
public class HuffmanTree {

    public static HuffmanBaseNode buildTree(Map<Character, Integer> freqTable) {
        PriorityQueue<HuffmanBaseNode> pq = new PriorityQueue<>(Comparator.comparingLong(HuffmanBaseNode::getWeight));
        // Intialize the priority queue with leaf nodes
        for (Map.Entry<Character, Integer> entry : freqTable.entrySet()) {
            pq.add(new HuffmanLeafNode(entry.getKey(), entry.getValue()));
        }

        while (pq.size()>1){
            HuffmanBaseNode node1 = pq.poll();
            HuffmanBaseNode node2 = pq.poll();
            HuffmanInternalNode parent = new HuffmanInternalNode(node1, node2);
            pq.add(parent);
        }

        return pq.poll(); // The root of the Huffman tree
    }

    public static Map<> buildPrefixTable(HuffmanBaseNode root){
        Map<Character, String> prefixTable = new HashMap<>();
        buildPrefixTableHelper(root, prefixTable);
        return prefixTable;
    }

    public static void buildPrefixTableHelper(HuffmanBaseNode root, String prefixCode, Map<Character, String> prefixTable){
        if(root.isLeaf()) return;


    }
}
