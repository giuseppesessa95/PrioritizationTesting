package it.unisa.prioritization.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import it.unisa.prioritization.problems.SingleObjectiveGeneralizedPrioritizationProblem;
import jmetal.core.Solution;
import jmetal.encodings.variable.Permutation;

public class GeneralizedPrioritizationProblem_2CriteriaTest{

	private SingleObjectiveGeneralizedPrioritizationProblem problem;

	@Before 
	public void initialize(){
		List<String> list = new ArrayList<>();
		list.add("io/test/coverage.csv");
		list.add("io/test/coverage2.csv");
		problem  = new SingleObjectiveGeneralizedPrioritizationProblem(list, "io/test/cost.csv", "io/test/new_faults.csv");
	}
	
	@Test
	public void testPermutation1() throws ClassNotFoundException {
		Solution solution = new Solution(problem);
		((Permutation) solution.getDecisionVariables()[0]).vector_[0]=0;
		((Permutation) solution.getDecisionVariables()[0]).vector_[1]=1;
		((Permutation) solution.getDecisionVariables()[0]).vector_[2]=2;
		((Permutation) solution.getDecisionVariables()[0]).vector_[3]=3;

		problem.evaluate(solution);

		double actualHypervolume =  solution.getObjective(0);
		double expectedHypervolume = - (double)(8*4*1+10*5*2+10*3*3+10*3*4)/(10*15*4);
		assertEquals(expectedHypervolume,actualHypervolume, 0.000001);
	}

	@Test
	public void testPermutation2() throws ClassNotFoundException {
		Solution solution = new Solution(problem);

		((Permutation) solution.getDecisionVariables()[0]).vector_[0]=2;
		((Permutation) solution.getDecisionVariables()[0]).vector_[1]=3;
		((Permutation) solution.getDecisionVariables()[0]).vector_[2]=0;
		((Permutation) solution.getDecisionVariables()[0]).vector_[3]=1;

		problem.evaluate(solution);

		double actualHypervolume =  solution.getObjective(0);
		double expectedHypervolume = - (double)(4*3*1+6*3*2+9*4*3+10*5*4)/(10*15*4);
		assertEquals(expectedHypervolume,actualHypervolume, 0.000001);
	}

	@Test
	public void testPermutation3() throws ClassNotFoundException {
		Solution solution = new Solution(problem);

		((Permutation) solution.getDecisionVariables()[0]).vector_[0]=2;
		((Permutation) solution.getDecisionVariables()[0]).vector_[1]=3;
		((Permutation) solution.getDecisionVariables()[0]).vector_[2]=1;
		((Permutation) solution.getDecisionVariables()[0]).vector_[3]=0;

		problem.evaluate(solution);

		double actualHypervolume =  solution.getObjective(0);
		double expectedHypervolume = - (double)(4*3*1+6*3*2+10*4*4+10*5*4)/(10*15*4);
		assertEquals(expectedHypervolume,actualHypervolume, 0.000001);
	}
	
}
