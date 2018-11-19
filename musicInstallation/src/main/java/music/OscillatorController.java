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
    private float actualFrequency;
    private float baseFrequency;

    private BeadsGenerator beadsGenerator;
    private AudioContext audioContext;

    private Random random;

    private WavePlayer primaryOscillator;
    private WavePlayer secondaryOscillator;
    private WavePlayer LFO;

    private Bead noteModulator;
    private Glide glide;
    private Gain positionalGain;
    private Gain limiter;
    private Panner panner;
    private Buffer waveForm;

    private FilterModule filterModule;
    private AmpModule ampModule;
    private EffectModule effectModule;

    public OscillatorController(BeadsGenerator beadsGenerator) {
        this.beadsGenerator = beadsGenerator;
        audioContext = beadsGenerator.getAc();
        random = new Random();
        baseFrequency = notesOfDDorian[random.nextInt(notesOfDDorian.length)];
        actualFrequency = baseFrequency;
        calculateNoteRelations();
        calculateBeatPerNote();
        waveForm = waveForms.get(random.nextInt(waveForms.size()));
        primaryOscillator = new WavePlayer(audioContext, actualFrequency, waveForm);
        secondaryOscillator = new WavePlayer(audioContext, actualFrequency, waveForm);
        LFO = new WavePlayer(audioContext, 0.1f, Buffer.SINE);
        glide = new Glide(audioContext, 0, 50);
        positionalGain = new Gain(audioContext, 1, glide);
        limiter = new Gain(audioContext, 1, 0.8f);
        panner = new Panner(audioContext, 0);
        filterModule = new FilterModule(this);
        ampModule = new AmpModule(this);
        effectModule = new EffectModule(this);
        setUpSignalChain();
        setNoteTriggerToClock();
        setUpSound();
    }

    private void setUpSignalChain() {
        filterModule.addInput(primaryOscillator);
        filterModule.addInput(secondaryOscillator);
        ampModule.setInputModule(filterModule);
        effectModule.setInputModule(ampModule);
        limiter.addInput(effectModule.getOutput());
        positionalGain.addInput(limiter);
        panner.addInput(positionalGain);
        audioContext.out.addInput(panner);
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
        if (baseFrequency > 349 && baseFrequency < 359) {
            noteRelations[1] = 1;
        } else if (baseFrequency > 493 && baseFrequency < 523) {
            noteRelations[2] = 1;
        }
    }

    private void setUpSound() {
        if (waveForm == Buffer.SAW) {
            Function frequencyModulation = new Function(LFO) {
                @Override
                public float calculate() {
                    return (x[0] * 3.0f) + actualFrequency;
                }
            };
            primaryOscillator.setFrequency(frequencyModulation);
        }
    }

    private void setNoteTriggerToClock() {
        Clock clock = beadsGenerator.getBeatClock();
        noteModulator = new Bead() {
            public void messageReceived(Bead message) {
                if (beatCounter >= beatPerNote) {
                    actualFrequency = baseFrequency * noteRelations[random.nextInt(noteRelations.length)];
                    secondaryOscillator.setFrequency(actualFrequency);
                    beatCounter = 0;
                } else {
                    beatCounter++;
                }
            }
        };
        clock.addMessageListener(noteModulator);
    }

    public void removeOscillator() {
        glide.setValue(0);
        effectModule.kill();
        ampModule.kill();
        filterModule.kill();
        primaryOscillator.kill();
        secondaryOscillator.kill();
        LFO.kill();
        positionalGain.kill();
        limiter.kill();
        glide.kill();
        noteModulator.kill();
    }

    AudioContext getAudioContext() {
        return beadsGenerator.getAc();
    }

    public Glide getGlide() {
        return glide;
    }

    float getBaseFrequency() {
        return baseFrequency;
    }

    public Panner getPanner() {
        return panner;
    }

    Buffer getWaveForm() {
        return waveForm;
    }
}
