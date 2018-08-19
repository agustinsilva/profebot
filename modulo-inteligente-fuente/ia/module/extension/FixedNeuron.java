package ia.module.extension;

import org.neuroph.core.*;

public class FixedNeuron extends Neuron
{
    public void calculate() {
        if (this.inputConnections.size() != 0) {
            this.totalInput = this.inputFunction.getOutput(this.inputConnections);
        }
        this.output = this.transferFunction.getOutput(this.totalInput);
    }
}
