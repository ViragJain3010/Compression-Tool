package src.main.java.compressor;

/**
 * Leaf Node Class for Huffman Tree
 * Character and frequency
 */
public class HuffmanLeafNode implements HuffmanBaseNode {
    private final int symbol;
    private final int weight;

    public HuffmanLeafNode(int symbol, int weight){
        this.symbol = symbol;
        this.weight = weight;
    }

    public int getSymbol() {
        return symbol;
    }

    public int getWeight() {
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