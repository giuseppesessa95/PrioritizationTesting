package it.unisa.prioritization.problems;

import java.util.ArrayList;
import java.util.List;

import it.unisa.prioritization.criterion.CumulativeCoverage;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.solution.PermutationSolution;

/**
 * Class representing a Testing Prioritization problem with any number of
 * testing criteria and a single objective.
 *
 * @author Annibale Panichella
 */
public class SingleObjectiveGeneralizedPrioritizationProblem extends GeneralizedPrioritizationProblem {

    private int[] length_;

    /**
     * Public constructor
     *
     * @param coverageFilenames List of files (absolute path) storing the
     * coverage matrices
     * @param costFilename file containing the execution cost info
     * @param faultFilename file containing the fault coverage matrix
     */
    public SingleObjectiveGeneralizedPrioritizationProblem(List<String> coverageFilenames, String costFilename, String faultFilename) {
        super(coverageFilenames, costFilename, faultFilename);
        int numberOfObjectives_ = 1;
        double[] lowerLimit_ = new double[this.length_[0]];
        double[] upperLimit_ = new double[this.length_[0]];
    }

    private static final long serialVersionUID = 1L;

    /**
     * Evaluates a solution
     *
     * @param solution The solution to evaluate
     */
    public void evaluate(Solution solution) {

        int[] solutionArray = ((Permutation) solution.getDecisionVariables()[0]).vector_; //vedi questo come modificarlo

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
            // save previous cumulative coverage scores
            //List<Double> previousCoverages = new ArrayList<Double>();
            //for (int index=0; index<this.coverageCriteria.size(); index++){
            //	previousCoverages.add(index, coverages.get(index));
            //}

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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getNumberOfObjectives() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getNumberOfConstraints() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void evaluate(Object s) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object createSolution() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
