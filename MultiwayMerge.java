package multiwaymerge;

import java.io.*;
import java.util.*;

public class MultiwayMerge {
    public static void main(String[] args) throws IOException {
        // Prompt the user to select the input files
        File file1 = promptFileSelection("Select the first input file");
        File file2 = promptFileSelection("Select the second input file");

        // Prompt the user to select the output file
        File outputFile = promptFileSelection("Select the output file");

        // Read the CSV files into lists
        List<String> file1Data = readCsvFile(file1.getAbsolutePath());
        List<String> file2Data = readCsvFile(file2.getAbsolutePath());

        // Perform multiway merge
        List<String> mergedData = multiwayMerge(file1Data, file2Data);

        // Write merged data to output file
        writeCsvFile(outputFile.getAbsolutePath(), mergedData);

        System.out.println("Merge complete. Output written to " + outputFile.getAbsolutePath());
    }

    // Method to prompt the user to select a file
    private static File promptFileSelection(String prompt) {
        System.out.println(prompt);
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        try {
            System.out.print("Enter file path: ");
            String filePath = reader.readLine();
            return new File(filePath);
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
            return null;
        }
    }

    // Method to read a CSV file into a List
    private static List<String> readCsvFile(String filename) throws IOException {
        List<String> data = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                data.add(line); // Read each line and add it to the list
            }
        }
        return data;
    }

    // Method to write merged data to a CSV file
    private static void writeCsvFile(String filename, List<String> data) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (String line : data) {
                writer.write(line);
                writer.newLine();
            }
        }
    }

    // Method to merge two sorted lists using multiway merge
    private static List<String> multiwayMerge(List<String> file1Data, List<String> file2Data) {
        List<String> mergedData = new ArrayList<>();
        int i = 0, j = 0;

        // Merge the two lists
        while (i < file1Data.size() && j < file2Data.size()) {
            String data1 = file1Data.get(i);
            String data2 = file2Data.get(j);

            // Compare and add the smaller element to mergedData
            if (data1.compareTo(data2) <= 0) {
                mergedData.add(data1);
                i++;
            } else {
                mergedData.add(data2);
                j++;
            }
        }

        // Add remaining data from both lists
        while (i < file1Data.size()) {
            mergedData.add(file1Data.get(i));
            i++;
        }

        while (j < file2Data.size()) {
            mergedData.add(file2Data.get(j));
            j++;
        }

        return mergedData;
    }
}