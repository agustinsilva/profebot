package ia.module.config;

public class NeuralNetworkConfig
{
    public static int INPUTS;
    public static int OUTPUTS;
    public static int TRAINING_EXAMPLES;
    public static double BASE_SIMILARITY_MAIN_CATEOGORY;
    public static double BASE_SIMILARITY_SECONDARY_CATEGORY;
    public static double BASE_SIMILARITY_OTHER_CATEGORY;
    public static double SIMILAR_CATEGORY_LIMIT;
    
    static {
        NeuralNetworkConfig.INPUTS = 21;
        NeuralNetworkConfig.OUTPUTS = 400;
        NeuralNetworkConfig.TRAINING_EXAMPLES = 1000;
        NeuralNetworkConfig.BASE_SIMILARITY_MAIN_CATEOGORY = 0.8;
        NeuralNetworkConfig.BASE_SIMILARITY_SECONDARY_CATEGORY = 0.5;
        NeuralNetworkConfig.BASE_SIMILARITY_OTHER_CATEGORY = 0.0;
        NeuralNetworkConfig.SIMILAR_CATEGORY_LIMIT = 0.01;
    }
}
