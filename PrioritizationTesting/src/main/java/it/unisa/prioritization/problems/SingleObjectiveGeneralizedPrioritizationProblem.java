package it.unisa.prioritization.problems;

import java.util.ArrayList;
import java.util.List;

import it.unisa.prioritization.criterion.CumulativeCoverage;
import org.uma.jmetal.solution.impl.DefaultIntegerPermutationSolution;

/**
 * Class representing a Testing Prioritization problem with any number of
 * testing criteria and a single objective.
 *
 * @author Annibale Panichella
 */
public class SingleObjectiveGeneralizedPrioritizationProblem extends GeneralizedPrioritizationProblem {

    public SingleObjectiveGeneralizedPrioritizationProblem(List<String> coverageFilenames, String costFilename, String faultFilename) {
        super(coverageFilenames, costFilename, faultFilename);
    }

    private static final long serialVersionUID = 1L;

    /**
     * Evaluates a solution
     *
     * @param solution The solution to evaluate
     */
    public void evaluate(DefaultIntegerPermutationSolution solution) {

        String solutionString = solution.getVariableValueString(0);
        int[] solutionArray = new int[solutionString.length()];
        for (int i = 0; i < solutionString.length(); i++) {
            solutionArray[i] = solutionString.charAt(i);
        }
        // let's create cumulative coverage analyzers (one analyzer for each coverage matrix)
        List<CumulativeCoverage> objectives = new ArrayList<>();
        for (int i = 0; i < this.coverageCriteria.size(); i++) {
            objectives.add(new CumulativeCoverage(this.coverageCriteria.get(i)));
        }

        double cost = 0;
        // initialize the cumulative coverage scores to 0
        List<Double> coverages = new ArrayList<>();
        for (int i = 0; i < this.coverageCriteria.size(); i++) {
            coverages.add(i, 0.0);
        }
        double hyperVolume = 0;
        for (int i = 0; i < solutionArray.length; i++) {
            double previousCost = cost;

            cost = cost + costCriterion.getCostOfTest(solutionArray[i]);

            // let's update the cumulative coverage scores
            for (int index = 0; index < this.coverageCriteria.size(); index++) {
                double cumulativeCoverage = objectives.get(index).updateCoverage(solutionArray[i]);
                coverages.set(index, cumulativeCoverage);
            }

            // let's update the hyperVolume
            double delta = 1;
            for (int index = 0; index < this.coverageCriteria.size(); index++) {
                delta = delta * coverages.get(index);
            }
            hyperVolume = hyperVolume + (cost - previousCost) * delta;

            // if all coverage scores achieved the maximum values let's stop the cycle
            boolean isMaximum = true;
            for (int index = 0; index < this.coverageCriteria.size(); index++) {
                isMaximum = isMaximum & objectives.get(index).hasReachedMaxCoverage();
            }
            if (isMaximum) {
                break;
            }
        }
        // we add the remaining part of the hyperVolume (due to early stop when all coverage 
        // criteria reach the maximum coverage
        double delta = 1;
        for (int index = 0; index < this.coverageCriteria.size(); index++) {
            delta = delta * this.coverageCriteria.get(index).maxCoverage_;
        }
        hyperVolume = hyperVolume + delta * (costCriterion.getMaxCost() - cost);

        // let's normalize the hypervolume
        for (int index = 0; index < this.coverageCriteria.size(); index++) {
            hyperVolume = hyperVolume / objectives.get(index).getMaxCoverage();
        }
        hyperVolume = hyperVolume / costCriterion.getMaxCost();
        solution.setObjective(0, -hyperVolume);
    }

    @Override
    public int getNumberOfVariables() {
        return 1;
    }

    @Override
    public int getNumberOfObjectives() {
        return this.coverageCriteria.size();
    }

    @Override
    public int getNumberOfConstraints() {
        return 0;
    }

    @Override
    public String getName() {
        return "SingleObjectiveGeneralizedPrioritizationProblem";
    }

    @Override
    public DefaultIntegerPermutationSolution createSolution() {
        return new DefaultIntegerPermutationSolution(this);
    }

}
