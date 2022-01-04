import com.jsyn.ports.UnitInputPort;
import com.jsyn.ports.UnitOutputPort;
import com.jsyn.unitgen.*;
import com.softsynth.shared.time.TimeStamp;
public class MainCircuit2 extends Circuit implements UnitVoice {
    private UnitOscillator osc;
    private UnitOscillator gatingOsc;
    private FilterLowPass lowpass;
    private EnvelopeDAHDSR ampEnv;
    private EnvelopeDAHDSR filterEnv;
    private Add cutoffAdder;
    private Add lfo;
    private Multiply frequencyScaler;
    public UnitInputPort gatingOscAmp;
    public UnitInputPort gatingOscFreq;
    public UnitInputPort amplitude;
    public UnitInputPort frequency;
    public UnitInputPort pitchModulation;
    public UnitInputPort cutoff;
    public UnitInputPort cutoffRange;
    public UnitInputPort Q;
    public MainCircuit2() {
        add(frequencyScaler = new Multiply());
        add(osc = new SawtoothOscillatorBL());
        add(gatingOsc=new SawtoothOscillator());
        add(lfo=new Add());
        add(ampEnv = new EnvelopeDAHDSR());
        add(filterEnv = new EnvelopeDAHDSR());
        add(lowpass = new FilterLowPass());
        add(cutoffAdder = new Add());
        filterEnv.output.connect(cutoffAdder.inputA);
        gatingOsc.output.connect(lfo.inputA);
        lfo.output.connect(frequencyScaler.inputA);
        frequencyScaler.output.connect(osc.frequency);
        cutoffAdder.output.connect(lowpass.frequency);
        osc.output.connect(lowpass.input);
        lowpass.output.connect(ampEnv.amplitude);
        addPort(amplitude = osc.amplitude, "Amplitude");
        addPort(frequency = lfo.inputB, "Frequency");
        addPort(pitchModulation = frequencyScaler.inputB, "PitchMod");
        addPort(cutoff = cutoffAdder.inputB, "Cutoff");
        addPort(cutoffRange = filterEnv.amplitude, "CutoffRange");
        addPort(Q = lowpass.Q);
        addPort(gatingOscFreq=gatingOsc.frequency,"Modulation Frequency");
        addPort(gatingOscAmp=gatingOsc.amplitude,"Modulation Depth");
        gatingOscFreq.setup(0,0,40);
        gatingOscAmp.setup(0,0,100);
        ampEnv.export(this, "Amp");
        filterEnv.export(this, "Filter");
        frequency.setup(osc.frequency);
        pitchModulation.setup(0.2, 1.0, 4.0);
        cutoff.setup(lowpass.frequency);
        cutoffRange.setup(lowpass.frequency);
                ampEnv.setupAutoDisable(this);
    }
    @Override
    public void noteOn(double freq, double ampl, TimeStamp timeStamp) {
        frequency.set(freq, timeStamp);
        amplitude.set(ampl, timeStamp);
        ampEnv.input.on(timeStamp);
        filterEnv.input.on(timeStamp);
    }
    @Override
    public void noteOff(TimeStamp timeStamp) {
        ampEnv.input.off(timeStamp);
        filterEnv.input.off(timeStamp);
    }
    @Override
    public UnitOutputPort getOutput() {
        return ampEnv.output;
    }
}
