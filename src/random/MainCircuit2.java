package random;

import com.jsyn.ports.UnitInputPort;
import com.jsyn.ports.UnitOutputPort;
import com.jsyn.unitgen.*;
import com.softsynth.shared.time.TimeStamp;
public class MainCircuit2 extends Circuit implements UnitVoice {
    EnvelopeAttackDecay ampEnv;
    SineOscillatorPhaseModulated carrierOsc;
    EnvelopeAttackDecay modEnv;
    SineOscillator modOsc;
    PassThrough freqDistributor;
    Add modSummer;
    Multiply frequencyMultiplier;

    public UnitInputPort mcratio;
    public UnitInputPort index;
    public UnitInputPort modRange;
    public UnitInputPort frequency;
    public MainCircuit2() {
        add(carrierOsc = new SineOscillatorPhaseModulated());
        add(freqDistributor = new PassThrough());
        add(modSummer = new Add());
        add(ampEnv = new EnvelopeAttackDecay());
        add(modEnv = new EnvelopeAttackDecay());
        add(modOsc = new SineOscillator());
        add(frequencyMultiplier = new Multiply());

        addPort(mcratio = frequencyMultiplier.inputB, "MCRatio");
        addPort(index = modSummer.inputA, "Index");
        addPort(modRange = modEnv.amplitude, "ModRange");
        addPort(frequency = freqDistributor.input, "Frequency");

        ampEnv.export(this, "Amp");
        modEnv.export(this, "Mod");

        freqDistributor.output.connect(carrierOsc.frequency);
        freqDistributor.output.connect(frequencyMultiplier.inputA);

        carrierOsc.output.connect(ampEnv.amplitude);
        modEnv.output.connect(modSummer.inputB);
        modSummer.output.connect(modOsc.amplitude);
        modOsc.output.connect(carrierOsc.modulation);
        frequencyMultiplier.output.connect(modOsc.frequency);
        mcratio.setup(0.001, 0.6875, 20.0);
        ampEnv.attack.setup(0.001, 0.005, 8.0);
        modEnv.attack.setup(0.001, 0.005, 8.0);
        ampEnv.decay.setup(0.001, 0.293, 8.0);
        modEnv.decay.setup(0.001, 0.07, 8.0);
        frequency.setup(0.0, 349.0, 3000.0);
        index.setup(0.001, 0.05, 10.0);
        modRange.setup(0.001, 0.4, 10.0);





    }
    @Override
    public void noteOn(double freq, double ampl, TimeStamp timeStamp) {
        carrierOsc.amplitude.set(ampl, timeStamp);
        ampEnv.input.on(timeStamp);
        modEnv.input.trigger(timeStamp);


    }
    @Override
    public void noteOff(TimeStamp timeStamp) {



    }
    @Override
    public UnitOutputPort getOutput() {
        return ampEnv.output;
    }
}
