package ia.module.extension;

import org.neuroph.core.input.*;
import org.neuroph.core.transfer.*;
import org.neuroph.util.*;
import org.neuroph.nnet.learning.*;
import org.neuroph.core.learning.*;
import org.neuroph.core.*;

public class FixedKohonen extends NeuralNetwork
{
    public FixedKohonen(final int inputNeuronsCount, final int outputNeuronsCount) {
        this.createNetwork(inputNeuronsCount, outputNeuronsCount);
    }
    
    private void createNetwork(final int inputNeuronsCount, final int outputNeuronsCount) {
        final NeuronProperties inputNeuronProperties = new NeuronProperties((Class)FixedNeuron.class);
        final NeuronProperties outputNeuronProperties = new NeuronProperties((Class)FixedNeuron.class, (Class)Difference.class, (Class)Linear.class);
        this.setNetworkType(NeuralNetworkType.KOHONEN);
        final Layer inLayer = LayerFactory.createLayer(inputNeuronsCount, inputNeuronProperties);
        this.addLayer(inLayer);
        final Layer mapLayer = LayerFactory.createLayer(outputNeuronsCount, outputNeuronProperties);
        this.addLayer(mapLayer);
        ConnectionFactory.fullConnect(inLayer, mapLayer);
        NeuralNetworkFactory.setDefaultIO((NeuralNetwork)this);
        this.setLearningRule((LearningRule)new KohonenLearning());
    }
}
