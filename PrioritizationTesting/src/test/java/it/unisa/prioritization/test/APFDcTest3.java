package it.unisa.prioritization.test;

import it.unisa.prioritization.problems.SingleObjectiveGeneralizedPrioritizationProblem;
import java.util.ArrayList;
import java.util.List;
import org.uma.jmetal.solution.PermutationSolution;
import it.unisa.prioritization.criterion.GeneralizedAveragePercentage;
import static junit.framework.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.uma.jmetal.solution.impl.DefaultIntegerPermutationSolution;

/**
 *
 * @author apanichella
 */
public class APFDcTest3 {

    private SingleObjectiveGeneralizedPrioritizationProblem problem;

    @Before
    public void initialize() {
        List<String> list = new ArrayList<>();
        list.add("io/test/APFDc/coverage.csv");
        problem = new SingleObjectiveGeneralizedPrioritizationProblem(list, "io/test/APFDc/cost.csv", "io/test/APFDc/one_fault.csv", false);

    }

    @Test
    public void testPermutation1() throws ClassNotFoundException {
        PermutationSolution<Integer> solution = new DefaultIntegerPermutationSolution(problem);
        solution.setVariableValue(0, 1);
        solution.setVariableValue(1, 2);
        solution.setVariableValue(2, 3);
        solution.setVariableValue(3, 4);
        solution.setVariableValue(4, 0);
        double value = GeneralizedAveragePercentage.calculate(solution, problem.faultMatrix, problem.costCriterion, false);
        assertEquals(0.03, value, 0.01);
    }

    @Test
    public void testPermutation2() throws ClassNotFoundException {
        PermutationSolution<Integer> solution = new DefaultIntegerPermutationSolution(problem);
        solution.setVariableValue(0, 0);
        solution.setVariableValue(1, 1);
        solution.setVariableValue(2, 2);
        solution.setVariableValue(3, 3);
        solution.setVariableValue(4, 4);
        double value = GeneralizedAveragePercentage.calculate(solution, problem.faultMatrix, problem.costCriterion, false);
        assertEquals(0.96, value, 0.01);
    }

}
