package it.unisa.prioritization;

import it.unisa.prioritization.problems.GeneralizedPrioritizationProblem;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.management.JMException;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.problem.PermutationProblem;
import org.uma.jmetal.solution.SolutionBuilder.Variable;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.comparator.DominanceComparator;
import jmetal.core.SolutionSet; // modificare in jMetal 5
/**
 *
 * @author dardin88
 */
public class ParetoFrontUtil {

    public int[] rankPointbyPointSolutions(String gde3SolutionPath, String moeadSolutionPath, String hgaSolutionPath, GeneralizedPrioritizationProblem problem) throws IOException {
        SolutionSet gde3Solutions = readSolutions(gde3SolutionPath, problem);
        SolutionSet moeadSolutions = readSolutions(moeadSolutionPath, problem);
        SolutionSet hgaSolutions = readSolutions(hgaSolutionPath, problem);

        int[] points = new int[3];

        SolutionSet paretoFront = initFront(gde3Solutions.get(0), problem);
        
        for (int i = 1; i < gde3Solutions.size(); i++) {
            paretoFront = computeCurrentSolution(gde3Solutions.get(i), paretoFront, problem);
            System.out.println("GDE3 " + i);
        }

        for (int i = 0; i < moeadSolutions.size(); i++) {
            paretoFront = computeCurrentSolution(moeadSolutions.get(i), paretoFront, problem);
            System.out.println("MOEAD " + i);
        }

        for (int i = 0; i < hgaSolutions.size(); i++) {
            paretoFront = computeCurrentSolution(hgaSolutions.get(i), paretoFront, problem);
            System.out.println("HGA");
        }

        int gde3Points = 0;
        for (int i = 0; i < gde3Solutions.size(); i++) {
            gde3Points += this.countPointsOnFront(paretoFront, gde3Solutions.get(i), problem);
        }

        gde3Points /= gde3Solutions.size();

        int moeadPoints = 0;
        for (int i = 0; i < moeadSolutions.size(); i++) {
            moeadPoints += this.countPointsOnFront(paretoFront, moeadSolutions.get(i), problem);
        }

        moeadPoints /= moeadSolutions.size();

        int hgaPoints = 0;
        for (int i = 0; i < hgaSolutions.size(); i++) {
            hgaPoints += countPointsOnFront(paretoFront, hgaSolutions.get(i), problem);
        }

        points[0] = gde3Points;
        points[1] = moeadPoints;
        points[2] = hgaPoints;

        return points;
    }

    public SolutionSet readSolutions(String solutionFile, GeneralizedPrioritizationProblem problem) throws IOException {
        FileReader fr = new FileReader(solutionFile);
        BufferedReader br = new BufferedReader(fr);
        SolutionSet solutions = new SolutionSet(100);
        String solutionLine;
        while ((solutionLine = br.readLine()) != null) {
            StringTokenizer st = new StringTokenizer(solutionLine, " ");
            int[] readSolution = new int[st.countTokens()];
            int solutionCounter = 0;
            while (st.hasMoreTokens()) {
                readSolution[solutionCounter] = Integer.parseInt(st.nextToken());
                solutionCounter++;
            }
            Variable[] variables = new Variable[1];
            Permutation variable = new Permutation(readSolution.length);
            variable.vector_ = readSolution;
            variables[0] = variable;
            Solution solution = new Solution(problem, variables);

            solutions.add(solution);
        }
        return solutions;
    }

    public SolutionSet initFront(Solution solution, GeneralizedPrioritizationProblem problem) {
        SolutionSet paretoFront = new SolutionSet(10000000);
        SolutionSet solutionPoints = this.splitSolutionPointByPoint(solution, problem);
        for (int i = 0; i < solutionPoints.size(); i++) {
            Solution solutionPoint = solutionPoints.get(i);
            problem.evaluate(solutionPoint);
            paretoFront.add(solutionPoint);
        }
        return paretoFront;
    }

    public SolutionSet computeCurrentSolution(Solution solution, SolutionSet paretoFront, GeneralizedPrioritizationProblem problem) {
        DominanceComparator comparator = new DominanceComparator();
        SolutionSet solutionPoints = this.splitSolutionPointByPoint(solution, problem);
        for (int i = 0; i < solutionPoints.size(); i++) {
            Solution solutionPoint = solutionPoints.get(i);
            Permutation currentSolutionPermutation = (Permutation) solutionPoint.getDecisionVariables()[0];
            if (paretoFront.size() == 0) {
                problem.evaluate(solutionPoint);
                paretoFront.add(solutionPoint);
            } else {
                for (int j = 0; j < paretoFront.size(); j++) {
                    Solution paretoFrontPoint = paretoFront.get(j);
                    Permutation currentParetoPermutation = (Permutation) paretoFrontPoint.getDecisionVariables()[0];
                    if (!Arrays.equals(currentSolutionPermutation.vector_, currentParetoPermutation.vector_)) {
                        
                        problem.evaluate(solutionPoint);
                        int compareResult = comparator.compare(solutionPoint, paretoFrontPoint);
                        if (compareResult == -1) {
                            paretoFront.remove(j);
                            paretoFront.add(j, solutionPoint);
                        } else if (compareResult == 0) {
                            paretoFront.add(solutionPoint);
                        }
                    }
                }
            }
        }
        return paretoFront;
    }

    public SolutionSet splitSolutionPointByPoint(Solution solution, GeneralizedPrioritizationProblem problem) {
        int[] solutionArray = ((Permutation) solution.getDecisionVariables()[0]).vector_;

        SolutionSet splittedSolution = new SolutionSet();

        for (int solutionPointCounter = 0; solutionPointCounter < solutionArray.length; solutionPointCounter++) {
            Solution solutionPoint = new Solution(problem.getNumberOfObjectives());
            Variable[] variables = new Variable[1];
            int[] solutionFraction = Arrays.copyOfRange(solutionArray, 0, solutionPointCounter + 1);
            Permutation variable = new Permutation(solutionFraction.length);
            System.arraycopy(solutionFraction, 0, variable.vector_, 0, solutionFraction.length);
            variables[0] = variable;
            solutionPoint.setDecisionVariables(variables);

            splittedSolution.setCapacity(1000000);
            splittedSolution.add(solutionPoint);
        }
        return splittedSolution;
    }

    public int countPointsOnFront(SolutionSet paretoFront, Solution newSolution, GeneralizedPrioritizationProblem problem) {
        SolutionSet solutionPoints = this.splitSolutionPointByPoint(newSolution, problem);
        int numOfPoints = 0;

        for (int i = 0; i < solutionPoints.size(); i++) {
            Permutation solutionPermutation = (Permutation) solutionPoints.get(i).getDecisionVariables()[0];
            for (int j = 0; j < paretoFront.size(); j++) {
                Permutation paretoPointPermutation = (Permutation) paretoFront.get(j).getDecisionVariables()[0];
                if (Arrays.equals(solutionPermutation.vector_, paretoPointPermutation.vector_)) {
                    numOfPoints++;
                }
            }
        }

        return numOfPoints;
    }
}
