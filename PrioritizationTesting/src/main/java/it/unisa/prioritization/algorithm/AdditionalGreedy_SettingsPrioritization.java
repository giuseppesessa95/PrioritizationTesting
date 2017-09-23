package it.unisa.prioritization.algorithm;

import com.sun.scenario.Settings;
import it.unisa.prioritization.ConfigManager;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.management.JMException;
import org.uma.jmetal.algorithm.Algorithm;

public class AdditionalGreedy_SettingsPrioritization extends Settings {

    public int populationSize_;
    public int maxEvaluations_;

    public double mutationProbability_;
    public double crossoverProbability_;

    public AdditionalGreedy_SettingsPrioritization(String problem) {
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
            problem_ = (new ProblemFactory()).getProblem(problemName_, problemParams);
        } catch (IOException ex) {
            Logger.getLogger(AdditionalGreedy_Settings.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Algorithm configure() throws JMException {
        return new AdditionalGreedy(problem_);
    }
}
