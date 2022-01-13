package project;


import java.awt.Component;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.logging.Logger;

import javax.swing.*;

import com.jsyn.*;
import com.jsyn.ports.UnitInputPort;
import com.jsyn.ports.UnitPort;
import com.jsyn.swing.PortControllerFactory;
import com.jsyn.unitgen.UnitGenerator;
import com.jsyn.unitgen.UnitSource;
import com.jsyn.unitgen.UnitVoice;
import com.jsyn.util.Instrument;
import com.softsynth.math.AudioMath;

@SuppressWarnings("serial")
public class CustomTweaker extends JPanel {
    private UnitSource source;
    private CustomASCIIKeyboard keyboard;
    private Synthesizer synth;
    private JCheckBox loop;

    public ArrayList<Component> getSliders() {
        return sliders;
    }

    private JTextField beatsToLoop;
    private ArrayList<Component> sliders;

    public JTextField getBeatsToLoop() {
        return beatsToLoop;
    }

    private JButton focusButton;


    public JCheckBox getLoop() {
        return loop;
    }

    static Logger logger = Logger.getLogger(CustomTweaker.class.getName());

    public JButton getFocusButton() {
        return focusButton;
    }

    public CustomTweaker(Synthesizer synth, String title, UnitSource source) {
        this.synth = synth;
        this.source = source;

        setLayout(new GridLayout(0, 2));

        UnitGenerator ugen = source.getUnitGenerator();
         sliders = new ArrayList<Component>();

        add(new JLabel(title));

        if (source instanceof Instrument) {
            add(keyboard = createPolyphonicKeyboard());
            loop=keyboard.getLoopButton();
            focusButton=keyboard.getFocusButton();
            beatsToLoop=keyboard.getBeatsToLoop();
        } else if (source instanceof UnitVoice) {
            add(keyboard = createMonophonicKeyboard());
        }

        // Arrange the faders in a stack.
        // Iterate through the ports.
        for (UnitPort port : ugen.getPorts()) {
            if (port instanceof UnitInputPort) {
                UnitInputPort inputPort = (UnitInputPort) port;
                Component slider;
                // Use an exponential slider if it seems appropriate.
                if ((inputPort.getMinimum() > 0.0)
                        && ((inputPort.getMaximum() / inputPort.getMinimum()) > 4.0)) {
                    slider = PortControllerFactory.createExponentialPortSlider(inputPort);
                } else {
                    slider = PortControllerFactory.createPortSlider(inputPort);

                }
                add(slider);
                sliders.add(slider);
            }
        }

        if (keyboard != null) {
            for (Component slider : sliders) {
                slider.addKeyListener(keyboard.getKeyListener());
            }
        }
        validate();
    }

    @SuppressWarnings("serial")
    private CustomASCIIKeyboard createPolyphonicKeyboard() {
        return new CustomASCIIKeyboard() {
            @Override
            public void keyOff(int pitch) {
                ((Instrument) source).noteOff(pitch, synth.createTimeStamp());
            }

            @Override
            public void keyOn(int pitch) {
                double freq = AudioMath.pitchToFrequency(pitch);
                ((Instrument) source).noteOn(pitch, freq, 0.5, synth.createTimeStamp());
            }
        };
    }

    @SuppressWarnings("serial")
    private CustomASCIIKeyboard createMonophonicKeyboard() {
        return new CustomASCIIKeyboard() {
            @Override
            public void keyOff(int pitch) {
                ((UnitVoice) source).noteOff(synth.createTimeStamp());
            }

            @Override
            public void keyOn(int pitch) {
                double freq = AudioMath.pitchToFrequency(pitch);
                ((UnitVoice) source).noteOn(freq, 0.5, synth.createTimeStamp());
            }
        };
    }

}
