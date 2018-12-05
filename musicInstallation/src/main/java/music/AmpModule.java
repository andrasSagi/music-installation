package music;

import net.beadsproject.beads.core.UGen;
import net.beadsproject.beads.ugens.Envelope;
import net.beadsproject.beads.ugens.Gain;

public class AmpModule implements Module {

    private OscillatorController controller;
    private Envelope envelope;
    private Gain gain;

    AmpModule(OscillatorController controller) {
        this.controller = controller;
        envelope = new Envelope(controller.getAudioContext(), 0.8f);
        gain = new Gain(controller.getAudioContext(), 1, envelope);
    }

    @Override
    public void setInputModule(Module module) {
        gain.addInput(module.getOutput());
    }

    @Override
    public UGen getOutput() {
        return gain;
    }

    @Override
    public void kill() {
        gain.setGain(0);
        envelope.kill();
        gain.kill();
    }
}
