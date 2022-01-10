package random;/*
 * Copyright 2009 Phil Burk, Mobileer Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */



import com.jsyn.JSyn;
import com.jsyn.Synthesizer;
import com.jsyn.unitgen.LineOut;
import com.jsyn.unitgen.SawtoothOscillator;
import com.softsynth.shared.time.TimeStamp;

/**
 * Play notes using timestamped noteOn and noteOff methods of the UnitVoice.
 *
 * @author Phil Burk (C) 2009 Mobileer Inc
 */
public class PlayNotes {

    private void test() {

        // Create a context for the synthesizer.
        Synthesizer synth = JSyn.createSynthesizer();
        // Set output latency to 123 msec because this is not an interactive app.
        synth.getAudioDeviceManager().setSuggestedOutputLatency(0.123);

        // Add a tone generator.
        SawtoothOscillator voice = new SawtoothOscillator();
        synth.add(voice);
        // synth.add( ugen = new SineOscillator() );
        // synth.add( ugen = new SubtractiveSynthVoice() );
        // Add an output mixer.
        LineOut lineOut = new LineOut();
        synth.add(lineOut);

        // Connect the oscillator to the left and right audio output.
        voice.getOutput().connect(0, lineOut.input, 0);
        voice.getOutput().connect(0, lineOut.input, 1);

        // Start synthesizer using default stereo output at 44100 Hz.
        synth.start();

        // Get synthesizer time in seconds.
        double timeNow = synth.getCurrentTime();

        // Advance to a near future time so we have a clean start.
        TimeStamp timeStamp = new TimeStamp(timeNow + 0.5);

        // We only need to start the LineOut. It will pull data from the
        // oscillator.
        synth.startUnit(lineOut, timeStamp);

        // Schedule a note on and off.
        double freq = 200.0; // hertz
        double duration = 1.4;
        double onTime = 1.0;
        voice.noteOn(freq, 0.5, timeStamp);
        voice.noteOff(timeStamp.makeRelative(onTime));

        // Schedule this to happen a bit later.
        timeStamp = timeStamp.makeRelative(duration);
        freq *= 1.5; // up a perfect fifth
        voice.noteOn(freq, 0.5, timeStamp);
        voice.noteOff(timeStamp.makeRelative(onTime));

        timeStamp = timeStamp.makeRelative(duration);
        freq *= 4.0 / 5.0; // down a major third
        voice.noteOn(freq, 0.5, timeStamp);
        voice.noteOff(timeStamp.makeRelative(onTime));

        // Sleep while the song is being generated in the background thread.
        try {
            synth.sleepUntil(timeStamp.getTime() + 2.0);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Stop everything.
        synth.stop();
    }

    public static void main(String[] args) {
        new PlayNotes().test();
    }
}