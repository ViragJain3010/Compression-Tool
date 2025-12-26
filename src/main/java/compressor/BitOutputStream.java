package src.main.java.compressor;

import java.io.IOException;
import java.io.OutputStream;

public class BitOutputStream implements AutoCloseable {
    private final OutputStream out;
    private int currentByte = 0;
    private int numBitsFilled = 0;
    private int totalBitsWritten = 0;

    public BitOutputStream(OutputStream out) {
        this.out = out;
    }

    public void writeBit(int bit) throws IOException {
        if (bit != 0 && bit != 1)
            throw new IllegalArgumentException("Bit must be 0 or 1");

        currentByte = (currentByte << 1) | bit;
        numBitsFilled++;
        totalBitsWritten++;

        if (numBitsFilled == 8) {
            out.write(currentByte);
            numBitsFilled = 0;
            currentByte = 0;
        }
    }

    public void writeBits(String bits) throws IOException {
        for (char c : bits.toCharArray()) {
            writeBit(c == '1' ? 1 : 0);
        }
    }

    /**
     * Pads remaining bits with 0s and flushes final byte.
     * @return number of valid bits in the final byte (1–8)
     */
    public int flush() throws IOException {
        if (numBitsFilled == 0) return 8;

        int validBits = numBitsFilled;
        currentByte <<= (8 - numBitsFilled);
        out.write(currentByte);
        numBitsFilled = 0;
        currentByte = 0;
        return validBits;
    }

    public void close() throws IOException {
        out.close();
    }
}
