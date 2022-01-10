package random;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.jsyn.*;
import com.jsyn.ports.UnitInputPort;
import com.jsyn.swing.DoubleBoundedRangeModel;
import com.jsyn.swing.PortModelFactory;
import com.jsyn.swing.RotaryTextController;
import com.jsyn.unitgen.*;

import javax.swing.*;

public class Piano extends javax.swing.JFrame {
    private Synthesizer synth;
    private UnitOscillator osc;
    private LinearRamp lag;
    private LineOut lineOut;
    private EnvelopeDAHDSR dahdsr;


    private void setFrequency(double frequency){
        osc.frequency.set(frequency);

    }
    public Piano() {
        initComponents();
    }

    private void AddPortKnob(UnitInputPort port,int x,int y,int width,int height) {
        DoubleBoundedRangeModel model = PortModelFactory.createExponentialModel(port);
        RotaryTextController knob = new RotaryTextController(model, 10);
        knob.setBorder(BorderFactory.createTitledBorder(port.getName()));
        knob.setTitle(port.getName());
        jPanel1.add(knob).setBounds(x,y,width,height);
    }



    private void initComponents() {
        jPanel1 = new javax.swing.JPanel();
        synth = JSyn.createSynthesizer();
        synth.start();
        synth.add(osc=new SineOscillator());
        synth.add(lineOut = new LineOut());
        synth.add(dahdsr=new EnvelopeDAHDSR());
        dahdsr.output.connect(osc.amplitude);


        dahdsr.input.off();
        dahdsr.output.connect(osc.amplitude);

        dahdsr.attack.setup(0.001, 0.01, 2.0);

        osc.output.connect(0, lineOut.input, 0);
        osc.output.connect(0, lineOut.input, 1);
        dahdsr.input.off();
        lineOut.start();
        AddPortKnob(dahdsr.attack,600,600,90,90);
        AddPortKnob(dahdsr.decay,700,600,90,90);
        AddPortKnob(dahdsr.sustain,800,600,90 ,90);
        AddPortKnob(dahdsr.release,900,600,90,90);





        noteG1B = new javax.swing.JButton();
        noteD1B = new javax.swing.JButton();
        noteF1 = new javax.swing.JButton();
        noteC1 = new javax.swing.JButton();
        noteE1B = new javax.swing.JButton();
        noteD1 = new javax.swing.JButton();
        noteE1 = new javax.swing.JButton();
        noteA1B = new javax.swing.JButton();

        noteG1 = new javax.swing.JButton();
        noteB1B = new javax.swing.JButton();
        noteA1 = new javax.swing.JButton();
        noteB1 = new javax.swing.JButton();
        noteD2B = new javax.swing.JButton();
        noteC2 = new javax.swing.JButton();
        noteE2B = new javax.swing.JButton();
        noteD2 = new javax.swing.JButton();
        noteG2B = new javax.swing.JButton();
        noteF2 = new javax.swing.JButton();
        noteE2 = new javax.swing.JButton();
        noteA2B = new javax.swing.JButton();
        noteB2B = new javax.swing.JButton();
        noteA2 = new javax.swing.JButton();
        noteG2 = new javax.swing.JButton();
        noteD3B = new javax.swing.JButton();
        noteC3 = new javax.swing.JButton();
        noteB2 = new javax.swing.JButton();
        noteE3B = new javax.swing.JButton();
        noteD3 = new javax.swing.JButton();
        noteG3B = new javax.swing.JButton();
        noteF3 = new javax.swing.JButton();
        noteE3 = new javax.swing.JButton();
        noteA3B = new javax.swing.JButton();
        noteB3B = new javax.swing.JButton();
        noteA3 = new javax.swing.JButton();
        noteG3 = new javax.swing.JButton();
        noteB3 = new javax.swing.JButton();
        noteD4B = new javax.swing.JButton();
        noteC4 = new javax.swing.JButton();
        noteE4B = new javax.swing.JButton();
        noteD4 = new javax.swing.JButton();
        noteE4 = new javax.swing.JButton();
        noteG4B = new javax.swing.JButton();
        noteA4B = new javax.swing.JButton();
        noteG4 = new javax.swing.JButton();
        noteF4 = new javax.swing.JButton();
        noteB4B = new javax.swing.JButton();
        noteA4 = new javax.swing.JButton();
        noteB4 = new javax.swing.JButton();
        noteC5 = new javax.swing.JButton();
        sineBox= new JCheckBox("sine wave");
        squareBox=new JCheckBox("square wave");
        sawBox=new JCheckBox("sawtooth wave");
        triBox=new JCheckBox("triangle wave");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new javax.swing.OverlayLayout(getContentPane()));

