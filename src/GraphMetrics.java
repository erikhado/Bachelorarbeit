import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class GraphMetrics {
    public static void main(String[] args) {
        String inputFilePath = "G:\\My Drive\\Uni\\Bachelorarbeit\\Test\\output.gr";
        System.out.println("Max Degree: " + calculateMaxDegree(inputFilePath));

    }
    public static int calculateMaxDegree(String inputFilePath) {
        Map<String, Set<String>> adjacencyList = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(inputFilePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("p")) {
                    String[] parts = line.split(" ");
                    int numVertices = Integer.parseInt(parts[3]);
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
}
