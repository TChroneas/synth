import com.jsyn.JSyn;
import com.jsyn.Synthesizer;
import com.jsyn.apps.InstrumentTester;
import com.jsyn.instruments.SubtractiveSynthVoice;
import com.jsyn.swing.JAppletFrame;
import com.jsyn.swing.SoundTweaker;
import com.jsyn.unitgen.LineOut;
import com.jsyn.unitgen.UnitSource;
import com.jsyn.unitgen.UnitVoice;
import com.jsyn.util.PolyphonicInstrument;

import javax.swing.*;
import java.awt.*;


public class MainApp extends JApplet {
    private Synthesizer synth;
    private LineOut lineOut;
    private CustomTweaker tweaker;
    private final int MAX_VOICES=6;
    UnitVoice[] voices;
    protected PolyphonicInstrument instrument;

    public void init()
    {
        setLayout(new BorderLayout());
        synth = JSyn.createSynthesizer();
        synth.add(lineOut = new LineOut());
        voices=new UnitVoice[MAX_VOICES];
        for(int i=0;i<MAX_VOICES;i++){
            voices[i]=new MainCircuit();
        }

        instrument = new PolyphonicInstrument(voices);
        synth.add(instrument);
        instrument.usePreset(0,synth.createTimeStamp());
        connectVoices(instrument);
    }
    private void connectVoices(UnitSource voice){
        lineOut.input.disconnectAll(0);
        lineOut.input.disconnectAll(1);
        voice.getOutput().connect(0, lineOut.input, 0);
        voice.getOutput().connect(0, lineOut.input, 1);
        tweaker = new CustomTweaker(synth,"stuff" , voice);
        add(tweaker,BorderLayout.NORTH);

    }
    @Override
    public void start() {
        // Start synthesize666r using default stereo output at 44100 Hz.
        synth.start();
        // We only need to start the LineOut. It will pull data from the
        // oscillator.
        lineOut.start();
    }
    public static void main(String[] args) {
        MainApp applet = new MainApp();
        JAppletFrame frame = new JAppletFrame("InstrumentTester", applet);
        frame.setSize(600, 800);
        frame.setVisible(true);
        frame.test();
    }



}
