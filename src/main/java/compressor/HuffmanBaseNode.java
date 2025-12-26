package src.main.java.compressor;

/** Base node for Huffman tree */
public interface HuffmanBaseNode {
    boolean isLeaf();
    int getWeight();

    HuffmanBaseNode getLeft();
    HuffmanBaseNode getRight();
}