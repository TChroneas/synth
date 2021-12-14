import java.awt.GridLayout;
import java.awt.event.MouseEvent;

import javax.swing.*;

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
public class HearDAHDSRb extends JApplet {


    private Synthesizer synth;
    private UnitOscillator osc;
    // Use a square wave to trigger the envelope.
    private UnitOscillator gatingOsc;
    private EnvelopeDAHDSR dahdsr;
    private Add add;
    private LineOut lineOut;
    private JButton button;

    @Override
    public void init() {
        synth = JSyn.createSynthesizer();

        // Add a tone generator.
        synth.add(osc = new SineOscillator());

        synth.add(gatingOsc = new SquareOscillator());
        synth.add(dahdsr = new EnvelopeDAHDSR());
        synth.add(lineOut = new LineOut());
        synth.add(add=new Add());
        button=new JButton("play");
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                noteC2MousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                noteC2MouseReleased(evt);
            }
        });
        add(button);
        gatingOsc.output.connect(add.inputA);
        add.output.connect(osc.frequency);

        dahdsr.output.connect(osc.amplitude);
        dahdsr.attack.setup(0.001, 0.01, 2.0);
        osc.output.connect(0, lineOut.input, 0);
        osc.output.connect(0, lineOut.input, 1);





        // Arrange the knob in a row.
        setLayout(new GridLayout(1, 0));

        setupPortKnob(dahdsr.attack);
        setupPortKnob(dahdsr.decay);
        setupPortKnob(dahdsr.sustain);
        setupPortKnob(dahdsr.release);
        add.inputB.setup(330,330,500);
        add.inputB.setName("Center Frequency");
        setupPortKnob(add.inputB);
        gatingOsc.frequency.setup(2.0,2.0,50);
        gatingOsc.frequency.setName("Modulation Frequency");
        setupPortKnob(gatingOsc.frequency);

        gatingOsc.amplitude.setup(0.0,0.0,100);
        gatingOsc.amplitude.setName("Modulation Depth");
        setupPortKnob(gatingOsc.amplitude);
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
    private void noteC2MouseReleased(MouseEvent evt) {
        dahdsr.input.off();
    }

    private void noteC2MousePressed(MouseEvent evt) {
        dahdsr.input.on();
    }

    /* Can be run as either an application or as an applet. */
    public static void main(String[] args) {
        HearDAHDSRb applet = new HearDAHDSRb();
        JAppletFrame frame = new JAppletFrame("Hear DAHDSR Envelope", applet);
        frame.setSize(640, 200);
        frame.setVisible(true);
        frame.test();
    }
}