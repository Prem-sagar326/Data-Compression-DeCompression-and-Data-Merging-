package edu.princeton.cs.algs4;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class BinaryStdOut {
    private OutputStream out;
    private int buffer;    // 8-bit buffer of bits to write out
    private int n;         // number of bits remaining in buffer

    public BinaryStdOut(OutputStream outputStream) {
        this.out = outputStream;
        buffer = 0;
        n = 0;
    }

    // Write an integer x to the output stream with the specified bit width
    public void write(int x, int width) throws IOException {
        for (int i = 0; i < width; i++) {
            // Add the bit from the integer x to the buffer
            buffer = (buffer << 1) | ((x >> (width - i - 1)) & 1);
            n++;

            // If the buffer is full (8 bits), write it out as a single byte
            if (n == 8) {
                out.write(buffer);
                buffer = 0;
                n = 0;
            }
        }
    }

    // Write a string to the output stream (for writing expanded text directly)
    public void write(String s) throws IOException {
        out.write(s.getBytes());
    }

    // Flush the remaining bits in the buffer
    public void close() throws IOException {
        if (n > 0) {
            buffer <<= (8 - n);
            out.write(buffer);
        }
        out.close();
    }
}
