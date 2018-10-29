package music;

import net.beadsproject.beads.core.AudioContext;
import java.util.ArrayList;
import java.util.List;

public class BeadsGenerator implements MusicGenerator {

    List<Oscillator> oscillators = new ArrayList<>();
    private AudioContext ac = new AudioContext();

    AudioContext getAc() {
        return ac;
    }

    public void start() {
        ac.start();
    }

}
