package it.unisa.prioritization.test;

import it.unisa.prioritization.problems.MultiObjectiveGeneralizedPrioritizationProblem;
import it.unisa.prioritization.problems.SingleObjectiveGeneralizedPrioritizationProblem;
import java.util.ArrayList;
import java.util.List;
import jmetal.core.Solution;
import jmetal.encodings.variable.Permutation;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author dardin88
 */
public class Evaluations_3CriterionTest {

    private List<String> list;

    @Before
    public void initialize() {
        list = new ArrayList<>();
        list.add("io/test/grep/comp_coverage_matrix_s.csv");
        list.add("io/test/grep/comp_past_fault_matrix.csv");
    }

    @Test
    public void testSingleObjectiveProblem() throws ClassNotFoundException {
        SingleObjectiveGeneralizedPrioritizationProblem problem = new SingleObjectiveGeneralizedPrioritizationProblem(list, "io/test/grep/cost_array.csv", "io/test/grep/fault_matrix.csv");
        Solution solution = new Solution(problem);
        int solutionLength = ((Permutation) solution.getDecisionVariables()[0]).vector_.length;
        for (int i = 0; i < solutionLength; i++) {
            ((Permutation) solution.getDecisionVariables()[0]).vector_[i] = i;
        }

        long start = System.currentTimeMillis();
        problem.evaluate(solution);
        long end = System.currentTimeMillis();
        long time = end - start;
        
        System.out.println("Single objective time: " + time + "ms");
    }
    
    @Test
    public void testMultiObjectiveProblem() throws ClassNotFoundException {
        MultiObjectiveGeneralizedPrioritizationProblem problem = new MultiObjectiveGeneralizedPrioritizationProblem(list, "io/test/grep/cost_array.csv", "io/test/grep/fault_matrix.csv");
        Solution solution = new Solution(problem);
        int solutionLength = ((Permutation) solution.getDecisionVariables()[0]).vector_.length;
        for (int i = 0; i < solutionLength; i++) {
            ((Permutation) solution.getDecisionVariables()[0]).vector_[i] = i;
        }

        long start = System.currentTimeMillis();
        problem.evaluate(solution);
        long end = System.currentTimeMillis();
        long time = end - start;
        
        System.out.println("Multi objective time: " + time + "ms");
    }

}
