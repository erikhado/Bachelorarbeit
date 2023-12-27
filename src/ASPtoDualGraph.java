import java.io.*;
import java.util.*;

public class ASPtoDualGraph {
    public static void main(String[] args) {
        String inputFilePath = "G:\\My Drive\\Uni\\Bachelorarbeit\\Test\\test2.txt";
        String outputFilePath = "G:\\My Drive\\Uni\\Bachelorarbeit\\Test\\output.gr";

        Map<String, Set<Integer>> rulesByAtoms = new HashMap<>();

        int ruleCount = 1;
        try(BufferedReader br = new BufferedReader(new FileReader(inputFilePath))){

            String line;
            while ((line=br.readLine()) != null){
                Set<String> atoms = new HashSet<>();
                line = line.trim();
               String[] rule = line.split(":-");
               String head = rule[0].trim();
               String body = rule[1].trim().replace(".", "");
               Set<String> atomsInRule = new HashSet<>(List.of(body.split(",")));
                atomsInRule.add(head);
                for(String atom : atomsInRule){
                    atom = atom.trim().replaceAll("\\.","");
                    if(atom.startsWith("not ")){
                        atom = atom.substring(4).trim();
                    }
                    atoms.add(atom);
                }
               for (String atom: atoms){
                   if(rulesByAtoms.containsKey(atom)){
                       rulesByAtoms.get(atom).add(ruleCount);
                   } else {
                       Set<Integer> rules = new HashSet<>();
                       rules.add(ruleCount);
                       rulesByAtoms.put(atom, rules);
                   }
               }
               ruleCount++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        writeDualGraphToFile(rulesByAtoms, outputFilePath, ruleCount);
    }

    private static void writeDualGraphToFile(Map<String, Set<Integer>> rulesByAtoms, String outputFilePath, int ruleCount) {
        Set<Integer> vertexes = new HashSet<>();
        Map<String, Integer> atomNumbers = new HashMap<>();
        int currentAtomNumber = 1;
        for(Map.Entry<String, Set<Integer>> entry: rulesByAtoms.entrySet()){
            vertexes.addAll(entry.getValue());
        }
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath))){
            writer.write("p tw " + ruleCount + " " + calculateEdges(rulesByAtoms));
            writer.newLine();

            for (Map.Entry<String, Set<Integer>> entry : rulesByAtoms.entrySet()){

                for (Integer rule : entry.getValue()) {
                    for (Integer rule2 : entry.getValue()){
                        if(rule < rule2) {
                            if (!Objects.equals(rule, rule2)) {
                                writer.write(rule + " " + rule2);
                                writer.newLine();
                            }
                        }
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static int calculateEdges(Map<String, Set<Integer>> rulesByAtoms) {
        int totalEdges = 0;
        for (Set<Integer> rules: rulesByAtoms.values()){
            for (int i = 0; i<rules.size();i++){
                totalEdges += i;
            }
        }
        return totalEdges;
    }
}
