import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JApplet;

import com.jsyn.JSyn;
import com.jsyn.Synthesizer;
import com.jsyn.ports.UnitInputPort;
import com.jsyn.swing.DoubleBoundedRangeModel;
import com.jsyn.swing.JAppletFrame;
import com.jsyn.swing.PortModelFactory;
import com.jsyn.swing.RotaryTextController;
import com.jsyn.unitgen.*;

/**
 * Play a tone using a JSyn oscillator.
 * Modulate the amplitude using a DAHDSR envelope.
 *
 * @author Phil Burk (C) 2010 Mobileer Inc
 */
public class HearDAHDSR extends JApplet {


    private Synthesizer synth;
    private UnitOscillator osc;
    private UnitOscillator gatingOsc;
    private EnvelopeDAHDSR dahdsr;
    private LineOut lineOut;
    private Add add;


    @Override
    public void init() {
        synth = JSyn.createSynthesizer();
        synth.add(osc=new TriangleOscillator());
        synth.add(gatingOsc=new SineOscillator());
        synth.add(lineOut=new LineOut());
        synth.add(add=new Add());
        //demo
        synth.add(dahdsr=new EnvelopeDAHDSR());
        dahdsr.output.connect(osc.amplitude);
        dahdsr.attack.setup(0.0,0.0,8);
        dahdsr.attack.setName("Amp Attack");
        dahdsr.decay.setName("Amp Decay");
        dahdsr.sustain.setName("Amp sustain");
        dahdsr.release.setName("Amp release");



        gatingOsc.output.connect(add.inputA);
        add.output.connect(osc.frequency);
        osc.output.connect(0,lineOut.input,0);
        osc.output.connect(0,lineOut.input,1);
        setLayout(new GridLayout(1, 0));
        add.inputB.setup(330,330,500);
        add.inputB.setName("Center Frequency");
        setupPortKnob(add.inputB);
        gatingOsc.frequency.setup(2.0,2.0,50);
        gatingOsc.frequency.setName("Modulation Frequency");
        setupPortKnob(gatingOsc.frequency);

        gatingOsc.amplitude.setup(0.0,0.0,100);
        gatingOsc.amplitude.setName("Modulation Depth");
        osc.amplitude.set(10);

        setupPortKnob(gatingOsc.amplitude);
        setupPortKnob(dahdsr.attack);
        setupPortKnob(dahdsr.decay);
        setupPortKnob(dahdsr.sustain);
        setupPortKnob(dahdsr.release);

        //STOP HERE









        validate();










    }
    private void setupPortKnob(UnitInputPort port) {
        DoubleBoundedRangeModel model = PortModelFactory.createExponentialModel(port);
        RotaryTextController knob = new RotaryTextController(model, 10);
        knob.setBorder(BorderFactory.createTitledBorder(port.getName()));
        knob.setTitle(port.getName());
        add(knob);
    }

    @Override
    public void start() {
        // Start synthesizer using default stereo output at 44100 Hz.
        synth.start();
        // We only need to start the LineOut. It will pull data from the
        // oscillator.
        lineOut.start();
    }

    @Override
    public void stop() {
        synth.stop();
    }

    /* Can be run as either an application or as an applet. */
    public static void main(String[] args) {
        HearDAHDSR applet = new HearDAHDSR();
        JAppletFrame frame = new JAppletFrame("Hear DAHDSR Envelope", applet);
        frame.setSize(640, 200);
        frame.setVisible(true);
        frame.test();
    }

}