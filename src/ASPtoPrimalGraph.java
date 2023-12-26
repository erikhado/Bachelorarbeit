import java.io.*;
import java.util.*;

public class ASPtoPrimalGraph {
    public static void main(String[] args) {
        String inputFilePath = "C:\\Users\\erikh\\Desktop\\Bachelorarbeit\\Test\\test2.txt";
        String outputFilePath = "C:\\Users\\erikh\\Desktop\\Bachelorarbeit\\Test\\output.gr";

        Map<Integer, Set<String>> ruleAtoms = new HashMap<>();
        int i = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(inputFilePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                Set<String> atoms = new HashSet<>();
                line = line.trim();
                if (line.contains(":-")) {
                    String[] rule = line.split(":-");
                    String head = rule[0].trim();
                    String body = rule[1].trim().replace(".", "");

                    List<String> atomsInRule = Arrays.asList(body.split(","));

                    for (String atom : atomsInRule) {
                        atom = atom.trim().replaceAll("\\.", ""); // Remove period
                        if (atom.startsWith("not ")) {
                            atom = atom.substring(4).trim();
                        }
                        atoms.add(atom);
                    }
                    atoms.addAll(Arrays.asList(head.split(",")));
                    ruleAtoms.put(i, atoms);
                    i++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Map<String, Set<String>> primalGraph = createPrimalGraph(ruleAtoms);
        writePrimalGraphToFile(primalGraph, outputFilePath);
    }



    //

    public static Map<String, Set<String>> createPrimalGraph(Map<Integer, Set<String>> ruleAtoms) {
        Map<String, Set<String>> primalGraph = new HashMap<>();

        for(int i : ruleAtoms.keySet()){
            for (String atom: ruleAtoms.get(i)){
                primalGraph.put(atom, new HashSet<>());
            }
            for (String atomA: ruleAtoms.get(i)){
                for (String atomB: ruleAtoms.get(i)){
                    if(!atomA.equals(atomB) && !primalGraph.get(atomB).contains(atomA) && primalGraph.containsKey(atomA) && primalGraph.containsKey(atomB)){
                        primalGraph.get(atomA).add(atomB);
                    }
                }
            }
        }




        return primalGraph;
    }

    public static void writePrimalGraphToFile(Map<String, Set<String>> primalGraph, String outputFilePath) {
        Map<String, Integer> atomNumbers = new HashMap<>();
        int currentAtomNumber = 1;
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath))) {
            for(String atom: primalGraph.keySet()){
                atomNumbers.put(atom, currentAtomNumber++);
            }


            writer.write("p tw " + primalGraph.size() + " " + calculateEdges(primalGraph));
            writer.newLine();

            for (Map.Entry<String, Set<String>> entry : primalGraph.entrySet()) {
                String vertex = entry.getKey();
                Set<String> adjacentVertices = entry.getValue();

                int vertexNumber = atomNumbers.get(vertex);
                for (String adjVertex : adjacentVertices) {
                    int adjVertexNumber = atomNumbers.get(adjVertex);
                    writer.write(vertexNumber + " " + adjVertexNumber);
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int calculateEdges(Map<String, Set<String>> primalGraph) {
        int totalEdges = 0;
        for (Set<String> adjVertices : primalGraph.values()) {
            totalEdges += adjVertices.size();
        }
        return totalEdges;
    }
}
