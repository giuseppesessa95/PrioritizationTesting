
package it.unisa.prioritization.study;

import it.unisa.prioritization.algorithm.NSGASettings;
import it.unisa.prioritization.problems.MultiObjectiveGeneralizedPrioritizationProblem;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.multiobjective.nsgaii.NSGAIIBuilder;
import org.uma.jmetal.operator.impl.crossover.SBXCrossover;
import org.uma.jmetal.operator.impl.mutation.PolynomialMutation;
import org.uma.jmetal.qualityindicator.impl.Epsilon;
import org.uma.jmetal.qualityindicator.impl.GenerationalDistance;
import org.uma.jmetal.qualityindicator.impl.InvertedGenerationalDistance;
import org.uma.jmetal.qualityindicator.impl.InvertedGenerationalDistancePlus;
import org.uma.jmetal.qualityindicator.impl.Spread;
import org.uma.jmetal.qualityindicator.impl.hypervolume.PISAHypervolume;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.JMetalException;
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


public class TestingPrioritizationStudyII 
{
    private static final int INDEPENDENT_RUNS = 25;
    
    public static void main(String[] args) throws IOException 
    {
        if (args.length != 1) 
        {
            throw new JMetalException("Missing argument: experimentBaseDirectory");
        }
        String experimentBaseDirectory = args[0];
        List<ExperimentProblem<DoubleSolution>> problemList = new ArrayList<>();
        problemList.add(new ExperimentProblem<>(new NSGASettings()));
        problemList.add(new ExperimentProblem<>(new MultiObjectiveGeneralizedPrioritizationProblem(coverageFilenames, experimentBaseDirectory, experimentBaseDirectory)))
        List<ExperimentAlgorithm<DoubleSolution, List<DoubleSolution>>> algorithmList = configureAlgorithmList(problemList);
        List<String> referenceFrontFileNames = Arrays.asList("NSGASettings.pf");
        Experiment<DoubleSolution, List<DoubleSolution>> experiment =
            new ExperimentBuilder<DoubleSolution, List<DoubleSolution>>("NSGAIIStudy")
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
        
        static List<ExperimentAlgorithm<DoubleSolution, List<DoubleSolution>>> configureAlgorithmList(
          List<ExperimentProblem<DoubleSolution>> problemList) {
    List<ExperimentAlgorithm<DoubleSolution, List<DoubleSolution>>> algorithms = new ArrayList<>();

        for (int i = 0; i < problemList.size(); i++) 
        {
            Algorithm<List<DoubleSolution>> algorithm = new NSGAIIBuilder<>(
                problemList.get(i).getProblem(),
                new SBXCrossover(1.0, 5),
                new PolynomialMutation(1.0 / problemList.get(i).getProblem().getNumberOfVariables(), 10.0))
                .setMaxEvaluations(25000)
                .setPopulationSize(100)
                .build();
            algorithms.add(new ExperimentAlgorithm<>(algorithm, "NSGAIIa", problemList.get(i).getTag()));
        }
        
        for (int i = 0; i < problemList.size(); i++) 
        {
            Algorithm<List<DoubleSolution>> algorithm = new NSGAIIBuilder<>(
                problemList.get(i).getProblem(),
                new SBXCrossover(1.0, 20.0),
                new PolynomialMutation(1.0 / problemList.get(i).getProblem().getNumberOfVariables(), 20.0))
                .setMaxEvaluations(25000)
                .setPopulationSize(100)
                .build();
            algorithms.add(new ExperimentAlgorithm<>(algorithm, "NSGAIIb", problemList.get(i).getTag()));
        }
        
        for (int i = 0; i < problemList.size(); i++) 
        {
            Algorithm<List<DoubleSolution>> algorithm = new NSGAIIBuilder<>(problemList.get(i).getProblem(), new SBXCrossover(1.0, 40.0),
                new PolynomialMutation(1.0 / problemList.get(i).getProblem().getNumberOfVariables(), 40.0))
                .setMaxEvaluations(25000)
                .setPopulationSize(100)
                .build();
            algorithms.add(new ExperimentAlgorithm<>(algorithm, "NSGAIIc", problemList.get(i).getTag()));
        }
        
        for (int i = 0; i < problemList.size(); i++) 
        {
            Algorithm<List<DoubleSolution>> algorithm = new NSGAIIBuilder<>(problemList.get(i).getProblem(), new SBXCrossover(1.0, 80.0),
                new PolynomialMutation(1.0 / problemList.get(i).getProblem().getNumberOfVariables(), 80.0))
                .setMaxEvaluations(25000)
                .setPopulationSize(100)
                .build();
            algorithms.add(new ExperimentAlgorithm<>(algorithm, "NSGAIId", problemList.get(i).getTag()));
        }
        
        return algorithms;
    }
    
}
