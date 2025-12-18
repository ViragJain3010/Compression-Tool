package src.main.java.compressor;

/**
 * Leaf Node Class for Huffman Tree
 * Character and frequency
 */
public class HuffmanLeafNode implements HuffmanBaseNode {
    private final char element;
    private final long weight;

    public HuffmanLeafNode(char element, long weight){
        this.element = element;
        this.weight = weight;
    }

    public char getElement() {
        return element;
    }

    public long getWeight() {
        return weight;
    }

    public boolean isLeaf() {
        return true;
    }

    public HuffmanBaseNode getLeft() {
        return null;
    }

    public HuffmanBaseNode getRight() {
        return null;
    }
}