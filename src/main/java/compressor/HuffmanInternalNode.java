package src.main.java.compressor;

public class HuffmanInternalNode implements HuffmanBaseNode {
    private final long weight;
    private final HuffmanBaseNode left;
    private final HuffmanBaseNode right;

    public HuffmanInternalNode(HuffmanBaseNode left, HuffmanBaseNode right) {
        this.left = left;
        this.right = right;
        this.weight = left.getWeight() + right.getWeight();
    }

    public long getWeight() {
        return weight;
    }

    public HuffmanBaseNode getLeft() {
        return left;
    }

    public HuffmanBaseNode getRight() {
        return right;
    }

    public boolean isLeaf() {
        return false;
    }
}
