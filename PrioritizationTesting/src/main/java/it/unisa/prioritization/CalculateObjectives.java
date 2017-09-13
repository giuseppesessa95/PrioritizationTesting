package it.unisa.prioritization;

import it.unisa.prioritization.problems.MultiObjectiveGeneralizedPrioritizationProblem;
import it.unisa.prioritization.problems.GeneralizedPrioritizationProblem;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.management.JMException;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.solution.impl.DefaultIntegerPermutationSolution;

/**
 *
 * @author dardin88
 */
public class CalculateObjectives {

    public static void main(String[] args) throws JMException {
        String dataFolder = "/home/dardin88/Documenti/Unisa - phd/3 - Papers work in progress/Hypervolume-based Search for Test Case - Estensione/systems/";
        String resultsFolder = "/home/dardin88/Documenti/Unisa - phd/3 - Papers work in progress/Hypervolume-based Search for Test Case - Estensione/results/";
        File[] projects = new File(dataFolder).listFiles();
        for (File project : projects) {
            if (!project.toString().contains(".git") && project.toString().contains("mysql")) {
                System.out.println(project);
                List<String> list = new ArrayList<>();
                list.add(project.toString() + "/comp_coverage_matrix_s");
                list.add(project.toString() + "/comp_coverage_matrix_b");
                //list.add(project.toString() + "/comp_coverage_matrix_f");
                list.add(project.toString() + "/comp_past_fault_matrix");
                GeneralizedPrioritizationProblem problem = new MultiObjectiveGeneralizedPrioritizationProblem(list, project.toString() + "/cost_array", project.toString() + "/fault_matrix");

                String projectOutput = project.toString().replace(dataFolder, resultsFolder);

                CalculateObjectives pbpd = new CalculateObjectives();

                String greedySolutionPath = projectOutput + "/4d/data/GREEDY/VAR.0";
                try {
                    pbpd.calculateSolutions(greedySolutionPath, problem, projectOutput + "/4d/data/GREEDY/FUN_NEW.0");
                } catch (IOException ex) {
                    Logger.getLogger(CalculateObjectives.class.getName()).log(Level.SEVERE, null, ex);
                }

                for (int run = 0; run < 30; run++) {
                    String hgaSolutionPath = projectOutput + "/4d/data/GGA/SingleObjectiveGeneralizedPrioritizationProblem/VAR." + run;
                    try {
                        pbpd.calculateSolutions(hgaSolutionPath, problem, projectOutput + "/4d/data/GGA/SingleObjectiveGeneralizedPrioritizationProblem/FUN_NEW." + run);
                    } catch (IOException ex) {
                        Logger.getLogger(CalculateObjectives.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
    }

    public void calculateSolutions(String solutionFile, GeneralizedPrioritizationProblem problem, String outputFile) throws IOException, JMException {
        try (FileReader fr = new FileReader(solutionFile)) {
            BufferedReader br = new BufferedReader(fr);
            FileWriter fw = new FileWriter(outputFile);
            BufferedWriter bw = new BufferedWriter(fw);
            String solutionLine;
            while ((solutionLine = br.readLine()) != null) {
                StringTokenizer st = new StringTokenizer(solutionLine, " ");
                int i = 0;
                DefaultIntegerPermutationSolution solution = new DefaultIntegerPermutationSolution(problem);

                while (st.hasMoreTokens()) {
                    solution.setVariableValue(i, Integer.parseInt(st.nextToken()));
                    i++;
                }
                problem.evaluate(solution);
                String outputString = "";
                for (i = 0; i < solution.getNumberOfObjectives(); i++) {
                    outputString += solution.getObjective(i) + " ";
                }
                bw.write(outputString + "\n");
            }

        }
    }

}
