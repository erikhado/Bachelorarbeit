import java.io.*;
import java.util.*;

public class ASPtoDualGraph {
    public static void main(String[] args) {
        String inputFilePath = "-";
        String outputFilePath = "-";

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
        
        writeDualGraphToFile(rulesByAtoms, outputFilePath);
    }

    private static void writeDualGraphToFile(Map<String, Set<Integer>> rulesByAtoms, String outputFilePath) {
        Set<Integer> vertexes = new HashSet<>();
        Map<Integer, Set<Integer>> adjRulesByRules = new HashMap<>();
        for(Map.Entry<String, Set<Integer>> entry: rulesByAtoms.entrySet()){
            vertexes.addAll(entry.getValue());
        }
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath))){
            for (Map.Entry<String, Set<Integer>> entry : rulesByAtoms.entrySet()){

                for (Integer rule : entry.getValue()) {
                    for (Integer rule2 : entry.getValue()){
                        if(rule < rule2) {
                            if (!Objects.equals(rule, rule2)) {
                                if(!adjRulesByRules.containsKey(rule)) {
                                    Set<Integer> rules = new HashSet<>();
                                    rules.add(rule2);
                                    adjRulesByRules.put(rule, rules);
                                } else {
                                    adjRulesByRules.get(rule).add(rule2);
                                }

                            }
                        }
                    }
                }
            }
            writer.write("p tw " + vertexes.size() + " " + calculateEdges(adjRulesByRules));
            writer.newLine();


            for(Map.Entry<Integer, Set<Integer>> entry : adjRulesByRules.entrySet()){
                for(Integer rule : entry.getValue()){
                    writer.write(entry.getKey() + " " + rule);
                    writer.newLine();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static int calculateEdges(Map<Integer, Set<Integer>> adjRulesByRules) {
        int totalEdges = 0;
        for(Map.Entry<Integer, Set<Integer>> entry : adjRulesByRules.entrySet()){
            totalEdges += entry.getValue().size();
        }
        return totalEdges;
    }
}
