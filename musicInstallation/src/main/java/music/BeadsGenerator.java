package music;

import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.ugens.Clock;

import java.util.ArrayList;
import java.util.List;

public class BeadsGenerator implements MusicGenerator {

    private List<OscillatorController> oscillators = new ArrayList<>();
    private AudioContext ac;
    private Clock beatClock;

    public BeadsGenerator() {
        ac = new AudioContext();
        beatClock = new Clock(ac, 1500);
        beatClock.setTicksPerBeat(4);
        ac.out.addDependent(beatClock);
    }

    AudioContext getAc() {
        return ac;
    }

    public void start() {
        ac.start();
    }

    void removeOscillator(OscillatorController oscillator) {
        oscillators.remove(oscillator);
    }

    public int getOscillatorNumber() {
        return oscillators.size();
    }

    public Clock getBeatClock() {
        return beatClock;
    }
}
