package music;

import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.core.Bead;
import net.beadsproject.beads.data.Buffer;
import net.beadsproject.beads.ugens.*;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class OscillatorController {

    private static float[] notesOfDDorian = {
            146.83f, 196.00f, 220.00f,
            293.66f, 329.63f, 349.23f, 392, 440, 493.88f, 523.25f, 587.33f};
    private float[] noteRelations = {1, 1.335f, 1.5f, 2};
    private static List<Buffer> waveForms = Arrays.asList(Buffer.SAW //, Buffer.SQUARE //,Buffer.SINE//Buffer.TRIANGLE
            );

    private int beatPerNote;
    private int beatCounter = 0;
    private int filterCounter = 0;
    private float actualFreq;
    private float freq;

    private BeadsGenerator beadsGenerator;
    private AudioContext audioContext;

    private Random random;
    private WavePlayer wavePlayer;
    private WavePlayer secondWave;
    private Bead noteModulator;
    private LPRezFilter lowPassFilter;
    private Envelope lowPassEnvelope;
    private BiquadFilter highPassFilter;
    private Glide glide;
    private Gain positionalGain;
    private Gain limiter;
    private Gain tremoloGain;

    public OscillatorController(BeadsGenerator beadsGenerator) {
        this.beadsGenerator = beadsGenerator;
        audioContext = beadsGenerator.getAc();
        random = new Random();
        freq = notesOfDDorian[random.nextInt(notesOfDDorian.length)];
        actualFreq = freq;
        lowPassEnvelope = new Envelope(audioContext);
        calculateNoteRelations();
        calculateBeatPerNote();
        Buffer waveForm = waveForms.get(random.nextInt(waveForms.size()));
        setUpSound(waveForm);
    }

    private void calculateBeatPerNote() {
        double gauss = random.nextGaussian() * 4 + 12;
        beatPerNote = (int) Math.round(gauss);
        if (beatPerNote > 16) {
            beatPerNote = 16;
        } else if (beatPerNote < 1) {
            beatPerNote = 1;
        }
    }

    private void calculateNoteRelations() {
        if (freq > 349 && freq < 359) {
            noteRelations[1] = 1;
        } else if (freq > 493 && freq < 523) {
            noteRelations[2] = 1;
        }
    }

    private void setUpSound(Buffer waveForm) {
        Reverb reverb = new Reverb(audioContext, 1);
        if (waveForm == Buffer.SAW) {
            reverb.setSize(0.6f);
            WavePlayer modulator = new WavePlayer(audioContext, 0.1f, Buffer.SINE);
            Function frequencyModulation = new Function(modulator) {
                @Override
                public float calculate() {
                    return (x[0] * 3.0f) + actualFreq;
                }
            };
            wavePlayer = new WavePlayer(audioContext, frequencyModulation, waveForm);
            secondWave = new WavePlayer(audioContext, freq, Buffer.SAW);
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
            lowPassFilter = new LPRezFilter(audioContext, lowPassEnvelope, 0.7f);
            lowPassFilter.addInput(wavePlayer);
            lowPassFilter.addInput(secondWave);
        } else if (waveForm == Buffer.SQUARE) {
            reverb.setSize(0.2f);
            wavePlayer = new WavePlayer(audioContext, freq, waveForm);
            WavePlayer LFO = new WavePlayer(audioContext, 2, Buffer.SINE);
            Gain lfoGain = new Gain(audioContext, 1, 0.8f);
            lfoGain.addInput(LFO);
            Function tremolo = new Function(lfoGain) {
                @Override
                public float calculate() {
                    return x[0];
                }
            };
            lowPassFilter = new LPRezFilter(audioContext, 8000, 0);
            tremoloGain = new Gain(audioContext, 1, tremolo);
            tremoloGain.addInput(wavePlayer);
            lowPassFilter.addInput(tremoloGain);
        } else {
            reverb.setSize(0.2f);
            wavePlayer = new WavePlayer(audioContext, freq, waveForm);
            lowPassFilter = new LPRezFilter(audioContext, 8000, 0);
            lowPassFilter.addInput(wavePlayer);
        }
        glide = new Glide(audioContext, 0, 50);
        highPassFilter = new BiquadFilter(audioContext, BiquadFilter.HP, freq-100, 0.2f);
        positionalGain = new Gain(audioContext, 1, glide);
        Clock clock = beadsGenerator.getBeatClock();
        limiter = new Gain(audioContext, 1, 0.5f);
        noteModulator = new Bead() {
            public void messageReceived(Bead message) {
                if (beatCounter >= beatPerNote) {
                    actualFreq = freq * noteRelations[random.nextInt(noteRelations.length)];
                    wavePlayer.setFrequency(actualFreq);
                    beatCounter = 0;
                } else {
                    beatCounter++;
                }
            }
        };
        clock.addMessageListener(noteModulator);
        highPassFilter.addInput(lowPassFilter);
        positionalGain.addInput(highPassFilter);
        reverb.addInput(positionalGain);
        limiter.addInput(reverb);
        audioContext.out.addInput(limiter);
    }

    public void removeOscillator() {
        glide.setValue(0);

        wavePlayer.kill();
//        secondWave.kill();
        lowPassFilter.kill();
        highPassFilter.kill();
        positionalGain.kill();
        limiter.kill();
        glide.kill();
        noteModulator.kill();
        //beadsGenerator.removeOscillator(this);
    }

    public Glide getGlide() {
        return glide;
    }

    public BeadsGenerator getBeadsGenerator() {
        return beadsGenerator;
    }
}
