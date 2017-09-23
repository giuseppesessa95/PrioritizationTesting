package it.unisa.prioritization.study;

import it.unisa.prioritization.problems.MultiObjectiveGeneralizedPrioritizationProblem;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.multiobjective.nsgaii.NSGAIIBuilder;
import org.uma.jmetal.operator.impl.crossover.PMXCrossover;
import org.uma.jmetal.operator.impl.mutation.PermutationSwapMutation;
import org.uma.jmetal.qualityindicator.impl.Epsilon;
import org.uma.jmetal.qualityindicator.impl.GenerationalDistance;
import org.uma.jmetal.qualityindicator.impl.InvertedGenerationalDistance;
import org.uma.jmetal.qualityindicator.impl.InvertedGenerationalDistancePlus;
import org.uma.jmetal.qualityindicator.impl.Spread;
import org.uma.jmetal.qualityindicator.impl.hypervolume.PISAHypervolume;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.solution.PermutationSolution;
import org.uma.jmetal.solution.impl.DefaultIntegerPermutationSolution;
import org.uma.jmetal.util.experiment.Experiment;
import org.uma.jmetal.util.experiment.ExperimentBuilder;
import org.uma.jmetal.util.experiment.component.ComputeQualityIndicators;
import org.uma.jmetal.util.experiment.component.ExecuteAlgorithms;
import org.uma.jmetal.util.experiment.component.GenerateBoxplotsWithR;
import org.uma.jmetal.util.experiment.component.GenerateFriedmanTestTables;
import org.uma.jmetal.util.experiment.component.GenerateLatexTablesWithStatistics;
import org.uma.jmetal.util.experiment.component.GenerateWilcoxonTestTablesWithR;
import org.uma.jmetal.util.experiment.util.ExperimentAlgorithm;
import org.uma.jmetal.util.experiment.util.ExperimentProblem;

/**
 *
 * @author Administrator
 */
public class TestingPrioritizationStudy {

    private static final int INDEPENDENT_RUNS = 25;

    public static void main(String[] args) throws IOException {
        String experimentBaseDirectory = "";

        List<String> coverageFilenames = new ArrayList<>();
        String costFilename = "";
        String faultFilename = "";
        
        List<ExperimentProblem<PermutationSolution<Integer>>> problemList = new ArrayList<>();
        problemList.add(new ExperimentProblem<>(new MultiObjectiveGeneralizedPrioritizationProblem(coverageFilenames, costFilename, faultFilename)));

        List<ExperimentAlgorithm<PermutationSolution<Integer>, List<PermutationSolution<Integer>>>> algorithmList = configureAlgorithmList(problemList);

        List<String> referenceFrontFileNames = Arrays.asList("SingleObjectiveGeneralizedPrioritizationProblem.pf");

        Experiment<DefaultIntegerPermutationSolution, List<PermutationSolution>> experiment = new ExperimentBuilder<PermutationSolution<Integer>, List<PermutationSolution>>("NSGAIIStudy")
                .setAlgorithmList(algorithmList)
                .setProblemList(problemList)
                .setExperimentBaseDirectory(experimentBaseDirectory)
                .setOutputParetoFrontFileName("FUN")
                .setOutputParetoSetFileName("VAR")
                .setReferenceFrontDirectory("/pareto_fronts")
                .setReferenceFrontFileNames(referenceFrontFileNames)
                .setIndicatorList(Arrays.asList(
                        new Epsilon<DoubleSolution>(),
                        new Spread<DoubleSolution>(),
                        new GenerationalDistance<DoubleSolution>(),
                        new PISAHypervolume<DoubleSolution>(),
                        new InvertedGenerationalDistance<DoubleSolution>(),
                        new InvertedGenerationalDistancePlus<DoubleSolution>()))
                .setIndependentRuns(INDEPENDENT_RUNS)
                .setNumberOfCores(8)
                .build();
        new ExecuteAlgorithms<>(experiment).run();
        new ComputeQualityIndicators<>(experiment).run();
        new GenerateLatexTablesWithStatistics(experiment).run();
        new GenerateWilcoxonTestTablesWithR<>(experiment).run();
        new GenerateFriedmanTestTables<>(experiment).run();
        new GenerateBoxplotsWithR<>(experiment).setRows(3).setColumns(3).run();

    }

    static List<ExperimentAlgorithm<PermutationSolution<Integer>, List<PermutationSolution>>> configureAlgorithmList(
            List<ExperimentProblem<PermutationSolution<Integer>>> problemList) {
        
        List<ExperimentAlgorithm<PermutationSolution, List<PermutationSolution>>> algorithms = new ArrayList<>();

        for (int i = 0; i < problemList.size(); i++) {
            Algorithm<List<PermutationSolution>> algorithm = new NSGAIIBuilder<>(
                    problemList.get(0).getProblem(),
                    new PMXCrossover(0.9),
                    new PermutationSwapMutation<>(1.0 / problemList.get(0).getProblem().getNumberOfVariables()))
                    .setMaxEvaluations(25000)
                    .setPopulationSize(100)
                    .build();
            algorithms.add(new ExperimentAlgorithm<>(algorithm, "NSGAII", problemList.get(0).getTag()));
        }
        return algorithms;
    }

}
