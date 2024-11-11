package edu.princeton.cs.algs4;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class BinaryStdIn {
    private InputStream in;
    private int buffer;    // Buffer to store one byte
    private int n;         // Number of bits remaining in the buffer

    public BinaryStdIn(InputStream inputStream) {
        this.in = inputStream;
        fillBuffer();
    }

    private void fillBuffer() {
        try {
            buffer = in.read();
            n = 8;  // Buffer now has 8 bits available
        } catch (IOException e) {
            buffer = -1;  // End of file
            n = -1;
        }
    }

    // Read an integer with the specified bit width
    public int readInt(int width) throws IOException {
        int x = 0;
        for (int i = 0; i < width; i++) {
            if (buffer == -1) throw new IOException("End of file");

            // Add the current bit to the integer
            x = (x << 1) | ((buffer >> (n - 1)) & 1);
            n--;

            // Refill the buffer if all bits are read
            if (n == 0) fillBuffer();
        }
        return x;
    }

    public void close() throws IOException {
        in.close();
    }
}
