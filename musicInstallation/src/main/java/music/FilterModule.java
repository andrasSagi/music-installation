package music;

import net.beadsproject.beads.core.Bead;
import net.beadsproject.beads.core.UGen;
import net.beadsproject.beads.data.Buffer;
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

    FilterModule(OscillatorController controller) {
        this.controller = controller;
        lowPassEnvelope = new Envelope(controller.getAudioContext(), 8000);
        lowPassFilter = new LPRezFilter(controller.getAudioContext(), lowPassEnvelope, 0.7f);
        highPassFilter = new BiquadFilter(controller.getAudioContext(), BiquadFilter.HP, controller.getBaseFrequency() - 100, 0.2f);
        gain = new Gain(controller.getAudioContext(), 1, 0.6f);
        highPassFilter.addInput(lowPassFilter);
        gain.addInput(highPassFilter);
        if (controller.getWaveForm() == Buffer.SAW) {
            setSawEnvelope();
        }
    }

    private void setSawEnvelope() {
        Bead envelopeStarter = new Bead() {
            public void messageReceived(Bead message) {
                lowPassEnvelope.addSegment(8000, 9000);
                lowPassEnvelope.addSegment(8000, 6000);
                lowPassEnvelope.addSegment(0, 9000, this);
            }
        };
        lowPassEnvelope.addSegment(8000, 8000);
        lowPassEnvelope.addSegment(6000, 700);
        lowPassEnvelope.addSegment(6000, 1750);
        lowPassEnvelope.addSegment(0, 1750, envelopeStarter);
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

    void addInput(UGen uGen) {
        lowPassFilter.addInput(uGen);
    }
}
