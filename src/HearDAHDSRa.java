import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
public class HearDAHDSRa extends JApplet {


    private Synthesizer synth;
    private UnitOscillator osc;
    // Use a square wave to trigger the envelope.
    private UnitOscillator tremOsc;
    private EnvelopeDAHDSR dahdsr;
    private LineOut lineOut;
    private JButton button;
    private Add lfo;

    @Override
    public void init() {
        synth = JSyn.createSynthesizer();

        // Add a tone generator.
        synth.add(osc = new TriangleOscillator());
        // Add a trigger.
        synth.add(tremOsc = new SineOscillator());
        synth.add(lfo=new Add());
        button=new JButton("play");
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                noteC2MousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                noteC2MouseReleased(evt);
            }
        });

        // Use an envelope to control the amplitude.
        synth.add(dahdsr = new EnvelopeDAHDSR());
        // Add an output mixer.
        synth.add(lineOut = new LineOut());
        add(button);

        tremOsc.output.connect(lfo.inputA);
        lfo.output.connect(osc.frequency);
        dahdsr.output.connect(osc.amplitude);
        dahdsr.attack.setup(0.001, 0.01, 2.0);
        osc.frequency.setup(50.0, 440.0, 2000.0);
        osc.frequency.setName("Freq");
        tremOsc.frequency.setup(0,0,20);
        osc.output.connect(0, lineOut.input, 0);
        osc.output.connect(0, lineOut.input, 1);




        lfo.inputB.setup(0,0,500);
        tremOsc.frequency.setup(0,0,50);
        tremOsc.amplitude.setup(0,0,100);
        dahdsr.input.off();


        // Arrange the knob in a row.
        setLayout(new GridLayout(1, 0));

        setupPortKnob(osc.frequency);
        setupPortKnob(lfo.inputA);
        setupPortKnob(tremOsc.amplitude);
        setupPortKnob(tremOsc.frequency);

        setupPortKnob(dahdsr.attack);
        setupPortKnob(dahdsr.hold);
        setupPortKnob(dahdsr.decay);
        setupPortKnob(dahdsr.sustain);
        setupPortKnob(dahdsr.release);

        validate();
    }

    private void noteC2MouseReleased(MouseEvent evt) {
        dahdsr.input.off();
    }

    private void noteC2MousePressed(MouseEvent evt) {
        dahdsr.input.on();
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
        HearDAHDSRa applet = new HearDAHDSRa();
        JAppletFrame frame = new JAppletFrame("Hear DAHDSR Envelope", applet);
        frame.setSize(640, 200);
        frame.setVisible(true);
        frame.test();
    }

}