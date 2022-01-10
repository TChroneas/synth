package random;

import com.jsyn.JSyn;
import com.jsyn.Synthesizer;
import com.jsyn.swing.JAppletFrame;
import com.jsyn.unitgen.*;

import javax.swing.*;

public class Synth extends JApplet {
    public Synthesizer synth;
    public UnitOscillator osc1;
    public UnitOscillator osc2;
    public LinearRamp lag;
    public LineOut lineOut;
    private EnvelopeDAHDSR dahdsr;


    public void init(){
        synth.start();
        synth = JSyn.createSynthesizer();
        synth.add(osc1 = new SineOscillator());
        synth.add(osc2 = new SineOscillator());
        synth.add(lineOut = new LineOut());
        synth.add(dahdsr=new EnvelopeDAHDSR());

        osc1.output.connect(0, lineOut.input, 1);
        osc2.output.connect(0, lineOut.input, 1);


        lineOut.start();
        double time = synth.getCurrentTime();
        for( int i=0; i<2; i++ )
        {

            time += 2.0;
            try {
                synth.sleepUntil( time );
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        lineOut.stop();
         time = synth.getCurrentTime();
        for( int i=0; i<2; i++ )
        {

            time += 2.0;
            try {
                synth.sleepUntil( time );
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        lineOut.start();


    }
    public static void main(String[] args) {
        Synth applet = new Synth();
        JAppletFrame frame = new JAppletFrame("About JSyn", applet);
        frame.setSize(440, 300);
        frame.setVisible(true);
        frame.test();
    }


}
