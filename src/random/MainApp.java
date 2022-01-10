package random;

import com.jsyn.JSyn;
import com.jsyn.Synthesizer;
import com.jsyn.swing.JAppletFrame;
import com.jsyn.unitgen.*;
import com.jsyn.util.PolyphonicInstrument;
import project.CustomTweaker;
import project.MainCircuit;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class MainApp extends JApplet {
    private Synthesizer synth;
    private LineOut lineOut;
    private CustomTweaker tweaker;
    private final int MAX_VOICES=6;
    UnitVoice[] voices;
    protected PolyphonicInstrument instrument;
    JCheckBox metronomeBox;
    MainCircuit3 metronome;
    EnvelopeAttackDecay metronomeAmp;
    EnvelopeAttackDecay metronomeModAmp;
    SineOscillator metronomeModOsc;
    SineOscillatorPhaseModulated metronomeBaseOsc;
    SquareOscillator tempoOsc;
    boolean connected;
    int metronomeCount;
    JTextField tempo;
    JButton plusTempo;
    JButton minusTempo;
    JPanel bottom;

    private static final double BPM_TO_HERTZ_FACTOR=0.0166666666666667;

    public void initMetronome(){

        metronomeModAmp.output.connect(metronomeModOsc.amplitude);
        metronomeModOsc.output.connect(metronomeBaseOsc.modulation);
        metronomeBaseOsc.output.connect(metronomeAmp.amplitude);
        metronomeBaseOsc.frequency.setup(0,80,200);
        metronomeModOsc.frequency.setup(0,108,200);
        metronomeModAmp.amplitude.setup(0.001, 1, 10.0);
        metronomeModAmp.attack.set(0.001);
        metronomeModAmp.decay.set(3.2);
        metronomeAmp.attack.set(0.001);
        metronomeAmp.amplitude.set(2);
        metronomeAmp.decay.set(0.17);
        metronomeAmp.input.off();
        metronomeModAmp.input.off();
    }


    public void init()
    {
        setLayout(new GridLayout(0,1));

        bottom=new JPanel(new FlowLayout(FlowLayout.LEFT,50,25));

        synth = JSyn.createSynthesizer();
         connected=false;
         metronomeCount=0;
         tempo=new JTextField("60");
         tempo.setColumns(2);
         minusTempo=new JButton("-");
         plusTempo=new JButton("+");
         minusTempo.addActionListener(new ActionListener() {
             @Override
             public void actionPerformed(ActionEvent e) {
                  int temp=Integer.parseInt(tempo.getText()) - 1;
                  tempo.setText(String.valueOf(temp));
                  tempoOsc.frequency.set(temp*BPM_TO_HERTZ_FACTOR);
             }
         });
        plusTempo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int temp=Integer.parseInt(tempo.getText())+ 1;
                tempo.setText(String.valueOf(temp));
                tempoOsc.frequency.set(temp*BPM_TO_HERTZ_FACTOR);
            }
        });


        synth.add(metronomeBaseOsc = new SineOscillatorPhaseModulated());
        synth.add(metronomeAmp = new EnvelopeAttackDecay());
        synth.add(tempoOsc=new SquareOscillator());
        synth.add(metronomeModAmp = new EnvelopeAttackDecay());
        synth.add(metronomeModOsc = new SineOscillator());
        initMetronome();

        metronomeBox=new JCheckBox("Metronome");
        metronomeBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (metronomeBox.isSelected()) {
                    tempoOsc.frequency.set(Integer.parseInt(tempo.getText())*BPM_TO_HERTZ_FACTOR);
                    if(!connected) {
                        tempoOsc.output.connect(metronomeAmp.input);
                        connected=true;
                    }
                }
                if(!metronomeBox.isSelected()){
                    metronomeAmp.input.off();
                    metronomeAmp.input.disconnectAll();
                    connected=false;
                }

            }
        });

        bottom.add(metronomeBox,BorderLayout.SOUTH);
        bottom.add(plusTempo,BorderLayout.SOUTH);
        bottom.add(minusTempo).setSize(50,50);
        bottom.add(tempo);


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
        metronomeAmp.output.connect(0, lineOut.input, 0);
        metronomeAmp.output.connect(0, lineOut.input, 1);
        tweaker = new CustomTweaker(synth,"stuff" , voice);
        add(tweaker,BorderLayout.NORTH);
        add(bottom);

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
