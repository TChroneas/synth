


import com.jsyn.JSyn;
import com.jsyn.Synthesizer;
import com.jsyn.ports.UnitInputPort;
import com.jsyn.unitgen.Add;
import com.jsyn.unitgen.FilterLowPass;
import com.jsyn.unitgen.LineOut;
import com.jsyn.unitgen.PulseOscillatorBL;
import com.jsyn.unitgen.RedNoise;
import com.jsyn.unitgen.SineOscillator;
import com.jsyn.unitgen.UnitOscillator;
import com.jsyn.unitgen.UnitVoice;
import com.softsynth.shared.time.TimeStamp;;

public class SynthesisDemo {


    public void test() {
        int[] pattern = new int[] { 1, 2, 2, 3, 5, 1, 1, 2, 2, 3, 5, 1 };
        play(pattern);
    }

    private Synthesizer synth;
    private UnitOscillator osc;
    private SineOscillator lfo;
    private Add adder;
    private FilterLowPass filter;
    private SineOscillator lfo2;
    private RedNoise noise;
    private Add mixer;
    private LineOut lineOut;

    private UnitVoice voice;

    private static final double DURATION = 0.2;
    final double ROOT = Math.pow(2., 1. / 12.);
    private double duration = DURATION;
    private final double freq = 444 / 4;

    public void modularPatching() {
        synth.add(osc = new PulseOscillatorBL());
        synth.add(noise = new RedNoise());
        synth.add(lfo = new SineOscillator());
        synth.add(lfo2 = new SineOscillator()); // pwm modulation
        synth.add(filter = new FilterLowPass());
        synth.add(adder = new Add()); // lfo -> cutoff
        synth.add(mixer = new Add()); // osc + noise
        synth.add(lineOut = new LineOut());

        // PWM modulation at 7Hz
        lfo2.amplitude.set(0.6); // 0.6 (60%)
        lfo2.frequency.set(7); // 7Hz
        final UnitInputPort cycleTime = (UnitInputPort) osc.getPortByName("width");
        lfo2.output.connect(cycleTime);

        // Dubstep Wobble (LFO-modulated filter)
        lfo.amplitude.set(500); // 500Hz frequency modulation range
        lfo.getOutput().connect(adder.inputA);
        adder.inputB.set(500); // 500Hz center frequency
        adder.output.connect(filter.frequency);

        // Some gritty red noise 40% in the mix
        osc.getOutput().connect(filter.input);
        filter.getOutput().connect(mixer.inputA);
        noise.amplitude.set(0.4); // 0.4 (40%)
        noise.getOutput().connect(mixer.inputB);

        mixer.output.connect(0, lineOut.input, 0);
        mixer.output.connect(0, lineOut.input, 1);

        voice = (UnitVoice) osc;
    }

    public enum Note {

        C(0), D(2), Eb(3), F(5), G(7), A(9), Bb(10);

        public final int semitone;

        private Note(int semitone) {
            this.semitone = semitone;
        }

        public final static Note fromInteger(int index) {
            return Note.values()[index % Note.values().length];
        }
    }

    public void play(final int[] pattern) {
        TimeStamp timeStamp = setup();
        for (int i = 0; i < 2; i++) { // 4
            for (Integer value : pattern) {
                final Note note = Note.fromInteger(value);
                timeStamp = addNote(timeStamp, freq * Math.pow(ROOT, note.semitone), (duration * value + 2.) / 8.);
                lfo.frequency.set(value * 2, timeStamp);
            }
        }
        play(timeStamp);
    }

    private TimeStamp addNote(TimeStamp timeStamp, double pitch, double duration) {
        voice.noteOn(pitch, 0.5, timeStamp);
        TimeStamp timestampOff = timeStamp.makeRelative(duration);
        voice.noteOff(timestampOff);
        return timestampOff;
    }

    private TimeStamp setup() {
        synth = JSyn.createSynthesizer();
        // Set output latency to 123 msec (not an interactive app)
        synth.getAudioDeviceManager().setSuggestedOutputLatency(0.123);

        modularPatching();

        // Start synthesizer using default stereo output at 44100 Hz.
        synth.start();

        // Get synthesizer time in seconds.
        double timeNow = synth.getCurrentTime();

        // Advance to a near future time so we have a clean start.
        TimeStamp timeStamp = new TimeStamp(timeNow + DURATION);

        // We only need to start the LineOut. It will pull data from the
        // oscillator.
        synth.startUnit(lineOut, timeStamp);
        return timeStamp;
    }

    private void play(TimeStamp timeStamp) {
        // Sleep while the song is being generated in the background thread.
        try {
            System.out.println("Sleep while synthesizing.");
            synth.sleepUntil(timeStamp.getTime() + 2.0);
            System.out.println("Woke up...");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // Stop everything.
        synth.stop();
    }

}