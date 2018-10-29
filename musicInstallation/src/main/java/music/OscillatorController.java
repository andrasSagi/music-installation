package music;

import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.data.Buffer;
import net.beadsproject.beads.ugens.WavePlayer;

public class OscillatorController {

    private Oscillator oscillator;
    private BeadsGenerator beadsGenerator;
    private WavePlayer wavePlayer;
    AudioContext audioContext;

    public OscillatorController(Oscillator oscillator, BeadsGenerator beadsGenerator) {
        this.oscillator = oscillator;
        this.beadsGenerator = beadsGenerator;
        audioContext = beadsGenerator.getAc();
        wavePlayer = new WavePlayer(audioContext, 440, Buffer.SAW);
        audioContext.out.addInput(wavePlayer);
    }

    public void removeOscillator(Oscillator oscillator) {
        wavePlayer.kill();

    }
}
