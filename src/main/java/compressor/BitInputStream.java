package src.main.java.compressor;

import java.io.IOException;
import java.io.InputStream;

public class BitInputStream implements AutoCloseable {

    private final InputStream in;
    private int currentByte = 0;
    private int numBitsRemaining = 0;

    public BitInputStream(InputStream in) {
        this.in = in;
    }

    /**
     * Reads a single bit (0 or 1).
     * @return bit value, or -1 if end of stream
     */
    public int readBit() throws IOException {
        if (numBitsRemaining == 0) {
            currentByte = in.read();
            if (currentByte == -1) {
                return -1;
            }
            numBitsRemaining = 8;
        }

        numBitsRemaining--;
        return (currentByte >> numBitsRemaining) & 1;
    }

    @Override
    public void close() throws IOException {
        in.close();
    }
}
