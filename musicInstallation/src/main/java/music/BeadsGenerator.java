package music;

import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.ugens.Clock;


import java.util.ArrayList;
import java.util.List;

public class BeadsGenerator implements MusicGenerator {

    private List<OscillatorController> oscillators = new ArrayList<>();
    private AudioContext ac;
    private float tempoInMSec = 1500;
    private Clock beatClock;

    public BeadsGenerator() {
        ac = new AudioContext();
        beatClock = new Clock(ac, tempoInMSec);
        beatClock.setTicksPerBeat(4);
        ac.out.addDependent(beatClock);
        //Gain master = new Gain(ac, 1, 0.8f);
        ac.out.setGain(0.8f);
    }

    AudioContext getAc() {
        return ac;
    }

    public float getTempoInMSec() {
        return tempoInMSec;
    }

    public void start() {
        ac.start();
    }

    void addOscillator(OscillatorController oscillator) {
        oscillators.add(oscillator);
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
