package src.main.java.compressor;

/** Base node for Huffman tree */
public interface HuffmanBaseNode {
    boolean isLeaf();
    long getWeight();

    HuffmanBaseNode getLeft();
    HuffmanBaseNode getRight();
}