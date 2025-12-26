package src.main.java.compressor;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import static java.lang.System.out;

public class Encoder {
    final Path filePath;

    public Encoder(Path filePath) {
        this.filePath = filePath;
    }

    public void runEncoder() throws IOException {
        out.println("1. Running Encoder");
        Map<Integer, Integer> frequencyTable = buildFrequencyTable();

        if (frequencyTable.isEmpty()) {
            throw new IOException("Cannot compress empty file");
        }

        HuffmanTree tree = new HuffmanTree(frequencyTable);
        writeCompressedFile(tree);
        out.println("5. Encoding Complete");
    }

    /**
     * Builds a frequency table for all bytes in the file (streamed).
     */
    public Map<Integer, Integer> buildFrequencyTable() throws IOException {
        out.println("2. Building Frequency Table");
        Map<Integer, Integer> frequencyTable = new HashMap<>();

        try (InputStream in = Files.newInputStream(filePath)) {
            int b;
            while ((b = in.read()) != -1) {
                frequencyTable.merge(b, 1, Integer::sum);
            }
        }
        out.println("   Frequency Table Size: " + frequencyTable.size());
        printFrequencyTable(frequencyTable);
        return frequencyTable;
    }

    /**
     * Prints the frequency table for debugging purposes
     */
    private static void printFrequencyTable(Map<Integer, Integer> frequencyTable) {
        out.println("   Frequency Table Contents:");
        for (Map.Entry<Integer, Integer> entry : frequencyTable.entrySet()) {
            out.printf("      Byte: %d Frequency: %d%n", entry.getKey(), entry.getValue());
        }
    }

    /**
     * Function to create and write compressed file
     */
    private void writeCompressedFile(HuffmanTree tree) throws IOException {
        out.println("4. Writing Compressed File  ---writeCompressedFile---");

        String fileName = filePath.getFileName().toString();
        int dot = fileName.lastIndexOf('.');

        String baseName = (dot == -1)
                ? fileName
                : fileName.substring(0, dot);

        Path outputPath = filePath.resolveSibling(baseName + ".cx");

        try (
                FileOutputStream fos = new FileOutputStream(outputPath.toFile());
                DataOutputStream dos = new DataOutputStream(fos);
                BitOutputStream bos = new BitOutputStream(dos)
        ) {
            writeHeader(dos);
            out.println("   Writing Huffman Tree Preorder traversal...");
            writeTreePreorder(tree.getRoot(), bos);
            out.println("   Huffman Tree written successfully.");
            writeEncodedData(bos, tree.getPrefixTable());

            int validBits = bos.flush();
            dos.writeByte(validBits);
        }
        out.println("   Compressed file written to: " + outputPath);
    }

    /**
     * Header:
     * - extension length (int)
     * - extension bytes
     * - original file size in bytes (long)
     * - Huffman tree (preorder)
     */
    private void writeHeader(DataOutputStream dos) throws IOException {
        out.println("   Writing Header...");

        String fileName = filePath.getFileName().toString();
        String extension = "";

        int dot = fileName.lastIndexOf('.');
        if (dot != -1) {
            extension = fileName.substring(dot + 1);
        }

        byte[] extBytes = extension.getBytes();

        // 1. Extension
        dos.writeInt(extBytes.length);
        dos.write(extBytes);

        // 2. Original file size
        long originalSize = Files.size(filePath);
        dos.writeLong(originalSize);

        out.println("   Header written:");
        out.println("      extension = '" + extension + "'");
        out.println("      originalSize = " + originalSize + " bytes");
    }


    /**
     * Preorder tree serialization.
     * Leaf: 1 + 8-bit symbol
     * Internal: 0
     */
    private void writeTreePreorder(HuffmanBaseNode node, BitOutputStream bos) throws IOException {
        if (node.isLeaf()) {
            bos.writeBit(1);
            int symbol = ((HuffmanLeafNode) node).getSymbol();
            for (int i = 7; i >= 0; i--) {
                bos.writeBit((symbol >> i) & 1);
            }
        } else {
            bos.writeBit(0);
            writeTreePreorder(node.getLeft(), bos);
            writeTreePreorder(node.getRight(), bos);
        }
    }

    /**
     * Streams input file and writes Huffman codes.
     */
    private void writeEncodedData(BitOutputStream bos, Map<Integer, String> prefixTable) throws IOException {
        out.println("   Writing Encoded Data...");
        try (InputStream in = Files.newInputStream(filePath)) {
            int b;
            while ((b = in.read()) != -1) {
                bos.writeBits(prefixTable.get(b));
            }
        }
        out.println("   Encoded Data written successfully.");
    }
}
