package music;

import net.beadsproject.beads.core.UGen;
import net.beadsproject.beads.ugens.Gain;
import net.beadsproject.beads.ugens.Reverb;

public class EffectModule implements Module {

    private OscillatorController controller;
    private Reverb reverb;
    private Gain gain;

    public EffectModule(OscillatorController controller) {
        this.controller = controller;
        reverb = new Reverb(controller.getAudioContext(), 1);
        gain = new Gain(controller.getAudioContext(), 1, 0.6f);
        gain.addInput(reverb);
    }

    @Override
    public void setInputModule(Module module) {
        reverb.addInput(module.getOutput());
    }

    @Override
    public UGen getOutput() {
        return gain;
    }
}
