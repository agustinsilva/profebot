package ia.module.genetic.algorithm;

import io.jenetics.prog.*;
import ia.module.fitness.*;
import ia.module.parser.*;
import io.jenetics.ext.util.*;
import io.jenetics.prog.op.*;
import io.jenetics.ext.*;
import java.util.function.*;
import io.jenetics.engine.*;
import java.util.stream.*;
import ia.module.config.*;
import io.jenetics.*;
import io.jenetics.util.*;

public class GeneticAlgorithm
{
    private static SimilarExpressionCalculator SIMILAR_EXPRESSION_CALCULATOR;
    private static final Codec<ProgramGene<Double>, ProgramGene<Double>> CODEC;
    
    public GeneticAlgorithm(final String candidate) {
        GeneticAlgorithm.SIMILAR_EXPRESSION_CALCULATOR = new ProceduralSimilarExpressionCalculator(candidate);
    }
    
    public GeneticAlgorithm(final String candidate, final Boolean useNeuralNetworkFitness) {
        GeneticAlgorithm.SIMILAR_EXPRESSION_CALCULATOR = (((boolean)useNeuralNetworkFitness) ? new NeuralNetworkSimilarExpressionCalculator(candidate) : new ProceduralSimilarExpressionCalculator(candidate));
    }
    
    private static final Double fitnessFunction(final ProgramGene<Double> expression) {
        final String otherExpression = new Parser().getAsInfix((TreeNode<Op<Double>>)TreeNode.ofTree((Tree)expression));
        return GeneticAlgorithm.SIMILAR_EXPRESSION_CALCULATOR.similarityWith(otherExpression);
    }
    
    public String getExpressionMostSimilar() {
        final Engine<ProgramGene<Double>, Double> engine = (Engine<ProgramGene<Double>, Double>)Engine.builder((Function)GeneticAlgorithm::fitnessFunction, (Codec)GeneticAlgorithm.CODEC).alterers((Alterer)new Mutator(0.03), new Alterer[] { new SingleNodeCrossover() }).populationSize(100).executor(Runnable::run).maximizing().build();
        final Phenotype<ProgramGene<Double>, Double> bestExpression = (Phenotype<ProgramGene<Double>, Double>)engine.stream().limit(Limits.bySteadyFitness(20)).peek((Consumer)GeneticAlgorithm::showGeneration).collect(EvolutionResult.toBestPhenotype());
        final TreeNode bestCandidate = TreeNode.ofTree((Tree)bestExpression.getGenotype().getGene());
        return new Parser().getAsInfix((TreeNode<Op<Double>>)bestCandidate);
    }
    
    public static void showGeneration(final EvolutionResult<ProgramGene<Double>, Double> generation) {
        final TreeNode bestCandidate = TreeNode.ofTree((Tree)generation.getBestPhenotype().getGenotype().getGene());
        final String candidateAsInfix = new Parser().getAsInfix((TreeNode<Op<Double>>)bestCandidate);
        System.out.println("Generation: " + generation.getGeneration() + "; Best fitness: " + GeneticAlgorithm.SIMILAR_EXPRESSION_CALCULATOR.similarityWith(candidateAsInfix) + "; Best genotype: " + candidateAsInfix);
    }
    
    public SimilarExpressionCalculator getSimilarExpressionCalculator() {
        return GeneticAlgorithm.SIMILAR_EXPRESSION_CALCULATOR;
    }
    
    static {
        CODEC = Codec.of((Factory)Genotype.of((Chromosome)GeneticAlgorithmConfig.CHROMOSOME, new Chromosome[0]), (Function)Genotype::getGene);
    }
}
