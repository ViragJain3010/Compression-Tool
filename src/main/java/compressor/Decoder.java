package src.main.java.compressor;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Files;

import static java.lang.System.out;

public class Decoder {

    private final Path compressedPath;
    private long originalFileSize;

    public Decoder(Path compressedPath) {
        this.compressedPath = compressedPath;
    }

    public void runDecoder() throws IOException {
        out.println("1. Running Decoder");

        try (
                FileInputStream fis = new FileInputStream(compressedPath.toFile());
                DataInputStream dis = new DataInputStream(fis);
                BitInputStream bis = new BitInputStream(dis)
        ) {
            // 1. Read header
            String extension = readHeader(dis);

            // 2. Read Huffman tree
            out.println("2. Reading Huffman Tree...");
            HuffmanBaseNode root = readTreePreorder(bis);
            out.println("   Huffman Tree reconstructed.");

            // 3. Decode data
            Path outputPath = buildOutputPath(extension);
            decodeData(bis, root, outputPath);

            out.println("4. Decoding Complete");
            out.println("   Output written to: " + outputPath);
        }
    }

    /**
     * Reads:
     * - extension length
     * - extension bytes
     * - original file size
     */
    private String readHeader(DataInputStream dis) throws IOException {
        out.println("   Reading Header...");

        int extLength = dis.readInt();
        byte[] extBytes = new byte[extLength];
        dis.readFully(extBytes);

        String extension = new String(extBytes);
        originalFileSize = dis.readLong();

        out.println("   Header read:");
        out.println("      extension = '" + extension + "'");
        out.println("      originalSize = " + originalFileSize + " bytes");

        return extension;
    }

    /**
     * Reconstructs Huffman tree from preorder bit stream.
     * Leaf: 1 + 8-bit symbol
     * Internal: 0
     */
    private HuffmanBaseNode readTreePreorder(BitInputStream bis) throws IOException {
        int bit = bis.readBit();
        if (bit == -1) {
            throw new EOFException("Unexpected EOF while reading Huffman tree");
        }

        // case 1: Leaf node
        if (bit == 1) {
            int symbol = 0;
            for (int i = 0; i < 8; i++) {
                symbol = (symbol << 1) | bis.readBit();
            }
            return new HuffmanLeafNode(symbol, 0);
        }

        // case 2: Internal node
        HuffmanBaseNode left = readTreePreorder(bis);
        HuffmanBaseNode right = readTreePreorder(bis);
        return new HuffmanInternalNode(left, right);
    }

    /**
     * Decodes Huffman-coded data until originalFileSize bytes are produced.
     */
    private void decodeData(BitInputStream bis,
                            HuffmanBaseNode root,
                            Path outputPath) throws IOException {

        out.println("3. Decoding Data...");

        try (OutputStream outStream = Files.newOutputStream(outputPath)) {

            long bytesWritten = 0;
            HuffmanBaseNode current = root;

            while (bytesWritten < originalFileSize) {
                int bit = bis.readBit();
                if (bit == -1) {
                    throw new EOFException("Unexpected EOF during data decoding");
                }

                current = (bit == 0)
                        ? current.getLeft()
                        : current.getRight();

                if (current.isLeaf()) {
                    int symbol = ((HuffmanLeafNode) current).getSymbol();
                    outStream.write(symbol);
                    bytesWritten++;
                    current = root;
                }
            }
        }

        out.println("   Decoded Data written successfully.");
    }

    /**
     * Builds output file path using original extension.
     */
    private Path buildOutputPath(String extension) {
        String fileName = compressedPath.getFileName().toString();
        int dot = fileName.lastIndexOf('.');

        String baseName = (dot == -1)
                ? fileName
                : fileName.substring(0, dot);

        if (!extension.isEmpty()) {
            baseName += "." + extension;
        }

        return compressedPath.resolveSibling(baseName);
    }
}