        jPanel1.setBackground(new java.awt.Color(153, 153, 153));
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel1.setLayout(null);
        sineBox.setBounds(40,90,120,40);
        sawBox.setBounds(40,130,120,40);
        squareBox.setBounds(160,90,120,40);
        triBox.setBounds(160,130,120,40);
        sineBox.setSelected(true);
        jPanel1.add(sineBox);
        jPanel1.add(sawBox);
        jPanel1.add(triBox);
        jPanel1.add(squareBox);
        squareBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(squareBox.isSelected())
                {
                    sawBox.setSelected(false);
                    sineBox.setSelected(false);
                    triBox.setSelected(false);
                    squareBox.setSelected(true);
                    lineOut.input.disconnect(0,osc.output,0);
                    lineOut.input.disconnect(1,osc.output,0);
                    synth.remove(osc);

                    synth.add(osc = new SquareOscillator());

                    osc.output.connect(0, lineOut.input, 0);
                    osc.output.connect(0, lineOut.input, 1);



                }
                if(!squareBox.isSelected())
                {
                    squareBox.setSelected(true);
                }


            }
        });
        triBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(triBox.isSelected())
                {
                    sawBox.setSelected(false);
                    sineBox.setSelected(false);
                    triBox.setSelected(true);
                    squareBox.setSelected(false);
                    lineOut.input.disconnect(0,osc.output,0);
                    lineOut.input.disconnect(1,osc.output,0);
                    synth.remove(osc);

                    synth.add(osc = new TriangleOscillator());

                    osc.output.connect(0, lineOut.input, 0);
                    osc.output.connect(0, lineOut.input, 1);


                }
                if(!triBox.isSelected())
                {
                    triBox.setSelected(true);
                }


            }
        });
        sawBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(sawBox.isSelected())
                {
                    sawBox.setSelected(true);
                    sineBox.setSelected(false);
                    triBox.setSelected(false);
                    squareBox.setSelected(false);
                    lineOut.input.disconnect(0,osc.output,0);
                    lineOut.input.disconnect(1,osc.output,0);
                    synth.remove(osc);

                    synth.add(osc = new SawtoothOscillator());

                    osc.output.connect(0, lineOut.input, 0);
                    osc.output.connect(0, lineOut.input, 1);


                }
                if(!sawBox.isSelected())
                {
                    sawBox.setSelected(true);
                }


            }
        });

        sineBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(sineBox.isSelected())
                {
                    sineBox.setSelected(true);
                    sawBox.setSelected(false);
                    triBox.setSelected(false);
                    squareBox.setSelected(false);
                    lineOut.input.disconnect(0,osc.output,0);
                    lineOut.input.disconnect(1,osc.output,0);
                    synth.remove(osc);

                    synth.add(osc = new SineOscillator());

                    osc.output.connect(0, lineOut.input, 0);
                    osc.output.connect(0, lineOut.input, 1);

                }
                if(!sineBox.isSelected())
                {
                    sineBox.setSelected(true);

                }



            }
        });
        noteG1B.setBackground(new java.awt.Color(0, 0, 0));
        noteG1B.setText("G1B#");
        noteG1B.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                noteG1BMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                noteG1BMouseReleased(evt);
            }
        });
        jPanel1.add(noteG1B);
        noteG1B.setBounds(244, 190, 40, 165);

        noteD1B.setBackground(new java.awt.Color(0, 0, 0));
        noteD1B.setFont(new java.awt.Font("Dialog", 0, 5)); // NOI18N
        noteD1B.setText("D1B#");
        noteD1B.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                noteD1BMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                noteD1BMouseReleased(evt);
            }
        });
        jPanel1.add(noteD1B);
        noteD1B.setBounds(76, 190, 40, 165);

        noteF1.setBackground(new java.awt.Color(255, 255, 255));
        noteF1.setForeground(new java.awt.Color(0, 0, 0));
        noteF1.setText("F1");
        noteF1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                noteF1MousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                noteF1MouseReleased(evt);
            }
        });
        jPanel1.add(noteF1);
        noteF1.setBounds(208, 190, 56, 330);

        noteC1.setBackground(new java.awt.Color(255, 255, 255));
        noteC1.setForeground(new java.awt.Color(0, 0, 0));
        noteC1.setText("C1");
        noteC1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                noteC1MousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                noteC1MouseReleased(evt);
            }
        });
        jPanel1.add(noteC1);
        noteC1.setBounds(40, 190, 56, 330);

        noteE1B.setBackground(new java.awt.Color(0, 0, 0));
        noteE1B.setText("E1B");
        noteE1B.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                noteE1BMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                noteE1BMouseReleased(evt);
            }
        });
        jPanel1.add(noteE1B);
        noteE1B.setBounds(132, 190, 40, 165);

        noteD1.setBackground(new java.awt.Color(255, 255, 255));
        noteD1.setForeground(new java.awt.Color(0, 0, 0));
        noteD1.setText("D1");
        noteD1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                noteD1MousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                noteD1MouseReleased(evt);
            }
        });
        jPanel1.add(noteD1);
        noteD1.setBounds(96, 190, 56, 330);

        noteE1.setBackground(new java.awt.Color(255, 255, 255));
        noteE1.setForeground(new java.awt.Color(0, 0, 0));
        noteE1.setText("E1");
        noteE1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                noteE1MousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                noteE1MouseReleased(evt);
            }
        });
        jPanel1.add(noteE1);
        noteE1.setBounds(152, 190, 56, 330);

        noteA1B.setBackground(new java.awt.Color(0, 0, 0));
        noteA1B.setText("A1B");
        noteA1B.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                noteA1BMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                noteA1BMouseReleased(evt);
            }
        });
        jPanel1.add(noteA1B);
        noteA1B.setBounds(300, 190, 40, 165);

        noteG1.setBackground(new java.awt.Color(255, 255, 255));
        noteG1.setForeground(new java.awt.Color(0, 0, 0));
        noteG1.setText("G1");
        noteG1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                noteG1MousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                noteG1MouseReleased(evt);
            }
        });
        jPanel1.add(noteG1);
        noteG1.setBounds(264, 190, 56, 330);

        noteB1B.setBackground(new java.awt.Color(0, 0, 0));
        noteB1B.setText("B1B");
        noteB1B.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                noteB1BMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                noteB1BMouseReleased(evt);
            }
        });
        noteB1B.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                noteB1BMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                noteB1BMouseReleased(evt);
            }
        });
        jPanel1.add(noteB1B);
        noteB1B.setBounds(356, 190, 40, 165);

        noteA1.setBackground(new java.awt.Color(255, 255, 255));
        noteA1.setForeground(new java.awt.Color(0, 0, 0));
        noteA1.setText("A1");
        noteA1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                noteA1MousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                noteA1MouseReleased(evt);
            }
        });
        jPanel1.add(noteA1);
        noteA1.setBounds(320, 190, 56, 330);

        noteB1.setBackground(new java.awt.Color(255, 255, 255));
        noteB1.setForeground(new java.awt.Color(0, 0, 0));
        noteB1.setText("B1");
        noteB1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                noteB1MousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                noteB1MouseReleased(evt);
            }
        });
        jPanel1.add(noteB1);
        noteB1.setBounds(376, 190, 56, 330);



        noteD2B.setBackground(new java.awt.Color(0, 0, 0));
        noteD2B.setText("D2B");
        noteD2B.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                noteD2BMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                noteD2BMouseReleased(evt);
            }
        });

        jPanel1.add(noteD2B);
        noteD2B.setBounds(468, 190, 40, 165);

        noteC2.setBackground(new java.awt.Color(255, 255, 255));
        noteC2.setForeground(new java.awt.Color(0, 0, 0));
        noteC2.setText("C2");
        noteC2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                noteC2MousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                noteC2MouseReleased(evt);
            }
        });
        jPanel1.add(noteC2);
        noteC2.setBounds(432, 190, 56, 330);

        noteE2B.setBackground(new java.awt.Color(0, 0, 0));
        noteE2B.setText("E2B");
        noteE2B.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                noteE2BMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                noteE2BMouseReleased(evt);
            }
        });
        jPanel1.add(noteE2B);
        noteE2B.setBounds(524, 190, 40, 165);

        noteD2.setBackground(new java.awt.Color(255, 255, 255));
        noteD2.setForeground(new java.awt.Color(0, 0, 0));
        noteD2.setText("D2");
        noteD2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                noteD2MousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                noteD2MouseReleased(evt);
            }
        });
        jPanel1.add(noteD2);
        noteD2.setBounds(488, 190, 56, 330);

        noteG2B.setBackground(new java.awt.Color(0, 0, 0));
        noteG2B.setText("G2B#");
        noteG2B.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                noteG2BMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                noteG2BMouseReleased(evt);
            }
        });
        jPanel1.add(noteG2B);
        noteG2B.setBounds(636, 190, 40, 165);

        noteF2.setBackground(new java.awt.Color(255, 255, 255));
        noteF2.setForeground(new java.awt.Color(0, 0, 0));
        noteF2.setText("F2");
        noteF2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                noteF2MousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                noteF2MouseReleased(evt);
            }
        });
        jPanel1.add(noteF2);
        noteF2.setBounds(600, 190, 56, 330);

        noteE2.setBackground(new java.awt.Color(255, 255, 255));
        noteE2.setForeground(new java.awt.Color(0, 0, 0));
        noteE2.setText("E2");
        noteE2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                noteE2MousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                noteE2MouseReleased(evt);
            }
        });
        jPanel1.add(noteE2);
        noteE2.setBounds(544, 190, 56, 330);

        noteA2B.setBackground(new java.awt.Color(0, 0, 0));
        noteA2B.setText("A2B");
        noteA2B.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                noteA2BMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                noteA2BMouseReleased(evt);
            }
        });
        jPanel1.add(noteA2B);
        noteA2B.setBounds(692, 190, 40, 165);

        noteB2B.setBackground(new java.awt.Color(0, 0, 0));
        noteB2B.setText("B2B");
        noteB2B.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                noteB2BMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                noteB2BMouseReleased(evt);
            }
        });
        jPanel1.add(noteB2B);
        noteB2B.setBounds(748, 190, 40, 165);

        noteA2.setBackground(new java.awt.Color(255, 255, 255));
        noteA2.setForeground(new java.awt.Color(0, 0, 0));
        noteA2.setText("A2");
        noteA2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                noteA2MousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                noteA2MouseReleased(evt);
            }
        });
        jPanel1.add(noteA2);
        noteA2.setBounds(712, 190, 56, 330);

        noteG2.setBackground(new java.awt.Color(255, 255, 255));
        noteG2.setForeground(new java.awt.Color(0, 0, 0));
        noteG2.setText("G2");
        noteG2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                noteG2MousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                noteG2MouseReleased(evt);
            }
        });
        jPanel1.add(noteG2);
        noteG2.setBounds(656, 190, 56, 330);

        noteD3B.setBackground(new java.awt.Color(0, 0, 0));
        noteD3B.setText("D3B");
        noteD3B.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                noteD3BMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                noteD3BMouseReleased(evt);
            }
        });
        jPanel1.add(noteD3B);
        noteD3B.setBounds(860, 190, 40, 165);

        noteC3.setBackground(new java.awt.Color(255, 255, 255));
        noteC3.setForeground(new java.awt.Color(0, 0, 0));
        noteC3.setText("C3");
        noteC3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                noteC3MousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                noteC3MouseReleased(evt);
            }
        });
        jPanel1.add(noteC3);
        noteC3.setBounds(824, 190, 56, 330);

        noteB2.setBackground(new java.awt.Color(255, 255, 255));
        noteB2.setForeground(new java.awt.Color(0, 0, 0));
        noteB2.setText("B2");
        noteB2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                noteB2MousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                noteB2MouseReleased(evt);
            }
        });
        jPanel1.add(noteB2);
        noteB2.setBounds(768, 190, 56, 330);

        noteE3B.setBackground(new java.awt.Color(0, 0, 0));
        noteE3B.setText("E3B");
        noteE3B.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                noteE3BMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                noteE3BMouseReleased(evt);
            }
        });
        jPanel1.add(noteE3B);
        noteE3B.setBounds(916, 190, 40, 165);

        noteD3.setBackground(new java.awt.Color(255, 255, 255));
        noteD3.setForeground(new java.awt.Color(0, 0, 0));
        noteD3.setText("D3");
        noteD3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                noteD3MousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                noteD3MouseReleased(evt);
            }
        });
        jPanel1.add(noteD3);
        noteD3.setBounds(880, 190, 56, 330);

        noteG3B.setBackground(new java.awt.Color(0, 0, 0));
        noteG3B.setText("G3B#");
        noteG3B.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                noteG3BMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                noteG3BMouseReleased(evt);
            }
        });
        jPanel1.add(noteG3B);
        noteG3B.setBounds(1028, 190, 40, 165);

        noteF3.setBackground(new java.awt.Color(255, 255, 255));
        noteF3.setForeground(new java.awt.Color(0, 0, 0));
        noteF3.setText("F3");
        noteF3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                noteF3MousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                noteF3MouseReleased(evt);
            }
        });
        jPanel1.add(noteF3);
        noteF3.setBounds(992, 190, 56, 330);

        noteE3.setBackground(new java.awt.Color(255, 255, 255));
        noteE3.setForeground(new java.awt.Color(0, 0, 0));
        noteE3.setText("E3");
        noteE3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                noteE3MousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                noteE3MouseReleased(evt);
            }
        });
        jPanel1.add(noteE3);
        noteE3.setBounds(936, 190, 56, 330);

        noteA3B.setBackground(new java.awt.Color(0, 0, 0));
        noteA3B.setText("A3B");
        noteA3B.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                noteA3BMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                noteA3BMouseReleased(evt);
            }
        });
        jPanel1.add(noteA3B);
        noteA3B.setBounds(1084, 190, 40, 165);

        noteB3B.setBackground(new java.awt.Color(0, 0, 0));
        noteB3B.setText("B3B");
        noteB3B.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                noteB3BMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                noteB3BMouseReleased(evt);
            }
        });
        jPanel1.add(noteB3B);
        noteB3B.setBounds(1140, 190, 40, 165);

        noteA3.setBackground(new java.awt.Color(255, 255, 255));
        noteA3.setForeground(new java.awt.Color(0, 0, 0));
        noteA3.setText("A3");
        noteA3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                noteA3MousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                noteA3MouseReleased(evt);
            }
        });
        jPanel1.add(noteA3);
        noteA3.setBounds(1104, 190, 56, 330);

        noteG3.setBackground(new java.awt.Color(255, 255, 255));
        noteG3.setForeground(new java.awt.Color(0, 0, 0));
        noteG3.setText("G3");
        noteG3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                noteG3MousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                noteG3MouseReleased(evt);
            }
        });
        jPanel1.add(noteG3);
        noteG3.setBounds(1048, 190, 56, 330);

        noteB3.setBackground(new java.awt.Color(255, 255, 255));
        noteB3.setForeground(new java.awt.Color(0, 0, 0));
        noteB3.setText("B3");
        noteB3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                noteB3MousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                noteB3MouseReleased(evt);
            }
        });
        jPanel1.add(noteB3);
        noteB3.setBounds(1160, 190, 56, 330);

        noteD4B.setBackground(new java.awt.Color(0, 0, 0));
        noteD4B.setText("D4B");
        noteD4B.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                noteD4BMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                noteD4BMouseReleased(evt);
            }
        });
        jPanel1.add(noteD4B);
        noteD4B.setBounds(1252, 190, 40, 165);

        noteC4.setBackground(new java.awt.Color(255, 255, 255));
        noteC4.setForeground(new java.awt.Color(0, 0, 0));
        noteC4.setText("C4");
        noteC4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                noteC4MousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                noteC4MouseReleased(evt);
            }
        });
        jPanel1.add(noteC4);
        noteC4.setBounds(1216, 190, 56, 330);

        noteE4B.setBackground(new java.awt.Color(0, 0, 0));
        noteE4B.setText("E4B");
        noteE4B.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                noteE4BMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                noteE4BMouseReleased(evt);
            }
        });
        jPanel1.add(noteE4B);
        noteE4B.setBounds(1308, 190, 40, 165);

        noteD4.setBackground(new java.awt.Color(255, 255, 255));
        noteD4.setForeground(new java.awt.Color(0, 0, 0));
        noteD4.setText("D4");
        noteD4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                noteD4MousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                noteD4MouseReleased(evt);
            }
        });
        jPanel1.add(noteD4);
        noteD4.setBounds(1272, 190, 56, 330);

        noteE4.setBackground(new java.awt.Color(255, 255, 255));
        noteE4.setForeground(new java.awt.Color(0, 0, 0));
        noteE4.setText("E4");
        noteE4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                noteE4MousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                noteE4MouseReleased(evt);
            }
        });
        jPanel1.add(noteE4);
        noteE4.setBounds(1328, 190, 56, 330);

        noteG4B.setBackground(new java.awt.Color(0, 0, 0));
        noteG4B.setText("G4B");
        noteG4B.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                noteG4BMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                noteG4BMouseReleased(evt);
            }
        });
        jPanel1.add(noteG4B);
        noteG4B.setBounds(1420, 190, 40, 165);

        noteA4B.setBackground(new java.awt.Color(0, 0, 0));
        noteA4B.setText("A4B");
        noteA4B.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                noteA4BMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                noteA4BMouseReleased(evt);
            }
        });
        jPanel1.add(noteA4B);
        noteA4B.setBounds(1476, 190, 40, 165);

        noteG4.setBackground(new java.awt.Color(255, 255, 255));
        noteG4.setForeground(new java.awt.Color(0, 0, 0));
        noteG4.setText("G4");
        noteG4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                noteG4MousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                noteG4MouseReleased(evt);
            }
        });
        jPanel1.add(noteG4);
        noteG4.setBounds(1440, 190, 56, 330);

        noteF4.setBackground(new java.awt.Color(255, 255, 255));
        noteF4.setForeground(new java.awt.Color(0, 0, 0));
        noteF4.setText("F4");
        noteF4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                noteF4MousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                noteF4MouseReleased(evt);
            }
        });
        jPanel1.add(noteF4);
        noteF4.setBounds(1384, 190, 56, 330);

        noteB4B.setBackground(new java.awt.Color(0, 0, 0));
        noteB4B.setText("B4B#");
        noteB4B.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                noteB4BMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                noteB4BMouseReleased(evt);
            }
        });
        jPanel1.add(noteB4B);
        noteB4B.setBounds(1532, 190, 40, 165);

        noteA4.setBackground(new java.awt.Color(255, 255, 255));
        noteA4.setForeground(new java.awt.Color(0, 0, 0));
        noteA4.setText("A4");
        noteA4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                noteA4MousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                noteA4MouseReleased(evt);
            }
        });
        jPanel1.add(noteA4);
        noteA4.setBounds(1496, 190, 56, 330);

        noteB4.setBackground(new java.awt.Color(255, 255, 255));
        noteB4.setForeground(new java.awt.Color(0, 0, 0));
        noteB4.setText("B4");
        noteB4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                noteB4MousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                noteB4MouseReleased(evt);
            }
        });
        jPanel1.add(noteB4);
        noteB4.setBounds(1552, 190, 56, 330);

        noteC5.setBackground(new java.awt.Color(255, 255, 255));
        noteC5.setForeground(new java.awt.Color(0, 0, 0));
        noteC5.setText("C5");
        noteC5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                noteC5MousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                noteC5MouseReleased(evt);
            }
        });
        jPanel1.add(noteC5);
        noteC5.setBounds(1608, 190, 56, 330);




        getContentPane().add(jPanel1);

        pack();
    }// </editor-fold>
    private void noteC1MousePressed(java.awt.event.MouseEvent evt) {
        setFrequency(32.703);
        lineOut.start();
    }
    private void noteC1MouseReleased(java.awt.event.MouseEvent evt) {
        lineOut.stop();
    }
    private void noteD1BMousePressed(java.awt.event.MouseEvent evt) {
        setFrequency(34.648);
        lineOut.start();
    }
    private void noteD1BMouseReleased(java.awt.event.MouseEvent evt) {
        lineOut.stop();
    }
    private void noteD1MousePressed(java.awt.event.MouseEvent evt) {
        setFrequency(36.708);
        lineOut.start();
    }
    private void noteD1MouseReleased(java.awt.event.MouseEvent evt) {
        lineOut.stop();
    }
    private void noteE1BMousePressed(java.awt.event.MouseEvent evt) {
        setFrequency(38.891);
        lineOut.start();
    }
    private void noteE1BMouseReleased(java.awt.event.MouseEvent evt) {
        lineOut.stop();
    }
    private void noteE1MousePressed(java.awt.event.MouseEvent evt) {
        setFrequency(41.203);
        lineOut.start();
    }
    private void noteE1MouseReleased(java.awt.event.MouseEvent evt) {
        lineOut.stop();
    }
    private void noteF1MousePressed(java.awt.event.MouseEvent evt) {
        setFrequency(43.654);
        lineOut.start();
    }
    private void noteF1MouseReleased(java.awt.event.MouseEvent evt) {
        lineOut.stop();
    }
    private void noteG1BMousePressed(java.awt.event.MouseEvent evt) {
        setFrequency(46.249);
        lineOut.start();
    }
    private void noteG1BMouseReleased(java.awt.event.MouseEvent evt) {
        lineOut.stop();
    }
    private void noteG1MousePressed(java.awt.event.MouseEvent evt) {
        setFrequency(48.999);
        lineOut.start();
    }
    private void noteG1MouseReleased(java.awt.event.MouseEvent evt) {
        lineOut.stop();
    }
    private void noteA1BMousePressed(java.awt.event.MouseEvent evt) {
        setFrequency(51.913);
        lineOut.start();
    }
    private void noteA1BMouseReleased(java.awt.event.MouseEvent evt) {
        lineOut.stop();
    }
    private void noteA1MousePressed(java.awt.event.MouseEvent evt) {
        setFrequency(55.000);
        lineOut.start();
    }
    private void noteA1MouseReleased(java.awt.event.MouseEvent evt) {
        lineOut.stop();
    }
    private void noteB1BMousePressed(java.awt.event.MouseEvent evt) {
        setFrequency(58.270);
        lineOut.start();
    }
    private void noteB1BMouseReleased(java.awt.event.MouseEvent evt) {
        lineOut.stop();
    }
    private void noteB1MousePressed(java.awt.event.MouseEvent evt) {
        setFrequency(61.735);
        lineOut.start();
    }
    private void noteB1MouseReleased(java.awt.event.MouseEvent evt) {
        lineOut.stop();
    }
    private void noteC2MousePressed(java.awt.event.MouseEvent evt) {
        setFrequency(65.406);
        lineOut.start();
    }
    private void noteC2MouseReleased(java.awt.event.MouseEvent evt) {
        lineOut.stop();
    }
    private void noteD2BMousePressed(java.awt.event.MouseEvent evt) {
        setFrequency(69.296);
        lineOut.start();
    }
    private void noteD2BMouseReleased(java.awt.event.MouseEvent evt) {
        lineOut.stop();
    }
    private void noteD2MousePressed(java.awt.event.MouseEvent evt) {
        setFrequency(73.416);
        lineOut.start();
    }
    private void noteD2MouseReleased(java.awt.event.MouseEvent evt) {
        lineOut.stop();
    }
    private void noteE2BMousePressed(java.awt.event.MouseEvent evt) {
        setFrequency(77.782);
        lineOut.start();
    }
    private void noteE2BMouseReleased(java.awt.event.MouseEvent evt) {
        lineOut.stop();
    }
    private void noteE2MousePressed(java.awt.event.MouseEvent evt) {
        setFrequency(82.407);
        lineOut.start();
    }
    private void noteE2MouseReleased(java.awt.event.MouseEvent evt) {
        lineOut.stop();
    }
    private void noteF2MousePressed(java.awt.event.MouseEvent evt) {
        setFrequency(87.307);
        lineOut.start();
    }
    private void noteF2MouseReleased(java.awt.event.MouseEvent evt) {
        lineOut.stop();
    }
    private void noteG2BMousePressed(java.awt.event.MouseEvent evt) {
        setFrequency(92.499);
        lineOut.start();
    }
    private void noteG2BMouseReleased(java.awt.event.MouseEvent evt) {
        lineOut.stop();
    }
    private void noteG2MousePressed(java.awt.event.MouseEvent evt) {
        setFrequency(97.999);
        lineOut.start();
    }
    private void noteG2MouseReleased(java.awt.event.MouseEvent evt) {
        lineOut.stop();
    }
    private void noteA2BMousePressed(java.awt.event.MouseEvent evt) {
        setFrequency(103.83);
        lineOut.start();
    }
    private void noteA2BMouseReleased(java.awt.event.MouseEvent evt) {
        lineOut.stop();
    }
    private void noteA2MousePressed(java.awt.event.MouseEvent evt) {
        setFrequency(110.00);
        lineOut.start();
    }
    private void noteA2MouseReleased(java.awt.event.MouseEvent evt) {
        lineOut.stop();
    }
    private void noteB2BMousePressed(java.awt.event.MouseEvent evt) {
        setFrequency(116.54);
        lineOut.start();
    }
    private void noteB2BMouseReleased(java.awt.event.MouseEvent evt) {
        lineOut.stop();
    }
    private void noteB2MousePressed(java.awt.event.MouseEvent evt) {
        setFrequency(123.47);
        lineOut.start();
    }
    private void noteB2MouseReleased(java.awt.event.MouseEvent evt) {
        lineOut.stop();
    }
    private void noteC3MousePressed(java.awt.event.MouseEvent evt) {
        setFrequency(130.81);
        dahdsr.input.on();
    }
    private void noteC3MouseReleased(java.awt.event.MouseEvent evt) {
        dahdsr.input.off();
    }
    private void noteD3BMousePressed(java.awt.event.MouseEvent evt) {
        setFrequency(138.59);
        lineOut.start();
    }
    private void noteD3BMouseReleased(java.awt.event.MouseEvent evt) {
        lineOut.stop();
    }
    private void noteD3MousePressed(java.awt.event.MouseEvent evt) {
        setFrequency(146.83);
        lineOut.start();
    }
    private void noteD3MouseReleased(java.awt.event.MouseEvent evt) {
        lineOut.stop();
    }
    private void noteE3BMousePressed(java.awt.event.MouseEvent evt) {
        setFrequency(155.56);
        lineOut.start();
    }
    private void noteE3BMouseReleased(java.awt.event.MouseEvent evt) {
        lineOut.stop();
    }
    private void noteE3MousePressed(java.awt.event.MouseEvent evt) {
        setFrequency(164.81);
        lineOut.start();
    }
    private void noteE3MouseReleased(java.awt.event.MouseEvent evt) {
        lineOut.stop();
    }
    private void noteF3MousePressed(java.awt.event.MouseEvent evt) {
        setFrequency(176.41);
        lineOut.start();
    }
    private void noteF3MouseReleased(java.awt.event.MouseEvent evt) {
        lineOut.stop();
    }
    private void noteG3BMousePressed(java.awt.event.MouseEvent evt) {
        setFrequency(185.00);
        lineOut.start();
    }
    private void noteG3BMouseReleased(java.awt.event.MouseEvent evt) {
        lineOut.stop();
    }
    private void noteG3MousePressed(java.awt.event.MouseEvent evt) {
        setFrequency(196.00);
        lineOut.start();
    }
    private void noteG3MouseReleased(java.awt.event.MouseEvent evt) {
        lineOut.stop();
    }
    private void noteA3BMousePressed(java.awt.event.MouseEvent evt) {
        setFrequency(207.65);
        lineOut.start();
    }
    private void noteA3BMouseReleased(java.awt.event.MouseEvent evt) {
        lineOut.stop();
    }
    private void noteA3MousePressed(java.awt.event.MouseEvent evt) {
        setFrequency(220.00);
        lineOut.start();
    }
    private void noteA3MouseReleased(java.awt.event.MouseEvent evt) {
        lineOut.stop();
    }
    private void noteB3BMousePressed(java.awt.event.MouseEvent evt) {
        setFrequency(233.08);
        lineOut.start();
    }
    private void noteB3BMouseReleased(java.awt.event.MouseEvent evt) {
        lineOut.stop();
    }
    private void noteB3MousePressed(java.awt.event.MouseEvent evt) {
        setFrequency(246.94);
        lineOut.start();
    }
    private void noteB3MouseReleased(java.awt.event.MouseEvent evt) {
        lineOut.stop();
    }
    private void noteC4MousePressed(java.awt.event.MouseEvent evt) {
        setFrequency(261.63);
        lineOut.start();
    }
    private void noteC4MouseReleased(java.awt.event.MouseEvent evt) {
        lineOut.stop();
    }
    private void noteD4BMousePressed(java.awt.event.MouseEvent evt) {
        setFrequency(277.18);
        lineOut.start();
    }
    private void noteD4BMouseReleased(java.awt.event.MouseEvent evt) {
        lineOut.stop();
    }
    private void noteD4MousePressed(java.awt.event.MouseEvent evt) {
        setFrequency(293.67);
        lineOut.start();
    }
    private void noteD4MouseReleased(java.awt.event.MouseEvent evt) {
        lineOut.stop();
    }
    private void noteE4BMousePressed(java.awt.event.MouseEvent evt) {
        setFrequency(311.13);
        lineOut.start();
    }
    private void noteE4BMouseReleased(java.awt.event.MouseEvent evt) {
        lineOut.stop();
    }
    private void noteE4MousePressed(java.awt.event.MouseEvent evt) {
        setFrequency(329.63);
        lineOut.start();
    }
    private void noteE4MouseReleased(java.awt.event.MouseEvent evt) {
        lineOut.stop();
    }
    private void noteF4MousePressed(java.awt.event.MouseEvent evt) {
        setFrequency(349.23);
        lineOut.start();
    }
    private void noteF4MouseReleased(java.awt.event.MouseEvent evt) {
        lineOut.stop();
    }
    private void noteG4BMousePressed(java.awt.event.MouseEvent evt) {
        setFrequency(366.99);
        lineOut.start();
    }
    private void noteG4BMouseReleased(java.awt.event.MouseEvent evt) {
        lineOut.stop();
    }
    private void noteG4MousePressed(java.awt.event.MouseEvent evt) {
        setFrequency(392.00);
        lineOut.start();
    }
    private void noteG4MouseReleased(java.awt.event.MouseEvent evt) {
        lineOut.stop();
    }
    private void noteA4BMousePressed(java.awt.event.MouseEvent evt) {
        setFrequency(415.30);
        lineOut.start();
    }
    private void noteA4BMouseReleased(java.awt.event.MouseEvent evt) {
        lineOut.stop();
    }
    private void noteA4MousePressed(java.awt.event.MouseEvent evt) {
        setFrequency(440.00);
        lineOut.start();
    }
    private void noteA4MouseReleased(java.awt.event.MouseEvent evt) {
        lineOut.stop();
    }
    private void noteB4BMousePressed(java.awt.event.MouseEvent evt) {
        setFrequency(466.16);
        lineOut.start();
    }
    private void noteB4BMouseReleased(java.awt.event.MouseEvent evt) {
        lineOut.stop();
    }
    private void noteB4MousePressed(java.awt.event.MouseEvent evt) {
        setFrequency(493.88);
        lineOut.start();
    }
    private void noteB4MouseReleased(java.awt.event.MouseEvent evt) {
        lineOut.stop();
    }
    private void noteC5MousePressed(java.awt.event.MouseEvent evt) {
        setFrequency(523.25);
        lineOut.start();
    }
    private void noteC5MouseReleased(java.awt.event.MouseEvent evt) {
        lineOut.stop();
    }








    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Piano.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Piano.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Piano.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Piano.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                Piano piano=new Piano();
                piano.setSize(1920,1080);
                piano.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify
    private javax.swing.JPanel jPanel1;
    private javax.swing.JCheckBox sineBox;
    private javax.swing.JCheckBox squareBox;
    private javax.swing.JCheckBox sawBox;
    private javax.swing.JCheckBox triBox;
    private javax.swing.JButton note47;
    private javax.swing.JButton noteA1;
    private javax.swing.JButton noteA1B;
    private javax.swing.JButton noteA2;
    private javax.swing.JButton noteA2B;
    private javax.swing.JButton noteA3;
    private javax.swing.JButton noteA3B;
    private javax.swing.JButton noteA4;
    private javax.swing.JButton noteA4B;
    private javax.swing.JButton noteB1;
    private javax.swing.JButton noteB1B;
    private javax.swing.JButton noteB2;
    private javax.swing.JButton noteB2B;
    private javax.swing.JButton noteB3;
    private javax.swing.JButton noteB3B;
    private javax.swing.JButton noteB4;
    private javax.swing.JButton noteB4B;
    private javax.swing.JButton noteC1;
    private javax.swing.JButton noteC2;
    private javax.swing.JButton noteC3;
    private javax.swing.JButton noteC4;
    private javax.swing.JButton noteC5;
    private javax.swing.JButton noteD1;
    private javax.swing.JButton noteD1B;
    private javax.swing.JButton noteD2;
    private javax.swing.JButton noteD2B;
    private javax.swing.JButton noteD3;
    private javax.swing.JButton noteD3B;
    private javax.swing.JButton noteD4;
    private javax.swing.JButton noteD4B;
    private javax.swing.JButton noteE1;
    private javax.swing.JButton noteE1B;
    private javax.swing.JButton noteE2;
    private javax.swing.JButton noteE2B;
    private javax.swing.JButton noteE3;
    private javax.swing.JButton noteE3B;
    private javax.swing.JButton noteE4;
    private javax.swing.JButton noteE4B;
    private javax.swing.JButton noteF1;
    private javax.swing.JButton noteF2;
    private javax.swing.JButton noteF3;
    private javax.swing.JButton noteF4;
    private javax.swing.JButton noteG1;
    private javax.swing.JButton noteG1B;
    private javax.swing.JButton noteG2;
    private javax.swing.JButton noteG2B;
    private javax.swing.JButton noteG3;
    private javax.swing.JButton noteG3B;
    private javax.swing.JButton noteG4;
    private javax.swing.JButton noteG4B;
    // End of variables declaration
}
