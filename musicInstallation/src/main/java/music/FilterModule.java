package music;

import net.beadsproject.beads.core.UGen;
import net.beadsproject.beads.ugens.BiquadFilter;
import net.beadsproject.beads.ugens.Envelope;
import net.beadsproject.beads.ugens.Gain;
import net.beadsproject.beads.ugens.LPRezFilter;

public class FilterModule implements Module {

    private OscillatorController controller;
    private Envelope lowPassEnvelope;
    private LPRezFilter lowPassFilter;
    private BiquadFilter highPassFilter;
    private Gain gain;

    public FilterModule(OscillatorController controller) {
        this.controller = controller;
        lowPassEnvelope = new Envelope(controller.getAudioContext(), 8000);
        lowPassFilter = new LPRezFilter(controller.getAudioContext(), lowPassEnvelope, 1);
        highPassFilter = new BiquadFilter(controller.getAudioContext(), BiquadFilter.HP, controller.getFreq() - 100, 0.2f);
        highPassFilter.addInput(lowPassFilter);
        gain = new Gain(controller.getAudioContext(), 1, 0.6f);
    }

    @Override
    public void setInputModule(Module module) {
        lowPassFilter.addInput(module.getOutput());
    }

    @Override
    public UGen getOutput() {
        return gain;
    }

    @Override
    public void kill() {
        gain.setGain(0);
        lowPassEnvelope.kill();
        lowPassFilter.kill();
        highPassFilter.kill();
        gain.kill();
    }
}
