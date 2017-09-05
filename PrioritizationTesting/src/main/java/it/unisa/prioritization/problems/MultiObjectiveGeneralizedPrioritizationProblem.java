package it.unisa.prioritization.problems;

import it.unisa.prioritization.criterion.GeneralizedAveragePercentage;
import java.util.List;

import org.uma.jmetal.solution.Solution;

/**
 * Class representing a Testing Prioritization problem with any number of
 * testing criteria and a multiple objective.
 *
 * @author Dario Di Nucci
 */
public class MultiObjectiveGeneralizedPrioritizationProblem extends GeneralizedPrioritizationProblem {

    private int[] length_;

    /**
     * Public constructor
     *
     * @param coverageFilenames List of files (absolute path) storing the
     * coverage matrices
     * @param costFilename file containing the execution cost info
     * @param faultFilename file containing the fault coverage matrix
     */
    public MultiObjectiveGeneralizedPrioritizationProblem(List<String> coverageFilenames, String costFilename, String faultFilename) {
        super(coverageFilenames, costFilename, faultFilename);
        int numberOfVariables_ = 1;
        //Objectives are given by coverage matrices and cost array
        int numberOfObjectives_ = this.coverageCriteria.size();
        int numberOfConstraints_ = 0;
        double[] lowerLimit_ = new double[this.length_[0]];
        double[] upperLimit_ = new double[this.length_[0]];
    }

    /**
     * Evaluates a solution
     *
     * @param solution The solution to evaluate
     */
    public void evaluate(Solution solution) {

        for (int i = 0; i < this.coverageCriteria.size(); i++) {
            //Coverage criteria are set in order to be maximized
            double criteriaAveragePercentage = GeneralizedAveragePercentage.calculate(solution, this.coverageCriteria.get(i), this.costCriterion, true);
            solution.setObjective(i, -criteriaAveragePercentage);
        }
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
