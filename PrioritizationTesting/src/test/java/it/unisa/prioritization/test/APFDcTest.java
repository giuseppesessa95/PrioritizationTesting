package it.unisa.prioritization.test;

import it.unisa.prioritization.problems.SingleObjectiveGeneralizedPrioritizationProblem;
import java.util.ArrayList;
import java.util.List;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.problem.PermutationProblem;
import org.uma.jmetal.solution.PermutationSolution;
//import jmetal.encodings.variable.Permutation;
import it.unisa.prioritization.criterion.GeneralizedAveragePercentage;
import static junit.framework.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author apanichella
 */
public class APFDcTest {
    
    private SingleObjectiveGeneralizedPrioritizationProblem problem;

	@Before 
	public void initialize(){
            List<String> list = new ArrayList<>();
            list.add("io/test/APFDc/coverage.csv");
            problem  = new SingleObjectiveGeneralizedPrioritizationProblem(list, "io/test/APFDc/cost.csv", "io/test/APFDc/faults.csv");
	
        }

    @Test
    public void testPermutation1() throws ClassNotFoundException{
        Solution solution = new Solution(problem);
	((Permutation) solution.getDecisionVariables()[0]).vector_[0]=0;
	((Permutation) solution.getDecisionVariables()[0]).vector_[1]=1;
	((Permutation) solution.getDecisionVariables()[0]).vector_[2]=2;
	((Permutation) solution.getDecisionVariables()[0]).vector_[3]=3;
        ((Permutation) solution.getDecisionVariables()[0]).vector_[4]=4;
        double value = GeneralizedAveragePercentage.calculate(solution, problem.faultMatrix, problem.costCriterion, false);
        assertEquals(94d/(15d*10d), value, 0.000001);
    }
    
    @Test
    public void testPermutation2() throws ClassNotFoundException{
        Solution solution = new Solution(problem);
	((Permutation) solution.getDecisionVariables()[0]).vector_[0]=2;
	((Permutation) solution.getDecisionVariables()[0]).vector_[1]=4;
	((Permutation) solution.getDecisionVariables()[0]).vector_[2]=0;
	((Permutation) solution.getDecisionVariables()[0]).vector_[3]=1;
        ((Permutation) solution.getDecisionVariables()[0]).vector_[4]=3;
        double value = GeneralizedAveragePercentage.calculate(solution, problem.faultMatrix, problem.costCriterion, false);
        assertEquals(123d/(15d*10d), value, 0.000001);
    }
    
        @Test
    public void testPermutation3() throws ClassNotFoundException{
        Solution solution = new Solution(problem);
	((Permutation) solution.getDecisionVariables()[0]).vector_[0]=2;
	((Permutation) solution.getDecisionVariables()[0]).vector_[1]=0;
	((Permutation) solution.getDecisionVariables()[0]).vector_[2]=4;
	((Permutation) solution.getDecisionVariables()[0]).vector_[3]=1;
        ((Permutation) solution.getDecisionVariables()[0]).vector_[4]=3;
        double value = GeneralizedAveragePercentage.calculate(solution, problem.faultMatrix, problem.costCriterion, false);
        assertEquals(120d/(15d*10d), value, 0.000001);
    }
   
}
