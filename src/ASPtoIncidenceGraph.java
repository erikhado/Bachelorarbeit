import java.io.*;
import java.util.*;

public class ASPtoIncidenceGraph {
    public static void main(String[] args) {
        String inputFilePath = "-";
        String outputFilePath = "-";

        Map<Integer, Set<String>> atomsByRule = new HashMap<>();

        try(BufferedReader br = new BufferedReader(new FileReader(inputFilePath))) {
            String line;
            int i = 1;
            while((line = br.readLine()) != null) {
                Set<String> atoms = new HashSet<>();
                line = line.trim();
                if (line.contains(":-")) {
                    String[] rule = line.split(":-");
                    String head = rule[0].trim();
                    String body = rule[1].trim().replace(".", "");

                    List<String> atomsInRule = new ArrayList<>(Arrays.asList(body.split(",")));
                    atomsInRule.add(head);

                    for(String atom : atomsInRule){
                        atom = atom.trim().replaceAll("\\.","");
                        if(atom.startsWith("not ")){
                            atom = atom.substring(4).trim();
                        }
                        atoms.add(atom);
                    }
                    atomsByRule.put(i, atoms);
                    i++;
                }
            }
        } catch (IOException e){
            e.printStackTrace();
        }
        writeIncidenceGraphToFile(atomsByRule, outputFilePath);
    }

    private static void writeIncidenceGraphToFile(Map<Integer, Set<String>> atomsByRule, String outputFilePath) {
        Set<String> vertexes = new HashSet<>();
        Map<String, Integer> atomNumbers = new HashMap<>();
        int currentAtomNumber = 1;
        for(Map.Entry<Integer, Set<String>> entry: atomsByRule.entrySet()){
            vertexes.add(entry.getKey().toString());
            vertexes.addAll(entry.getValue());
            atomNumbers.put(entry.getKey().toString(), currentAtomNumber++);
            for (String atom: entry.getValue()){
                if(!atomNumbers.containsKey(atom)) {
                    atomNumbers.put(atom, currentAtomNumber++);
                }
            }
        }
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath))){
            writer.write("p tw " + vertexes.size() + " " + calculateEdges(atomsByRule));
            writer.newLine();

            for (Map.Entry<Integer, Set<String>> entry : atomsByRule.entrySet()){
                int vertexNumber = atomNumbers.get(entry.getKey().toString());

                for (String atom : entry.getValue()) {
                    int adjVertexNumber = atomNumbers.get(atom);
                    writer.write(vertexNumber + " " + adjVertexNumber);
                    writer.newLine();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static int calculateEdges(Map<Integer, Set<String>> atomsByRule) {
        int totalEdges = 0;
        for (Set<String> atoms: atomsByRule.values()){
            totalEdges += atoms.size();
        }
        return totalEdges;
    }

}
