

import java.awt.GridLayout;

import javax.swing.*;

import com.jsyn.JSyn;
import com.jsyn.Synthesizer;
import com.jsyn.ports.UnitInputPort;
import com.jsyn.swing.*;
import com.jsyn.unitgen.*;

/**
 * Play a tone using a JSyn oscillator and some knobs.
 *
 * @author Phil Burk (C) 2010 Mobileer Inc
 *
 */
public class ModSynth extends JApplet
{
    private static final long serialVersionUID = -2704222221111608377L;
    private Synthesizer synth;
    private FilterLowPass filter;
    private UnitOscillator osc;
    private Add cutoffAdder;
    public UnitInputPort cutoff;
    private LinearRamp lag;
    private LineOut lineOut;

    public void init()
    {
        synth = JSyn.createSynthesizer();

        // Add a tone generator.
        synth.add( osc = new SawtoothOscillatorBL() );
        // Add a lag to smooth out amplitude changes and avoid pops.
        synth.add( lag = new LinearRamp() );
        // Add an output mixer.
        synth.add( lineOut = new LineOut() );
        synth.add(filter=new FilterLowPass());
        synth.add(cutoffAdder=new Add());
        filter.output.connect(osc.amplitude);
        filter.frequency.setup(50,100,4000);
        // Connect the oscillator to the output.
        osc.output.connect( 0, lineOut.input, 0 );

        // Set the minimum, current and maximum values for the port.
        lag.output.connect( osc.amplitude );
        lag.input.setup( 0.0, 0.5, 1.0 );
        lag.time.set(  0.2 );

        // Arrange the faders in a stack.
        setLayout( new GridLayout( 0, 1 ) );

        ExponentialRangeModel amplitudeModel = PortModelFactory.createExponentialModel( lag.input );
        RotaryTextController knob = new RotaryTextController( amplitudeModel, 5 );
        JPanel knobPanel = new JPanel();
        knobPanel.add( knob );
        add( knobPanel );

        osc.frequency.setup( 50.0, 300.0, 10000.0 );
        add( PortControllerFactory.createExponentialPortSlider( osc.frequency ) );
        add( PortControllerFactory.createExponentialPortSlider( filter.frequency ) );
        validate();
    }

    public void start()
    {
        // Start synthesizer using default stereo output at 44100 Hz.
        synth.start();
        // We only need to start the LineOut. It will pull data from the
        // oscillator.
        lineOut.start();
    }

    public void stop()
    {
        synth.stop();
    }


    private void AddPortKnob(UnitInputPort port,double x,double y,double width,double height) {
        DoubleBoundedRangeModel model = PortModelFactory.createExponentialModel(port);
        RotaryTextController knob = new RotaryTextController(model, 10);
        knob.setBorder(BorderFactory.createTitledBorder(port.getName()));
        knob.setTitle(port.getName());
        add(knob);
    }

    /* Can be run as either an application or as an applet. */
    public static void main( String args[] )
    {
        ModSynth applet = new ModSynth();
        JAppletFrame frame = new JAppletFrame( "SawFaders", applet );
        frame.setSize( 440, 200 );
        frame.setVisible( true );
        frame.test();
    }

}
