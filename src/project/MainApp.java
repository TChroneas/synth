package project;

import com.jsyn.JSyn;
import com.jsyn.Synthesizer;
import com.jsyn.swing.JAppletFrame;
import com.jsyn.unitgen.*;
import com.jsyn.util.PolyphonicInstrument;
import com.jsyn.util.WaveRecorder;
import it.sauronsoftware.jave.EncoderException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;


public class MainApp extends JApplet {
    private Synthesizer synth;
    private LineOut lineOut;
    private CustomTweaker tweaker;
    private final int MAX_VOICES=6;
    UnitVoice[] voices;
    protected PolyphonicInstrument instrument;
    JCheckBox metronomeBox;
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
    boolean looperCreated;
    JButton focusButton;
    JCheckBox looperRecord;
    File looperFile;
    WaveRecorder recorder;
    JCheckBox loopPlay;
    JTextField beatsToLoop;
    Looper looper;
    JLabel beats;
    JLabel secBeats;
    WavToMp3 wavToMp3;


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
         looperCreated=false;

          wavToMp3=new WavToMp3();
         tempo=new JTextField("60");
         tempo.setColumns(2);
         minusTempo=new JButton("-");
         plusTempo=new JButton("+");
         beats=new JLabel("4");
         secBeats=new JLabel("0");
         beats.setVisible(false);
         secBeats.setVisible(false);
         beats.setFont(new Font("Serif", Font.PLAIN, 50));
         secBeats.setFont(new Font("Serif", Font.PLAIN, 50));
         looperRecord=new JCheckBox("looper");
         looperFile = new File( "src\\project\\looper1.wav" );
         loopPlay=new JCheckBox("play loop");



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
                    tempo.setEditable(false);
                    if(!connected) {
                        tempoOsc.output.connect(metronomeAmp.input);
                        connected=true;
                    }
                }
                if(!metronomeBox.isSelected()){
                    metronomeAmp.input.off();
                    tempo.setEditable(true);
                    metronomeAmp.input.disconnectAll();
                    connected=false;
                }

            }
        });

        loopPlay.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(loopPlay.isSelected()){
                    if(!looperCreated){
                        looperCreated=true;
                        looper=new Looper();
                    }
                    looper.run();
                }

                if(!loopPlay.isSelected()){
                    looper.getPlayer().stop();

                }
            }
        });



        bottom.add(metronomeBox,BorderLayout.SOUTH);
        bottom.add(plusTempo,BorderLayout.SOUTH);
        bottom.add(minusTempo).setSize(50,50);
        bottom.add(tempo);
        bottom.add(loopPlay);
        bottom.add(beats);
        bottom.add(secBeats);




        synth.add(lineOut = new LineOut());
        voices=new UnitVoice[MAX_VOICES];
        for(int i=0;i<MAX_VOICES;i++){
            voices[i]=new MainCircuit();
        }

        instrument = new PolyphonicInstrument(voices);
        synth.add(instrument);
        instrument.usePreset(0,synth.createTimeStamp());
        connectVoices(instrument);
        looperRecord=tweaker.getLoop();
        focusButton=tweaker.getFocusButton();
        beatsToLoop=tweaker.getBeatsToLoop();
        beatsToLoop.setText("4");

        looperRecord.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(looperRecord.isSelected())
                {
                    try {
                        recorder = new WaveRecorder( synth, looperFile );
                    } catch (FileNotFoundException fileNotFoundException) {
                        fileNotFoundException.printStackTrace();
                    }
                    int temp=Integer.parseInt(tempo.getText());
                    int beatsL=Integer.parseInt(beatsToLoop.getText());
                    double beatTime=(double)60/temp;
                    double recTime=beatTime*beatsL;
                    double beatTimer=beatTime*4000;
                    long longTimer=(long)beatTimer;
                    long totalTime=(long)beatTimer+(long)beatTime;

                    recorder.setMaxRecordingTime(recTime);
                    beats.setVisible(true);
                    instrument.getOutput().connect(0,recorder.getInput(),0);
                    instrument.getOutput().connect(0,recorder.getInput(),1);
                    focusButton.grabFocus();
                    Timer timer=new Timer();
                    timer.scheduleAtFixedRate(new TimerTask() {
                        @Override
                        public void run() {
                            boolean start=false;
                            if((!beats.getText().equals("0"))&&(!beats.getText().equals("Recording"))) {
                                int currentBeats = Integer.parseInt(beats.getText());
                                currentBeats--;
                                beats.setText(Integer.toString(currentBeats));
                            }
                            if(beats.getText().equals("0")){
                                beats.setText("Recording");
                                if(!start) {
                                    recorder.start();
                                }
                                if(!secBeats.isVisible()){
                                    secBeats.setVisible(true);
                                }
                            }
                            if(beats.getText().equals("Recording")){

                                int secBeatstr=Integer.parseInt(secBeats.getText());
                                secBeatstr++;
                                secBeats.setText(Integer.toString(secBeatstr));


                            }
                        }
                    },longTimer/4,longTimer/4);








                }
                if(!looperRecord.isSelected()){
                    recorder.stop();


                    try {
                            wavToMp3.convert(looperFile);
                        } catch (EncoderException encoderException) {
                            encoderException.printStackTrace();
                    }

                }


            }
        });
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