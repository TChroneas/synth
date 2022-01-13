package project;

import com.jsyn.JSyn;
import com.jsyn.Synthesizer;
import com.jsyn.ports.UnitInputPort;
import com.jsyn.ports.UnitPort;
import com.jsyn.swing.DoubleBoundedRangeSlider;
import com.jsyn.swing.JAppletFrame;
import com.jsyn.unitgen.*;
import com.jsyn.util.PolyphonicInstrument;
import com.jsyn.util.WaveRecorder;
import org.apache.poi.hssf.record.formula.functions.Sin;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.Timer;


/** @noinspection ALL*/
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
    JPanel bottomBottom;
    boolean looperCreated;
    JButton focusButton;
    JCheckBox looperRecord;
    File looperFile;
    WaveRecorder recorder;
    JCheckBox loopPlay;
    JTextField beatsToLoop;
    JButton killButton;
    JLabel beats;
    JLabel secBeats;
    Sound loop;
    JButton octUp;
    JButton octDown;
    JRadioButton sineB;
    JRadioButton sawB;
    JRadioButton squareB;
    JRadioButton pulseB;
    JRadioButton impulseB;
    JRadioButton triangleB;
    ButtonGroup bg;
    JPanel bottomBottomBottom;
    BoxLayout boxLayout;

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


    public void init() {
        setLayout(new GridLayout(2, 1));

        bottom = new JPanel(new FlowLayout(FlowLayout.LEFT, 50, 25));
        bottomBottom = new JPanel(new FlowLayout(FlowLayout.LEFT, 50, 25));
        bottomBottomBottom=new JPanel();
        boxLayout = new BoxLayout(bottomBottomBottom, BoxLayout.Y_AXIS);
        bottomBottomBottom.setLayout(boxLayout);
        octUp=new JButton("Octave Up");
        octDown=new JButton("Octave Down");
        bottomBottom.add(octUp);
        bg=new ButtonGroup();
        sineB=new JRadioButton("Sine Oscillator");
        sawB=new JRadioButton("Sawtooth Oscillator");
        squareB=new JRadioButton("Square Oscillator");
        pulseB= new JRadioButton("pulse Oscillator");
        impulseB=new JRadioButton("Impulse Oscillator");
        triangleB=new JRadioButton("Triangle Oscillator");
        bg.add(sineB);
        bg.add(sawB);
        bg.add(squareB);
        bg.add(pulseB);
        bg.add(impulseB);
        bg.add(triangleB);
        sawB.setSelected(true);
        sawB.setEnabled(false);
        bottomBottom.add(octDown);
        bottomBottomBottom.add(sineB);
        bottomBottomBottom.add(sawB);
        bottomBottomBottom.add(squareB);
        bottomBottomBottom.add(pulseB);
        bottomBottomBottom.add(impulseB);
        bottomBottomBottom.add(triangleB);







        octUp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                OctaveUp();
            }
        });
        octDown.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                OctaveDown();
            }
        });
        sawB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                SawtoothOscillatorBL oscs[]=new SawtoothOscillatorBL[6];
                for(int i=0;i<6;i++){
                    oscs[i]=new SawtoothOscillatorBL();
                }
                changeOscillator(oscs);

                sawB.setEnabled(false);
                sineB.setEnabled(true);
                squareB.setEnabled(true);
                pulseB.setEnabled(true);
                impulseB.setEnabled(true);
                triangleB.setEnabled(true);
                focusButton.grabFocus();
            }
        });
        sineB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                SineOscillator oscs[]=new SineOscillator[6];
                for(int i=0;i<6;i++){
                    oscs[i]=new SineOscillator();
                }
                changeOscillator(oscs);

                sineB.setEnabled(false);
                sawB.setEnabled(true);
                squareB.setEnabled(true);
                pulseB.setEnabled(true);
                impulseB.setEnabled(true);
                triangleB.setEnabled(true);
                focusButton.grabFocus();
            }
        });
        squareB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                SquareOscillator oscs[]=new SquareOscillator[6];
                for(int i=0;i<6;i++){
                    oscs[i]=new SquareOscillator();
                }
                changeOscillator(oscs);

                squareB.setEnabled(false);
                sineB.setEnabled(true);
                sawB.setEnabled(true);
                pulseB.setEnabled(true);
                impulseB.setEnabled(true);
                triangleB.setEnabled(true);
                focusButton.grabFocus();
            }
        });
        pulseB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                PulseOscillator oscs[]=new PulseOscillator[6];
                for(int i=0;i<6;i++){
                    oscs[i]=new PulseOscillator();
                }
                changeOscillator(oscs);

                pulseB.setEnabled(false);
                squareB.setEnabled(true);
                sineB.setEnabled(true);
                sawB.setEnabled(true);
                impulseB.setEnabled(true);
                triangleB.setEnabled(true);
                focusButton.grabFocus();
            }
        });
        impulseB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                ImpulseOscillator oscs[]=new ImpulseOscillator[6];

                for(int i=0;i<6;i++){
                    oscs[i]=new ImpulseOscillator();
                }
                changeOscillator(oscs);

                impulseB.setEnabled(false);
                pulseB.setEnabled(true);
                squareB.setEnabled(true);
                sineB.setEnabled(true);
                sawB.setEnabled(true);
                triangleB.setEnabled(true);
                focusButton.grabFocus();
            }
        });
        triangleB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                TriangleOscillator oscs[]=new TriangleOscillator[6];
                for(int i=0;i<6;i++){
                    oscs[i]=new TriangleOscillator();
                }
                changeOscillator(oscs);

                triangleB.setEnabled(false);
                impulseB.setEnabled(true);
                pulseB.setEnabled(true);
                squareB.setEnabled(true);
                sineB.setEnabled(true);
                sawB.setEnabled(true);
                focusButton.grabFocus();

            }
        });





        synth = JSyn.createSynthesizer();
        connected = false;
        metronomeCount = 0;
        looperCreated = false;

        tempo = new JTextField("120");
        tempo.setColumns(2);
        minusTempo = new JButton("-");
        plusTempo = new JButton("+");
        beats = new JLabel("4");
        secBeats = new JLabel("-1");
        beats.setVisible(false);
        killButton = new JButton("kill");
        bottom.add(killButton);
        secBeats.setVisible(false);
        beats.setFont(new Font("Serif", Font.PLAIN, 50));
        secBeats.setFont(new Font("Serif", Font.PLAIN, 50));
        looperRecord = new JCheckBox("looper");
        looperFile = new File("src\\project\\looper1.wav");
        loopPlay = new JCheckBox("play loop");


        minusTempo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int temp = Integer.parseInt(tempo.getText()) - 1;
                tempo.setText(String.valueOf(temp));
                tempoOsc.frequency.set(temp * BPM_TO_HERTZ_FACTOR);
            }
        });
        plusTempo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int temp = Integer.parseInt(tempo.getText()) + 1;
                tempo.setText(String.valueOf(temp));
                tempoOsc.frequency.set(temp * BPM_TO_HERTZ_FACTOR);
            }
        });


        synth.add(metronomeBaseOsc = new SineOscillatorPhaseModulated());
        synth.add(metronomeAmp = new EnvelopeAttackDecay());
        synth.add(tempoOsc = new SquareOscillator());
        synth.add(metronomeModAmp = new EnvelopeAttackDecay());
        synth.add(metronomeModOsc = new SineOscillator());
        initMetronome();


        metronomeBox = new JCheckBox("Metronome");
        metronomeBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (metronomeBox.isSelected()) {
                    tempoOsc.frequency.set(Integer.parseInt(tempo.getText()) * BPM_TO_HERTZ_FACTOR);
                    tempo.setEditable(false);
                    if (!connected) {
                        tempoOsc.output.connect(metronomeAmp.input);
                        connected = true;
                        looperRecord.setEnabled(false);
                    }
                }
                if (!metronomeBox.isSelected()) {
                    metronomeAmp.input.off();
                    tempo.setEditable(true);
                    metronomeAmp.input.disconnectAll();
                    connected = false;
                    looperRecord.setEnabled(true);

                }

            }
        });
        loopPlay.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO: 12/01/2022 FTIAKSE AN DEN IPARXEI WAV NA LOOPAREI TI NA KANW
                if (loopPlay.isSelected()) {
                    loop.play();
                }

                if (!loopPlay.isSelected()) {
                    loop.stop();


                }
            }
        });
        killButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UnitOscillator oscs[]=new UnitOscillator[6];
                oscs[0]=new SquareOscillator();
                oscs[1]=new SquareOscillator();
                oscs[2]=new SquareOscillator();
                oscs[3]=new SquareOscillator();
                oscs[4]=new SquareOscillator();
                oscs[5]=new SquareOscillator();

                changeOscillator(oscs);
            }
        });
        bottom.add(metronomeBox, BorderLayout.SOUTH);
        bottom.add(plusTempo, BorderLayout.SOUTH);
        bottom.add(minusTempo).setSize(50, 50);
        bottom.add(tempo);
        bottom.add(loopPlay);
        bottom.add(beats);
        bottom.add(secBeats);
        synth.add(lineOut = new LineOut());
        voices = new UnitVoice[MAX_VOICES];
        for (int i = 0; i < MAX_VOICES; i++) {
            voices[i] = new MainCircuit(new SawtoothOscillatorBL());
        }
        instrument = new PolyphonicInstrument(voices);
        synth.add(instrument);
        instrument.usePreset(0, synth.createTimeStamp());
        connectVoices(instrument);
        looperRecord = tweaker.getLoop();
        focusButton = tweaker.getFocusButton();
        beatsToLoop = tweaker.getBeatsToLoop();
        beatsToLoop.setText("4");
        looperRecord.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (looperRecord.isSelected()) {

                    int temp = Integer.parseInt(tempo.getText());
                    int beatsL = Integer.parseInt(beatsToLoop.getText());
                    double beatTime = 60000 / temp;
                    double recTime = beatTime * beatsL;
                    double beatTimer = beatTime * 4;
                    long longTimer = (long) beatTimer;
                    long totalTime = (long) beatTimer + (long) beatTime;
                    looperFile = new File("src\\project\\looper1.wav");
                    try {
                        recorder = new WaveRecorder(synth, looperFile);
                    } catch (FileNotFoundException fileNotFoundException) {
                        fileNotFoundException.printStackTrace();
                    }

                    beats.setVisible(true);
                    instrument.getOutput().connect(0, recorder.getInput(), 0);
                    instrument.getOutput().connect(0, recorder.getInput(), 1);

                    focusButton.grabFocus();
                    Timer timer = new Timer();

                    tempoOsc.frequency.set(Integer.parseInt(tempo.getText()) * BPM_TO_HERTZ_FACTOR);
                    tempo.setEditable(false);
                    metronomeBox.setEnabled(false);
                    if (!connected) {
                        tempoOsc.output.connect(metronomeAmp.input);
                        connected = true;
                    }

                    timer.scheduleAtFixedRate(new TimerTask() {
                        @Override
                        public void run() {
                            boolean start = false;
                            if ((!beats.getText().equals("0")) && (!beats.getText().equals("Recording"))) {
                                int currentBeats = Integer.parseInt(beats.getText());
                                currentBeats--;
                                beats.setText(Integer.toString(currentBeats));
                            }
                            if (beats.getText().equals("0")) {
                                beats.setText("Recording");
                                if (!secBeats.isVisible()) {
                                    secBeats.setVisible(true);
                                    recorder.start();
                                }

                            }
                            if ((beats.getText().equals("Recording")) && !secBeats.getText().equals("Recording complete")) {

                                int secBeatstr = Integer.parseInt(secBeats.getText());
                                secBeatstr++;
                                secBeats.setText(Integer.toString(secBeatstr));


                            }

                            if ((secBeats.getText().equals(beatsToLoop.getText())) && (!secBeats.getText().equals("Recording complete"))) {
                                metronomeAmp.input.off();
                                tempo.setEditable(true);
                                metronomeAmp.input.disconnectAll();
                                connected = false;
                                metronomeBox.setEnabled(true);
                                secBeats.setText("Recording complete");

                                if (secBeats.getText().equals("Recording complete")) {
                                    beats.setText("4");
                                    secBeats.setText("-1");
                                    beats.setVisible(false);
                                    secBeats.setVisible(false);
                                    recorder.stop();
                                    timer.cancel();
                                    looperRecord.setSelected(false);
                                    Trimmer trimmer = new Trimmer(looperFile, beatsL, temp);
                                    loop = new Sound("src\\project\\looperFinal.wav", true);
                                }
                            }

                        }
                    }, longTimer / 4, longTimer / 4);
                }

            }
        });
        ArrayList<Component> sliders = tweaker.getSliders();
        validate();
    }



    public void changeOscillator(UnitOscillator []oscs){
        double [] presets=new double[15];
        UnitInputPort port=(UnitInputPort) instrument.getPortByName("Amplitude");
        presets[0]=port.getValue();
        port=(UnitInputPort) instrument.getPortByName("Pitch mod");
        presets[1]=port.getValue();
        port=(UnitInputPort) instrument.getPortByName("Cutoff");
        presets[2]=port.getValue();
        port=(UnitInputPort) instrument.getPortByName("CutoffRange");
        presets[3]=port.getValue();
        port=(UnitInputPort) instrument.getPortByName("Q");
        presets[4]=port.getValue();
        port=(UnitInputPort) instrument.getPortByName("Modulation Frequency");
        presets[5]=port.getValue();
        port=(UnitInputPort) instrument.getPortByName("Modulation Depth");
        presets[6]=port.getValue();
        port=(UnitInputPort) instrument.getPortByName("AmpAttack");
        presets[7]=port.getValue();
        port=(UnitInputPort) instrument.getPortByName("AmpDecay");
        presets[8]=port.getValue();
        port=(UnitInputPort) instrument.getPortByName("AmpSustain");
        presets[9]=port.getValue();
        port=(UnitInputPort) instrument.getPortByName("AmpRelease");
        presets[10]=port.getValue();
        port=(UnitInputPort) instrument.getPortByName("FilterAttack");
        presets[11]=port.getValue();
        port=(UnitInputPort) instrument.getPortByName("FilterDecay");
        presets[12]=port.getValue();
        port=(UnitInputPort) instrument.getPortByName("FilterSustain");
        presets[13]=port.getValue();
        port=(UnitInputPort) instrument.getPortByName("FilterRelease");
        presets[14]=port.getValue();
        remove(tweaker);
        remove(bottom);
        remove(bottomBottom);
        voices=new UnitVoice[MAX_VOICES];
        for(int i=0;i<MAX_VOICES;i++){

            voices[i]=new MainCircuit(oscs[i]);
        }


        instrument = new PolyphonicInstrument(voices);
        synth.add(instrument);
        instrument.getPorts();
        port=(UnitInputPort) instrument.getPortByName("Amplitude");
        port.set(presets[0]);
        port=(UnitInputPort) instrument.getPortByName("Pitch mod");
        port.set(presets[1]);
        port=(UnitInputPort) instrument.getPortByName("Cutoff");
        port.set(presets[2]);
        port=(UnitInputPort) instrument.getPortByName("CutoffRange");
        port.set(presets[3]);
        port=(UnitInputPort) instrument.getPortByName("Q");
        port.set(presets[4]);
        port=(UnitInputPort) instrument.getPortByName("Modulation Frequency");
        port.set(presets[5]);
        port=(UnitInputPort) instrument.getPortByName("Modulation Depth");
        port.set(presets[6]);
        port=(UnitInputPort) instrument.getPortByName("AmpAttack");
        port.set(presets[7]);
        port=(UnitInputPort) instrument.getPortByName("AmpDecay");
        port.set(presets[8]);
        port=(UnitInputPort) instrument.getPortByName("AmpSustain");
        port.set(presets[9]);
        port=(UnitInputPort) instrument.getPortByName("AmpRelease");
        port.set(presets[10]);
        port=(UnitInputPort) instrument.getPortByName("FilterAttack");
        port.set(presets[11]);
        port=(UnitInputPort) instrument.getPortByName("FilterDecay");
        port.set(presets[12]);
        port=(UnitInputPort) instrument.getPortByName("FilterSustain");
        port.set(presets[13]);
        port=(UnitInputPort) instrument.getPortByName("FilterRelease");
        port.set(presets[14]);

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

                    int temp=Integer.parseInt(tempo.getText());
                    int beatsL=Integer.parseInt(beatsToLoop.getText());
                    double beatTime=60000/temp;
                    double recTime=beatTime*beatsL;
                    double beatTimer=beatTime*4;
                    long longTimer=(long)beatTimer;
                    long totalTime=(long)beatTimer+(long)beatTime;
                    looperFile = new File( "src\\project\\looper1.wav" );
                    try {
                        recorder = new WaveRecorder( synth, looperFile );
                    } catch (FileNotFoundException fileNotFoundException) {
                        fileNotFoundException.printStackTrace();
                    }

                    beats.setVisible(true);
                    instrument.getOutput().connect(0, recorder.getInput(), 0);
                    instrument.getOutput().connect(0, recorder.getInput(), 1);

                    focusButton.grabFocus();
                    Timer timer=new Timer();

                    tempoOsc.frequency.set(Integer.parseInt(tempo.getText())*BPM_TO_HERTZ_FACTOR);
                    tempo.setEditable(false);
                    metronomeBox.setEnabled(false);
                    if(!connected) {
                        tempoOsc.output.connect(metronomeAmp.input);
                        connected = true;
                    }

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
                                if(!secBeats.isVisible()){
                                    secBeats.setVisible(true);
                                    recorder.start();
                                }

                            }
                            if((beats.getText().equals("Recording"))&&!secBeats.getText().equals("Recording complete")){

                                int secBeatstr=Integer.parseInt(secBeats.getText());
                                secBeatstr++;
                                secBeats.setText(Integer.toString(secBeatstr));


                            }

                            if((secBeats.getText().equals(beatsToLoop.getText()))&&(!secBeats.getText().equals("Recording complete"))){
                                metronomeAmp.input.off();
                                tempo.setEditable(true);
                                metronomeAmp.input.disconnectAll();
                                connected=false;
                                metronomeBox.setEnabled(true);
                                secBeats.setText("Recording complete");

                                if(secBeats.getText().equals("Recording complete")){
                                    beats.setText("4");
                                    secBeats.setText("-1");
                                    beats.setVisible(false);
                                    secBeats.setVisible(false);
                                    recorder.stop();
                                    timer.cancel();
                                    looperRecord.setSelected(false);
                                    Trimmer trimmer=new Trimmer(looperFile,beatsL,temp);
                                    loop=new Sound("src\\project\\looperFinal.wav",true);
                                }
                            }

                        }
                    },longTimer/4,longTimer/4);
                }

            }
        });
        revalidate();
        repaint();
    }

    public void OctaveUp(){
        double [] presets=new double[15];
        UnitInputPort port=(UnitInputPort) instrument.getPortByName("Amplitude");
        presets[0]=port.getValue();
        port=(UnitInputPort) instrument.getPortByName("Pitch mod");
        presets[1]=port.getValue();
        port=(UnitInputPort) instrument.getPortByName("Cutoff");
        presets[2]=port.getValue();
        port=(UnitInputPort) instrument.getPortByName("CutoffRange");
        presets[3]=port.getValue();
        port=(UnitInputPort) instrument.getPortByName("Q");
        presets[4]=port.getValue();
        port=(UnitInputPort) instrument.getPortByName("Modulation Frequency");
        presets[5]=port.getValue();
        port=(UnitInputPort) instrument.getPortByName("Modulation Depth");
        presets[6]=port.getValue();
        port=(UnitInputPort) instrument.getPortByName("AmpAttack");
        presets[7]=port.getValue();
        port=(UnitInputPort) instrument.getPortByName("AmpDecay");
        presets[8]=port.getValue();
        port=(UnitInputPort) instrument.getPortByName("AmpSustain");
        presets[9]=port.getValue();
        port=(UnitInputPort) instrument.getPortByName("AmpRelease");
        presets[10]=port.getValue();
        port=(UnitInputPort) instrument.getPortByName("FilterAttack");
        presets[11]=port.getValue();
        port=(UnitInputPort) instrument.getPortByName("FilterDecay");
        presets[12]=port.getValue();
        port=(UnitInputPort) instrument.getPortByName("FilterSustain");
        presets[13]=port.getValue();
        port=(UnitInputPort) instrument.getPortByName("FilterRelease");
        presets[14]=port.getValue();
        remove(tweaker);
        remove(bottom);
        remove(bottomBottom);


        MainCircuit current=(MainCircuit)voices[0];
        UnitOscillator currentOsc=current.getOsc();

        UnitOscillator[] oscs=new UnitOscillator[MAX_VOICES];
        if(currentOsc instanceof SineOscillator){
            for(int i=0;i<MAX_VOICES;i++){

                oscs[i]=new SineOscillator();

            }
        }
        if(currentOsc instanceof SawtoothOscillatorBL){
            for(int i=0;i<MAX_VOICES;i++){

                oscs[i]=new SawtoothOscillatorBL();

            }
        }
        if(currentOsc instanceof SquareOscillator){
            for(int i=0;i<MAX_VOICES;i++){

                oscs[i]=new SquareOscillator();

            }
        }
        if(currentOsc instanceof PulseOscillator){
            for(int i=0;i<MAX_VOICES;i++){

                oscs[i]=new PulseOscillator();

            }
        }
        if(currentOsc instanceof ImpulseOscillator){
            for(int i=0;i<MAX_VOICES;i++){

                oscs[i]=new ImpulseOscillator();

            }
        }
        if(currentOsc instanceof TriangleOscillator){
            for(int i=0;i<MAX_VOICES;i++){

                oscs[i]=new ImpulseOscillator();

            }
        }





        voices=new UnitVoice[MAX_VOICES];
        for(int i=0;i<MAX_VOICES;i++){

            voices[i]=new MainCircuit(oscs[i]);

        }


        instrument = new PolyphonicInstrument(voices);
        synth.add(instrument);
        instrument.getPorts();
        port=(UnitInputPort) instrument.getPortByName("Amplitude");
        port.set(presets[0]);
        port=(UnitInputPort) instrument.getPortByName("Pitch mod");
        double pitch=presets[1];
        if(pitch<=4 &&pitch>2){
            port.set(2);
        }
        if(pitch<=2&&pitch>1){
            port.set(1);
        }
        if(pitch<=1&&pitch>0.5){
            port.set(0.5);
        }
        if(pitch<=0.5){
            port.set(0.25);
        }
        if(pitch<0.25){
            port.set(0.25);
            octUp.setEnabled(true);
            octDown.setEnabled(true);
        }
        if(pitch>=0.25&&pitch<0.5){
            port.set(0.5);
            octUp.setEnabled(true);
            octDown.setEnabled(true);
        }
        if(pitch>=0.5&&pitch<1){
            port.set(1);
            octUp.setEnabled(true);
            octDown.setEnabled(true);
        }
        if(pitch>=1 &&pitch<2){
            port.set(2);
            octUp.setEnabled(true);
            octDown.setEnabled(true);
        }
        if(pitch>=2&&pitch<4){
            port.set(4);
            octUp.setEnabled(false);
            octDown.setEnabled(true);
        }
        port=(UnitInputPort) instrument.getPortByName("Cutoff");
        port.set(presets[2]);
        port=(UnitInputPort) instrument.getPortByName("CutoffRange");
        port.set(presets[3]);
        port=(UnitInputPort) instrument.getPortByName("Q");
        port.set(presets[4]);
        port=(UnitInputPort) instrument.getPortByName("Modulation Frequency");
        port.set(presets[5]);
        port=(UnitInputPort) instrument.getPortByName("Modulation Depth");
        port.set(presets[6]);
        port=(UnitInputPort) instrument.getPortByName("AmpAttack");
        port.set(presets[7]);
        port=(UnitInputPort) instrument.getPortByName("AmpDecay");
        port.set(presets[8]);
        port=(UnitInputPort) instrument.getPortByName("AmpSustain");
        port.set(presets[9]);
        port=(UnitInputPort) instrument.getPortByName("AmpRelease");
        port.set(presets[10]);
        port=(UnitInputPort) instrument.getPortByName("FilterAttack");
        port.set(presets[11]);
        port=(UnitInputPort) instrument.getPortByName("FilterDecay");
        port.set(presets[12]);
        port=(UnitInputPort) instrument.getPortByName("FilterSustain");
        port.set(presets[13]);
        port=(UnitInputPort) instrument.getPortByName("FilterRelease");
        port.set(presets[14]);

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

                    int temp=Integer.parseInt(tempo.getText());
                    int beatsL=Integer.parseInt(beatsToLoop.getText());
                    double beatTime=60000/temp;
                    double recTime=beatTime*beatsL;
                    double beatTimer=beatTime*4;
                    long longTimer=(long)beatTimer;
                    long totalTime=(long)beatTimer+(long)beatTime;
                    looperFile = new File( "src\\project\\looper1.wav" );
                    try {
                        recorder = new WaveRecorder( synth, looperFile );
                    } catch (FileNotFoundException fileNotFoundException) {
                        fileNotFoundException.printStackTrace();
                    }

                    beats.setVisible(true);
                    instrument.getOutput().connect(0, recorder.getInput(), 0);
                    instrument.getOutput().connect(0, recorder.getInput(), 1);

                    focusButton.grabFocus();
                    Timer timer=new Timer();

                    tempoOsc.frequency.set(Integer.parseInt(tempo.getText())*BPM_TO_HERTZ_FACTOR);
                    tempo.setEditable(false);
                    metronomeBox.setEnabled(false);
                    if(!connected) {
                        tempoOsc.output.connect(metronomeAmp.input);
                        connected = true;
                    }

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
                                if(!secBeats.isVisible()){
                                    secBeats.setVisible(true);
                                    recorder.start();
                                }

                            }
                            if((beats.getText().equals("Recording"))&&!secBeats.getText().equals("Recording complete")){

                                int secBeatstr=Integer.parseInt(secBeats.getText());
                                secBeatstr++;
                                secBeats.setText(Integer.toString(secBeatstr));


                            }

                            if((secBeats.getText().equals(beatsToLoop.getText()))&&(!secBeats.getText().equals("Recording complete"))){
                                metronomeAmp.input.off();
                                tempo.setEditable(true);
                                metronomeAmp.input.disconnectAll();
                                connected=false;
                                metronomeBox.setEnabled(true);
                                secBeats.setText("Recording complete");

                                if(secBeats.getText().equals("Recording complete")){
                                    beats.setText("4");
                                    secBeats.setText("-1");
                                    beats.setVisible(false);
                                    secBeats.setVisible(false);
                                    recorder.stop();
                                    timer.cancel();
                                    looperRecord.setSelected(false);
                                    Trimmer trimmer=new Trimmer(looperFile,beatsL,temp);
                                    loop=new Sound("src\\project\\looperFinal.wav",true);
                                }
                            }

                        }
                    },longTimer/4,longTimer/4);
                }

            }
        });

        revalidate();
        repaint();


    }

    public void OctaveDown(){
        double [] presets=new double[15];
        UnitInputPort port=(UnitInputPort) instrument.getPortByName("Amplitude");
        presets[0]=port.getValue();
        port=(UnitInputPort) instrument.getPortByName("Pitch mod");
        presets[1]=port.getValue();
        port=(UnitInputPort) instrument.getPortByName("Cutoff");
        presets[2]=port.getValue();
        port=(UnitInputPort) instrument.getPortByName("CutoffRange");
        presets[3]=port.getValue();
        port=(UnitInputPort) instrument.getPortByName("Q");
        presets[4]=port.getValue();
        port=(UnitInputPort) instrument.getPortByName("Modulation Frequency");
        presets[5]=port.getValue();
        port=(UnitInputPort) instrument.getPortByName("Modulation Depth");
        presets[6]=port.getValue();
        port=(UnitInputPort) instrument.getPortByName("AmpAttack");
        presets[7]=port.getValue();
        port=(UnitInputPort) instrument.getPortByName("AmpDecay");
        presets[8]=port.getValue();
        port=(UnitInputPort) instrument.getPortByName("AmpSustain");
        presets[9]=port.getValue();
        port=(UnitInputPort) instrument.getPortByName("AmpRelease");
        presets[10]=port.getValue();
        port=(UnitInputPort) instrument.getPortByName("FilterAttack");
        presets[11]=port.getValue();
        port=(UnitInputPort) instrument.getPortByName("FilterDecay");
        presets[12]=port.getValue();
        port=(UnitInputPort) instrument.getPortByName("FilterSustain");
        presets[13]=port.getValue();
        port=(UnitInputPort) instrument.getPortByName("FilterRelease");
        presets[14]=port.getValue();
        remove(tweaker);
        remove(bottom);
        remove(bottomBottom);


        MainCircuit current=(MainCircuit)voices[0];
        UnitOscillator currentOsc=current.getOsc();

        UnitOscillator[] oscs=new UnitOscillator[MAX_VOICES];
        if(currentOsc instanceof SineOscillator){
            for(int i=0;i<MAX_VOICES;i++){

                oscs[i]=new SineOscillator();

            }
        }
        if(currentOsc instanceof SawtoothOscillatorBL){
            for(int i=0;i<MAX_VOICES;i++){

                oscs[i]=new SawtoothOscillatorBL();

            }
        }
        if(currentOsc instanceof SquareOscillator){
            for(int i=0;i<MAX_VOICES;i++){

                oscs[i]=new SquareOscillator();

            }
        }
        if(currentOsc instanceof PulseOscillator){
            for(int i=0;i<MAX_VOICES;i++){

                oscs[i]=new PulseOscillator();

            }
        }
        if(currentOsc instanceof ImpulseOscillator){
            for(int i=0;i<MAX_VOICES;i++){

                oscs[i]=new ImpulseOscillator();

            }
        }
        if(currentOsc instanceof TriangleOscillator){
            for(int i=0;i<MAX_VOICES;i++){

                oscs[i]=new ImpulseOscillator();

            }
        }





        voices=new UnitVoice[MAX_VOICES];
        for(int i=0;i<MAX_VOICES;i++){

            voices[i]=new MainCircuit(oscs[i]);

        }


        instrument = new PolyphonicInstrument(voices);
        synth.add(instrument);
        instrument.getPorts();
        port=(UnitInputPort) instrument.getPortByName("Amplitude");
        port.set(presets[0]);
        port=(UnitInputPort) instrument.getPortByName("Pitch mod");
        double pitch=presets[1];
        if(pitch<=4 &&pitch>2){
            port.set(2);
            octDown.setEnabled(true);
            octUp.setEnabled(true);
        }
        if(pitch<=2&&pitch>1){
            port.set(1);
            octDown.setEnabled(true);
            octUp.setEnabled(true);
        }
        if(pitch<=1&&pitch>0.5){
            port.set(0.5);
            octDown.setEnabled(true);
            octUp.setEnabled(true);
        }
        if(pitch<=0.5){
            port.set(0.25);
            octDown.setEnabled(false);
            octUp.setEnabled(true);
        }

        port=(UnitInputPort) instrument.getPortByName("Cutoff");
        port.set(presets[2]);
        port=(UnitInputPort) instrument.getPortByName("CutoffRange");
        port.set(presets[3]);
        port=(UnitInputPort) instrument.getPortByName("Q");
        port.set(presets[4]);
        port=(UnitInputPort) instrument.getPortByName("Modulation Frequency");
        port.set(presets[5]);
        port=(UnitInputPort) instrument.getPortByName("Modulation Depth");
        port.set(presets[6]);
        port=(UnitInputPort) instrument.getPortByName("AmpAttack");
        port.set(presets[7]);
        port=(UnitInputPort) instrument.getPortByName("AmpDecay");
        port.set(presets[8]);
        port=(UnitInputPort) instrument.getPortByName("AmpSustain");
        port.set(presets[9]);
        port=(UnitInputPort) instrument.getPortByName("AmpRelease");
        port.set(presets[10]);
        port=(UnitInputPort) instrument.getPortByName("FilterAttack");
        port.set(presets[11]);
        port=(UnitInputPort) instrument.getPortByName("FilterDecay");
        port.set(presets[12]);
        port=(UnitInputPort) instrument.getPortByName("FilterSustain");
        port.set(presets[13]);
        port=(UnitInputPort) instrument.getPortByName("FilterRelease");
        port.set(presets[14]);

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

                    int temp=Integer.parseInt(tempo.getText());
                    int beatsL=Integer.parseInt(beatsToLoop.getText());
                    double beatTime=60000/temp;
                    double recTime=beatTime*beatsL;
                    double beatTimer=beatTime*4;
                    long longTimer=(long)beatTimer;
                    long totalTime=(long)beatTimer+(long)beatTime;
                    looperFile = new File( "src\\project\\looper1.wav" );
                    try {
                        recorder = new WaveRecorder( synth, looperFile );
                    } catch (FileNotFoundException fileNotFoundException) {
                        fileNotFoundException.printStackTrace();
                    }

                    beats.setVisible(true);
                    instrument.getOutput().connect(0, recorder.getInput(), 0);
                    instrument.getOutput().connect(0, recorder.getInput(), 1);

                    focusButton.grabFocus();
                    Timer timer=new Timer();

                    tempoOsc.frequency.set(Integer.parseInt(tempo.getText())*BPM_TO_HERTZ_FACTOR);
                    tempo.setEditable(false);
                    metronomeBox.setEnabled(false);
                    if(!connected) {
                        tempoOsc.output.connect(metronomeAmp.input);
                        connected = true;
                    }

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
                                if(!secBeats.isVisible()){
                                    secBeats.setVisible(true);
                                    recorder.start();
                                }

                            }
                            if((beats.getText().equals("Recording"))&&!secBeats.getText().equals("Recording complete")){

                                int secBeatstr=Integer.parseInt(secBeats.getText());
                                secBeatstr++;
                                secBeats.setText(Integer.toString(secBeatstr));


                            }

                            if((secBeats.getText().equals(beatsToLoop.getText()))&&(!secBeats.getText().equals("Recording complete"))){
                                metronomeAmp.input.off();
                                tempo.setEditable(true);
                                metronomeAmp.input.disconnectAll();
                                connected=false;
                                metronomeBox.setEnabled(true);
                                secBeats.setText("Recording complete");

                                if(secBeats.getText().equals("Recording complete")){
                                    beats.setText("4");
                                    secBeats.setText("-1");
                                    beats.setVisible(false);
                                    secBeats.setVisible(false);
                                    recorder.stop();
                                    timer.cancel();
                                    looperRecord.setSelected(false);
                                    Trimmer trimmer=new Trimmer(looperFile,beatsL,temp);
                                    loop=new Sound("src\\project\\looperFinal.wav",true);
                                }
                            }

                        }
                    },longTimer/4,longTimer/4);
                }

            }
        });

        revalidate();
        repaint();


    }

    private void connectVoices(UnitSource voice){
        lineOut.input.disconnectAll(0);
        lineOut.input.disconnectAll(1);
        voice.getOutput().connect(0, lineOut.input, 0);
        voice.getOutput().connect(0, lineOut.input, 1);
        metronomeAmp.output.connect(0, lineOut.input, 0);
        metronomeAmp.output.connect(0, lineOut.input, 1);
        tweaker = new CustomTweaker(synth,"stuff" , voice);
        add(tweaker);
        add(bottom);
        add(bottomBottom);
        add(bottomBottomBottom);


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
