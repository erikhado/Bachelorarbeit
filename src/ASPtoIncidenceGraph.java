import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class ASPtoIncidenceGraph {
    public static void main(String[] args) {
        String inputFilePath = "C:\\Users\\erikh\\Desktop\\Bachelorarbeit\\Test\\test2.txt";
        String outputFilePath = "C:\\Users\\erikh\\Desktop\\Bachelorarbeit\\Test\\output.gr";

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

                    List<String> atomsInRule = Arrays.asList(body.split(","));
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
        writeIncidenceGraphToFile(atomsByRule);
    }

    private static void writeIncidenceGraphToFile(Map<Integer, Set<String>> atomsByRule) {
    }

}
