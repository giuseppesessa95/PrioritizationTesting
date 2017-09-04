package it.unisa.prioritization.criterion;

import jmetal.core.Solution;
import jmetal.encodings.variable.Permutation;

/**
 *
 * @author dardin88
 */
public class GeneralizedAveragePercentage {

    public static double calculate(Solution solution, CoverageMatrix coverageMatrix, ExecutionCostVector costVector, boolean compacted) {
        double AFDPC = 0;
        int mi = coverageMatrix.numberOfTargets();
        int m = coverageMatrix.numberOfOriginalTargets();
        int n = coverageMatrix.numberOfTests();
        int[] TF = new int[mi];

        if (m == 0) {
            m = mi;
        }

        for (int i = 0; i < TF.length; i++) {
            TF[i] = -1;
        }

        int[] solutionArray = ((Permutation) solution.getDecisionVariables()[0]).vector_;
        for (int i = 0; i < mi; i++) {
            for (int j = 0; j < solutionArray.length; j++) {
                if (coverageMatrix.getElement(solutionArray[j], i) > 0) {
                    TF[i] = j;
                    break;
                }
            }
        }

        for (int i = 0; i < mi; i++) {
            double sum = 0;
            if (TF[i] == -1) {
                continue;
            }
            for (int j = TF[i]; j < n; j++) {
                sum += costVector.getCostOfTest(solutionArray[j]);
            }
            sum = sum - (1d / 2d * costVector.getCostOfTest(solutionArray[TF[i]]));

            if (compacted) {
                int ci = coverageMatrix.getElement(solutionArray[TF[i]], i);
                AFDPC += ci * sum;
            } else {
                AFDPC += sum;
            }
        }
        double denominator = 0;
        for (int j = 0; j < n; j++) {
            denominator += costVector.getCostOfTest(j) * m;
        }
        AFDPC /= denominator;
        return AFDPC;
    }
}
