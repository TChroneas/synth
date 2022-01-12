package project;

import com.jsyn.ports.UnitInputPort;
import com.jsyn.ports.UnitOutputPort;
import com.jsyn.unitgen.*;
import com.softsynth.shared.time.TimeStamp;
public class MainCircuit extends Circuit implements UnitVoice {
    private UnitOscillator osc;
    private UnitOscillator gatingOsc;
    private FilterLowPass lowpass;
    private EnvelopeDAHDSR dahdsr;
    private EnvelopeDAHDSR dahdsrLowPass;
    private Add cutoffAdd;
    private Add lfo;
    private Multiply pitchMod;
    public UnitInputPort gatingOscAmp;
    public UnitInputPort gatingOscFreq;
    public UnitInputPort amplitude;
    public UnitInputPort frequency;
    public UnitInputPort pitchModulation;
    public UnitInputPort cutoff;
    public UnitInputPort cutoffRange;
    public UnitInputPort Q;

    public MainCircuit() {
        add(pitchMod=new Multiply());
        add(osc=new SawtoothOscillatorBL());
        add(gatingOsc=new SawtoothOscillator());
        add(lfo=new Add());
        add(dahdsr=new EnvelopeDAHDSR());
        add(dahdsrLowPass=new EnvelopeDAHDSR());
        add(lowpass=new FilterLowPass());
        add(cutoffAdd=new Add());
        dahdsrLowPass.output.connect(cutoffAdd.inputA);
        gatingOsc.output.connect(lfo.inputA);
        lfo.output.connect(pitchMod.inputA);
        pitchMod.output.connect(osc.frequency);
        cutoffAdd.output.connect(lowpass.frequency);
        osc.output.connect(lowpass.input);
        lowpass.output.connect(dahdsr.amplitude);
        addPort(amplitude=osc.amplitude,"Amplitude");
        addPort(frequency=lfo.inputB,"Frequency");
        addPort(pitchModulation=pitchMod.inputB,"Pitch mod");
        addPort(cutoff=cutoffAdd.inputB,"Cutoff");
        addPort(cutoffRange=dahdsrLowPass.amplitude,"CutoffRange");
        addPort(Q=lowpass.Q);
        addPort(gatingOscFreq=gatingOsc.frequency,"Modulation Frequecy");
        addPort(gatingOscAmp=gatingOsc.amplitude,"Modulation Depth");
        gatingOscFreq.setup(0,0,40);
        gatingOscAmp.setup(0,0,100);
        dahdsr.export(this, "Amp");
        dahdsrLowPass.export(this, "Filter");
        frequency.setup(osc.frequency);
        pitchModulation.setup(0.2, 1.0, 4.0);
        cutoff.setup(lowpass.frequency);
        cutoffRange.setup(lowpass.frequency);
        dahdsr.setupAutoDisable(this);
    }
    @Override
    public void noteOn(double v, double v1, TimeStamp timeStamp) {
        frequency.set(v, timeStamp);
        amplitude.set(v1, timeStamp);
        dahdsr.input.on();
        dahdsrLowPass.input.on();
    }
    @Override
    public void noteOff(TimeStamp timeStamp) {
        dahdsr.input.off(timeStamp);
        dahdsrLowPass.input.off(timeStamp );
    }
    @Override
    public UnitOutputPort getOutput() {
        return dahdsr.output;
    }
}
