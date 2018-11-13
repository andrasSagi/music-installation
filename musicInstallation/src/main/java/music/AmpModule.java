package music;

import net.beadsproject.beads.core.UGen;
import net.beadsproject.beads.data.Buffer;
import net.beadsproject.beads.ugens.Envelope;
import net.beadsproject.beads.ugens.Gain;

public class AmpModule implements Module {

    private OscillatorController controller;
    private Envelope envelope;
    private Gain gain;

    public AmpModule(OscillatorController controller) {
        this.controller = controller;
        envelope = new Envelope(controller.getBeadsGenerator().getAc(), 0.6f);
        gain = new Gain(controller.getBeadsGenerator().getAc(), 1, envelope);
    }

    @Override
    public void setInputModule(Module module) {
        gain.addInput(module.getOutput());
    }

    @Override
    public UGen getOutput() {
        return gain;
    }
}
