import java.io.*;
import java.util.*;

public class GraphMetrics {
    public static void main(String[] args) {
        String inputFilePath = "-";
        String outputFilePath = "-";
        System.out.println("Max Degree: " + calculateMaxDegree(inputFilePath));
        System.out.println("Longest Path: " + calculateLongestPathLength(inputFilePath));

        try {
            // Create a FileWriter object with the specified file name
            FileWriter fileWriter = new FileWriter(outputFilePath);

            // Redirect standard output stream to the FileWriter
            PrintWriter printWriter = new PrintWriter(fileWriter);

            // Print the output to the file using PrintWriter
            printWriter.println("Max Degree: " + calculateMaxDegree(inputFilePath));
            printWriter.println("Longest Path: " + calculateLongestPathLength(inputFilePath));

            // Close the PrintWriter
            printWriter.close();

            System.out.println("Output written to " + outputFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static int calculateMaxDegree(String inputFilePath) {
        Map<String, Set<String>> adjacencyList = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(inputFilePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("p")) {
                    String[] parts = line.split(" ");
                    int numVertices = Integer.parseInt(parts[2]);
                    for (int i = 1; i <= numVertices; i++) {
                        adjacencyList.put(Integer.toString(i), new HashSet<>());
                    }
                } else if (!line.startsWith("c")) {
                    String[] vertices = line.split(" ");
                    String vertex1 = vertices[0];
                    String vertex2 = vertices[1];
                    adjacencyList.get(vertex1).add(vertex2);
                    adjacencyList.get(vertex2).add(vertex1);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        int maxDegree = 0;
        for (Set<String> neighbors : adjacencyList.values()) {
            maxDegree = Math.max(maxDegree, neighbors.size());
        }

        return maxDegree;
    }

    public static int calculateLongestPathLength(String inputFilePath) {
        List<List<Integer>> adjacencyList = new ArrayList<>();
        int numVertices = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(inputFilePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("p")) {
                    String[] parts = line.split(" ");
                    numVertices = Integer.parseInt(parts[2]);
                    for (int i = 0; i <= numVertices; i++) {
                        adjacencyList.add(new ArrayList<>());
                    }
                } else if (!line.startsWith("c")) {
                    String[] vertices = line.split(" ");
                    int vertex1 = Integer.parseInt(vertices[0]);
                    int vertex2 = Integer.parseInt(vertices[1]);
                    adjacencyList.get(vertex1).add(vertex2);
                    adjacencyList.get(vertex2).add(vertex1);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        int longestPathLength = 0;

        for (int startVertex = 1; startVertex <= numVertices; startVertex++) {
            longestPathLength = Math.max(longestPathLength, dfs(adjacencyList, startVertex));
        }

        return longestPathLength;
    }

    private static int dfs(List<List<Integer>> adjacencyList, int startVertex) {
        Set<Integer> visited = new HashSet<>();
        Stack<Integer> stack = new Stack<>();
        stack.push(startVertex);
        visited.add(startVertex);
        int longestPathLength = 0;

        while (!stack.isEmpty()) {
            int currentVertex = stack.pop();
            boolean isBranch = false;

            for (int neighbor : adjacencyList.get(currentVertex)) {
                if (!visited.contains(neighbor)) {
                    stack.push(neighbor);
                    visited.add(neighbor);
                    isBranch = true;
                }
            }

            if (isBranch) {
                longestPathLength = 0;
            } else {
                longestPathLength++;
            }
        }

        return longestPathLength;
    }




    }


