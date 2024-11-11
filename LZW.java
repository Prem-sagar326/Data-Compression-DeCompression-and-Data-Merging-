// Lempel-Ziv-Welch (LZW)
package edu.princeton.cs.algs4;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class LZW {
    private static final int R = 256;        // number of input chars
    private static final int L = 4096;       // number of codewords = 2^W
    private static final int W = 12;         // codeword width

    // Do not instantiate.
    private LZW() { }

    public static void compress(String inputPath, String outputPath) {
        try {
            // Read the entire input file as a string
            String input = new String(Files.readAllBytes(Paths.get(inputPath)));
            
            // Create FileOutputStream from the path
            FileOutputStream fileOut = new FileOutputStream(outputPath);
            BinaryStdOut out = new BinaryStdOut(fileOut);
            
            TST<Integer> st = new TST<Integer>();

            // Initialize symbol table with input chars
            for (int i = 0; i < R; i++)
                st.put("" + (char) i, i);
            
            int code = R + 1;  // R is codeword for EOF

            while (input.length() > 0) {
                String s = st.longestPrefixOf(input);   // Find max prefix match s.
                out.write(st.get(s), W);               // Print s's encoding.
                int t = s.length();
                if (t < input.length() && code < L)    // Add s to symbol table.
                    st.put(input.substring(0, t + 1), code++);
                input = input.substring(t);            // Scan past s in input.
            }
            out.write(R, W);
            out.close();
            fileOut.close();  // Don't forget to close the FileOutputStream
        } catch (IOException e) {
            System.err.println("Error reading/writing file: " + e.getMessage());
        }
    }

    public static void expand(String inputPath, String outputPath) {
        try {
            // Create FileInputStream for input
            FileInputStream fileIn = new FileInputStream(inputPath);
            BinaryStdIn in = new BinaryStdIn(fileIn);
            
            // Create FileOutputStream for output
            FileOutputStream fileOut = new FileOutputStream(outputPath);
            BinaryStdOut out = new BinaryStdOut(fileOut);

            String[] st = new String[L];
            int i; // next available codeword value

            // Initialize symbol table with input chars
            for (i = 0; i < R; i++)
                st[i] = "" + (char) i;
            st[i++] = "";     // (unused) lookahead for EOF

            int codeword = in.readInt(W);
            if (codeword == R) return;    // expanded message is empty string
            String val = st[codeword];

            while (true) {
                out.write(val);
                codeword = in.readInt(W);
                if (codeword == R) break;
                String s = st[codeword];
                if (i == codeword) s = val + val.charAt(0);   // special case hack
                if (i < L) st[i++] = val + s.charAt(0);
                val = s;
            }
            out.close();
            fileOut.close();  // Close FileOutputStream
            fileIn.close();   // Close FileInputStream
        } catch (Exception e) {
            System.err.println("Error processing file: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        // File paths
        String inputPath = "C:\\\\Users\\\\welcome\\\\OneDrive\\\\Desktop\\\\JAVA\\\\Data.csv";
        String compressedPath = "C:\\\\Users\\\\welcome\\\\OneDrive\\\\Desktop\\\\JAVA\\\\Compressed.bin";
        String decompressedPath = "C:\\\\Users\\\\welcome\\\\OneDrive\\\\Desktop\\\\JAVA\\\\DeCompressed.csv";
        
        // Set to true for compression, false for decompression
        boolean doCompression = false;
        
        try {
            if (doCompression) {
                if (!Files.exists(Paths.get(inputPath))) {
                    System.err.println("Input file does not exist: " + inputPath);
                    return;
                }
                compress(inputPath, compressedPath);
                System.out.println("Compression complete. Output written to: " + compressedPath);
            } else {
                if (!Files.exists(Paths.get(compressedPath))) {
                    System.err.println("Compressed file does not exist: " + compressedPath);
                    return;
                }
                expand(compressedPath, decompressedPath);
                System.out.println("Decompression complete. Output written to: " + decompressedPath);
            }
        } catch (Exception e) {
            System.err.println("Error occurred: " + e.getMessage());
        }
    }
}