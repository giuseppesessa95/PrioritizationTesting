package it.unisa.prioritization.problems;

import it.unisa.prioritization.criterion.CoverageMatrix;
import it.unisa.prioritization.criterion.ExecutionCostVector;
import java.util.ArrayList;
import java.util.List;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.PermutationSolution;

/**
 * Super class representing a Testing Prioritization problem with any number of
 * testing criteria.
 *
 * @author Dario Di Nucci
 */
public abstract class GeneralizedPrioritizationProblem implements Problem{
    
    public List<CoverageMatrix> coverageCriteria = new ArrayList<>();
    public ExecutionCostVector costCriterion;
    public CoverageMatrix faultMatrix;
    private final String problemName_;
    final int numberOfVariables_;
    private final int numberOfConstraints_;
    
    /**
     * Public constructor
     *
     * @param coverageFilenames List of files (absolute path) storing the
     * coverage matrices
     * @param costFilename file containing the execution cost info
     * @param faultFilename file containing the fault coverage matrix
     */
    public GeneralizedPrioritizationProblem(List<String> coverageFilenames, String costFilename, String faultFilename) {
        problemName_ = "GeneralizedPrioritizationProblem";
        numberOfVariables_ = 1;
        numberOfConstraints_ = 0;
        
        //solutionType_ = new PermutationSolutionType(this);

        int[] length_ = new int[numberOfVariables_];

        // load all coverage matrices
        for (String filename : coverageFilenames) {
            CoverageMatrix cov;
            cov = new CoverageMatrix(filename, true);
            this.coverageCriteria.add(cov);
            System.out.println("Read " + cov.getSize() + " elements from the coverage matrix '" + filename + "'");
        }

        // load cost info
        costCriterion = new ExecutionCostVector(costFilename);
        System.out.println("Read " + costCriterion.size() + " elements from the cost array.");

        // past fault matrix must not be compacted beacuse it is used for computing APFDc
        faultMatrix = new CoverageMatrix(faultFilename, false);
        System.out.println("Read " + faultMatrix.getSize() + " elements from the coverage matrix '" + faultFilename + "'");

        // set the length of the chromosome
        length_[0] = costCriterion.size();
    }

  
    
}
