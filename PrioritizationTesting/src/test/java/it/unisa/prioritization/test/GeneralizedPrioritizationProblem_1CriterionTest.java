package it.unisa.prioritization.test;

import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import it.unisa.prioritization.problems.SingleObjectiveGeneralizedPrioritizationProblem;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.solution.PermutationSolution;
import org.uma.jmetal.solution.impl.DefaultIntegerPermutationSolution;

public class GeneralizedPrioritizationProblem_1CriterionTest{

	private SingleObjectiveGeneralizedPrioritizationProblem problem;

	@Before 
	public void initialize(){
		List<String> list = new ArrayList<>();
		list.add("io/test/coverage.csv");
		problem  = new SingleObjectiveGeneralizedPrioritizationProblem(list, "io/test/cost.csv", "io/test/new_faults.csv");
	}

	@Test
	public void testPermutation1() throws ClassNotFoundException {
                PermutationSolution<Integer> solution = new DefaultIntegerPermutationSolution(problem);
                solution.setVariableValue(0, 0);
                solution.setVariableValue(1, 1);
                solution.setVariableValue(2, 2);
                solution.setVariableValue(3, 3);
		problem.evaluate(solution);

		double actualHypervolume =  solution.getObjective(0);
		double expectedHypervolume = - (double)(8*4+10*5+10*3+10*3)/(10*15);
		assertEquals(expectedHypervolume,actualHypervolume, 0.000001);
	}

	@Test
	public void testPermutation2() throws ClassNotFoundException {
            
                PermutationSolution<Integer> solution = new DefaultIntegerPermutationSolution(problem);
                solution.setVariableValue(0, 2);
                solution.setVariableValue(1, 3);
                solution.setVariableValue(2, 0);
                solution.setVariableValue(3, 1);
		
		problem.evaluate(solution);

		double actualHypervolume =  solution.getObjective(0);
		double expectedHypervolume = - (double)(4*3+6*3+9*4+10*5)/(10*15);
		assertEquals(expectedHypervolume,actualHypervolume, 0.000001);
	}

	@Test
	public void testPermutation3() throws ClassNotFoundException {
            
                PermutationSolution<Integer> solution = new DefaultIntegerPermutationSolution(problem);
                solution.setVariableValue(0, 2);
                solution.setVariableValue(1, 3);
                solution.setVariableValue(2, 1);
                solution.setVariableValue(3, 0);
		problem.evaluate(solution);

		double actualHypervolume =  solution.getObjective(0);
		double expectedHypervolume = - (double)(4*3+6*3+10*4+10*5)/(10*15);
		assertEquals(expectedHypervolume,actualHypervolume, 0.000001);
	}
}
