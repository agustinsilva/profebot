package ia.module.fitness;

import ia.module.extension.*;
import ia.module.parser.tree.*;
import ia.module.config.*;
import org.neuroph.core.data.*;
import java.io.*;
import ia.module.parser.*;

public class NeuralNetworkSimilarExpressionCalculator extends SimilarExpressionCalculator
{
    private FixedKohonen network;
    public double[] originalExpressionInput;
    public double[] originalExpressionOutput;
    
    public NeuralNetworkSimilarExpressionCalculator(final String original) {
        super(original);
        this.trainNetwork();
        try {
            final ExpressionNode expressionNode = new Parser().parse(original);
            this.originalExpressionInput = expressionNode.extractFeaturesForExpression();
            this.originalExpressionOutput = this.calculateOutput(expressionNode);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public Double similarityWith(final String otherExpression) {
        double[] otherExpressionInput = new double[NeuralNetworkConfig.INPUTS];
        double[] otherExpressionOutput = new double[NeuralNetworkConfig.OUTPUTS];
        try {
            final ExpressionNode expressionNode = new Parser().parse(otherExpression);
            otherExpressionInput = expressionNode.extractFeaturesForExpression();
            otherExpressionOutput = this.calculateOutput(expressionNode);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return this.similarity(otherExpressionInput, otherExpressionOutput);
    }
    
    private double similarity(final double[] candidateInput, final double[] candidateOutput) {
        Integer diff = 0;
        for (int i = 0; i < NeuralNetworkConfig.OUTPUTS; ++i) {
            diff += (int)Math.abs(candidateOutput[i] - this.originalExpressionOutput[i]);
        }
        if (diff == 0) {
            return this.fineAdjustment(candidateInput);
        }
        return 1 - diff / NeuralNetworkConfig.OUTPUTS;
    }
    
    private double fineAdjustment(final double[] candidateInput) {
        final Double originalExpressionInputCategory = this.category(this.originalExpressionInput);
        final Double candidateExpressionInputCategory = this.category(candidateInput);
        final Double ratio = 1.0 - Math.abs(originalExpressionInputCategory - candidateExpressionInputCategory) / (originalExpressionInputCategory + candidateExpressionInputCategory);
        if (candidateExpressionInputCategory > originalExpressionInputCategory) {
            return 1.0 - ratio;
        }
        return ratio;
    }
    
    private void trainNetwork() {
        this.network = new FixedKohonen(NeuralNetworkConfig.INPUTS, NeuralNetworkConfig.OUTPUTS);
        this.trainWithExamples();
    }
    
    private void trainWithExamples() {
        try {
            final DataSet ds = new DataSet(NeuralNetworkConfig.INPUTS);
            final File file = new File("resources/training/patterns.text");
            final FileReader fileReader = new FileReader(file);
            final BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                final ExpressionNode expressionNode = new Parser().parse(line);
                ds.addRow(expressionNode.extractFeaturesForExpression());
            }
            this.network.learn(ds);
            fileReader.close();
        }
        catch (Exception e) {
            System.out.println("Error: " + e);
        }
    }
    
    private double[] calculateOutput(final ExpressionNode expression) {
        final double[] input = expression.extractFeaturesForExpression();
        this.network.setInput(input);
        this.network.calculate();
        final double[] output = this.network.getOutput();
        final DataSet ds = new DataSet(NeuralNetworkConfig.INPUTS);
        ds.addRow(input);
        this.network.learn(ds);
        return output;
    }
    
    private void print(final double[] vector, final Integer size) {
        String line = "(";
        for (int i = 0; i < size; ++i) {
            line += vector[i];
            if (i + 1 == size) {
                line += ")";
            }
            else {
                line += ",";
            }
        }
        System.out.println(line);
    }
    
    private Double category(final double[] input) {
        Double category = 0.0;
        for (int i = 0; i < NeuralNetworkConfig.INPUTS; ++i) {
            category += (Double)((input[i] != 0.0) ? Operator.getFibonacciWeight(i) : 0.0);
        }
        return category;
    }
}
