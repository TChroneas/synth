import com.jsyn.ports.UnitInputPort;
import com.jsyn.unitgen.*;

public class SubSynth extends Circuit {
    private UnitOscillator osc;
    private FilterLowPass filter;
    private EnvelopeDAHDSR ampEnv;
    private Add cutoffAdder;
    private UnitInputPort ampl;
    private UnitInputPort freq;
    private UnitInputPort cutoff;

    public SubSynth() {
        add(osc=new TriangleOscillator());
        add(ampEnv=new EnvelopeDAHDSR());
        add(filter=new FilterLowPass());
        add((cutoffAdder=new Add()));
        cutoffAdder.output.connect(filter.frequency);
        osc.output.connect(filter.input);
        filter.output.connect(ampEnv.amplitude);
        addPort(ampl=osc.amplitude,"Ampl");
        addPort(cutoff=cutoffAdder.inputB,"Cutoff");
        ampEnv.export(this,"Amp");
        freq.setup(osc.frequency);
        cutoff.setup(filter.frequency);



    }
}
