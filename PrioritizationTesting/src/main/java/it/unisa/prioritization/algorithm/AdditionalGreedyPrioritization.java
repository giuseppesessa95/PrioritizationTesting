
package it.unisa.prioritization.algorithm;

import com.sun.org.apache.xml.internal.security.algorithms.Algorithm;
import it.unisa.prioritization.criterion.CumulativeCoverage;
import it.unisa.prioritization.problems.SingleObjectiveGeneralizedPrioritizationProblem;
import java.util.ArrayList;
import java.util.List;
import javax.management.JMException;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.solution.SolutionBuilder.Variable;


public class AdditionalGreedyPrioritization extends Algorithm
{
    private static final long serialVersionUID = 1L;
    
    private final SingleObjectiveGeneralizedPrioritizationProblem realProblem;
    private List<CumulativeCoverage> cumulativeCoverages;
    private List<Integer> coverages;
    private final int length;
    private final int numberOfCoverageCriterion;
    private List<Integer> usedValues;
    
    public AdditionalGreedy(Problem problem) 
    {
        super(problem);
        this.realProblem = (SingleObjectiveGeneralizedPrioritizationProblem) this.problem_;
        this.length = realProblem.costCriterion.size();
        this.numberOfCoverageCriterion = realProblem.coverageCriteria.size();
    }
    
    public SolutionSet execute() throws JMException, ClassNotFoundException 
    {
        NonDominatedSolutionList ndl = new NonDominatedSolutionList();

        this.cumulativeCoverages = new ArrayList<>();
        this.coverages = new ArrayList<>();
        this.usedValues = new ArrayList<>();

        for (int i = 0; i < this.numberOfCoverageCriterion; i++) 
        {
            this.cumulativeCoverages.add(new CumulativeCoverage(this.realProblem.coverageCriteria.get(i)));
        }

        for (int i = 0; i < this.numberOfCoverageCriterion; i++) 
        {
            this.coverages.add(i, 0);
        }

        Permutation variable = new Permutation();
        variable.vector_ = new int[this.length];

        int step = 0;

        while (step < this.length) 
        {
            boolean isMaximum = true;
            variable.vector_[step] = this.getGreedyStep(variable, step, this.coverages);
            System.out.println("Greedy algorithm step: " + (step));
            step++;

            // if all coverage scores achieved the maximum values let's stop the cycle
            for (int index = 0; index < this.numberOfCoverageCriterion; index++) {
                isMaximum = isMaximum & this.cumulativeCoverages.get(index).hasReachedMaxCoverage();
            }
            if (isMaximum) {
                System.out.println("Rebooting at step:" + step);
                for (int i = 0; i < this.numberOfCoverageCriterion; i++) {
                    this.cumulativeCoverages.set(i, new CumulativeCoverage(this.realProblem.coverageCriteria.get(i)));
                    this.coverages.set(i, 0);
                }
            }
        }
        Variable[] variables = new Variable[1];
        variables[0] = variable;
        ndl.add(new Solution(problem_, variables));
        return ndl;
    }

    private int getGreedyStep(Permutation variable, int step, List<Integer> previousCoverages) 
    {
        double maxim = 0;
        int bestStep = 0;

        for (int i = 0; i < length; i++) {
            if (!usedValues.contains(i)) {
                variable.vector_[step] = i;
                List<CumulativeCoverage> tmpObjectives = new ArrayList<>();
                for (int j = 0; j < this.numberOfCoverageCriterion; j++) {
                    tmpObjectives.add(this.cumulativeCoverages.get(j).copy());
                }
                List<Integer> tmpCoverages = new ArrayList<>();

                // let's update the cumulative coverage scores
                for (int index = 0; index < this.realProblem.coverageCriteria.size(); index++) {
                    int cumulativeCoverage = tmpObjectives.get(index).updateCoverage(i);
                    tmpCoverages.add(cumulativeCoverage);
                }

                double greedy = 0;
                for (int j = 0; j < this.numberOfCoverageCriterion; j++) {
                    greedy += Math.abs(tmpCoverages.get(j) - previousCoverages.get(j));
                }
                greedy = greedy / this.realProblem.costCriterion.getCostOfTest(step);

                if (greedy > maxim || maxim == 0) {
                    maxim = greedy;
                    bestStep = i;
                }
            }
        }
        // let's update the cumulative coverage scores
        for (int index = 0; index < this.numberOfCoverageCriterion; index++) 
        {
            int coverage = this.cumulativeCoverages.get(index).updateCoverage(bestStep);
            this.coverages.set(index, coverage);
        }
        this.usedValues.add(bestStep);
        return bestStep;
    }
}
