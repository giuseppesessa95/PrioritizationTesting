
package it.unisa.prioritization.algorithm;
import it.unisa.prioritization.ConfigManager;
import com.sun.scenario.Settings;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.management.JMException;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.multiobjective.nsgaii.NSGAII;
import org.uma.jmetal.operator.Operator;
import java.lang.String;
import java.lang.Object;


public class NSGASettings extends Settings
{
    public int populationSize;
    public int maxEvaluations;
    
    public double mutationProbability;
    public double crossoverProbability;
    
    public NSGASettings(String problem)
    {
        super(problem);
        
        try {
            List<String> coverageFilenames = new LinkedList<>();
            String objectives = ConfigManager.getObjectives();
            if (objectives.contains("coverage_s")) {
                coverageFilenames.add(ConfigManager.getInputPath() + "comp_coverage_matrix_s");
            }
            if (objectives.contains("coverage_b")) {
                coverageFilenames.add(ConfigManager.getInputPath() + "comp_coverage_matrix_b");
            }
            if (objectives.contains("coverage_f")) {
                coverageFilenames.add(ConfigManager.getInputPath() + "comp_coverage_matrix_f");
            }
            if (objectives.contains("past_fault")) {
                coverageFilenames.add(ConfigManager.getInputPath() + "comp_past_fault_matrix");
            }
            if (objectives.contains("input_diversity")) {
                coverageFilenames.add(ConfigManager.getInputPath() + "input_diversity");
            }
            Object[] problemParams = {coverageFilenames, ConfigManager.getInputPath() + "cost_array", ConfigManager.getInputPath() + "fault_matrix"};
            problem = (new ProblemFactory()).getProblem(problemName, problemParams);
            
             // Default experiments.settings
            populationSize = 250;
            maxEvaluations = 25000;
            mutationProbability = 1.0 / problem.getNumberOfVariables();
            crossoverProbability = 0.9;
        } catch (IOException ex) {
            Logger.getLogger(NSGASettings.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public NSGASettings() 
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public Algorithm configure() throws JMException {
        Algorithm algorithm;
        Operator selection;
        Operator crossover;
        Operator mutation;

        HashMap parameters;

        // Creating the problem
        algorithm = new NSGAII(problem);

        // Algorithm parameters
        algorithm.setInputParameter("populationSize", populationSize);
        algorithm.setInputParameter("maxEvaluations", maxEvaluations);

        // Mutation and Crossover Permutation codification
        parameters = new HashMap();
        parameters.put("probability", crossoverProbability);
        crossover = CrossoverFactory.getCrossoverOperator("PMXCrossover", parameters);

        parameters = new HashMap();
        parameters.put("probability", mutationProbability);
        mutation = MutationFactory.getMutationOperator("SwapMutation", parameters);

        // Selection Operator 
        parameters = null;
        selection = SelectionFactory.getSelectionOperator("TournamentSelection", parameters);

        // Add the operators to the algorithm
        algorithm.addOperator("crossover", crossover);
        algorithm.addOperator("mutation", mutation);
        algorithm.addOperator("selection", selection);

        return algorithm;
    }

    public Algorithm configure(Properties configuration) throws JMException {
        Algorithm algorithm;
        Operator selection;
        Operator crossover;
        Operator mutation;

        HashMap parameters;

        // Creating the problem
        algorithm = new NSGAII(problem);

        // Algorithm parameters
        populationSize = Integer.parseInt(configuration.getProperty("populationSize", String.valueOf(populationSize)));
        maxEvaluations = Integer.parseInt(configuration.getProperty("maxEvaluations", String.valueOf(maxEvaluations)));

        // Mutation and Crossover Permutation codification
        crossoverProbability = Double.parseDouble(configuration.getProperty("crossoverProbability", String.valueOf(crossoverProbability)));
        parameters = new HashMap();
        parameters.put("probability", crossoverProbability);
        crossover = CrossoverFactory.getCrossoverOperator("PMXCrossover", parameters);

        mutationProbability = Double.parseDouble(configuration.getProperty("mutationProbability", String.valueOf(mutationProbability)));
        parameters = new HashMap();
        parameters.put("probability", mutationProbability);
        mutation = MutationFactory.getMutationOperator("SwapMutation", parameters);

        // Selection Operator 
        parameters = null;
        selection = SelectionFactory.getSelectionOperator("TournamentSelection", parameters);

        // Add the operators to the algorithm
        algorithm.addOperator("crossover", crossover);
        algorithm.addOperator("mutation", mutation);
        algorithm.addOperator("selection", selection);

        return algorithm;
    }
    
}
