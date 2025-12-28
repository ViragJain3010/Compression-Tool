package src.test.java.compressor;

import org.junit.jupiter.api.Test;
import src.main.java.compressor.BitInputStream;
import src.main.java.compressor.BitOutputStream;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

class BitInputOutputStreamTest {

    @Test
    void writesAndReadsBitsCorrectly() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BitOutputStream bos = new BitOutputStream(baos);

        bos.writeBits("10101100");
        bos.flush();
        bos.close();

        byte[] data = baos.toByteArray();

        BitInputStream bis = new BitInputStream(new ByteArrayInputStream(data));

        StringBuilder read = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            read.append(bis.readBit());
        }

        assertEquals("10101100", read.toString());
    }

    @Test
    void partialByteIsPaddedCorrectly() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BitOutputStream bos = new BitOutputStream(baos);

        bos.writeBits("101");
        int validBits = bos.flush();

        assertEquals(3, validBits);
        assertEquals(1, baos.toByteArray().length);
    }
}
