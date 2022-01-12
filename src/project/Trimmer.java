package project;

import java.io.*;
import java.time.Duration;
import javax.sound.sampled.*;

public class Trimmer {
    File file;
    int tempo;
    int beats;


    public static void main (String args[]){
        Trimmer trimmer=new Trimmer(new File("src\\project\\looper1.wav"),110,16);


    }

    public Trimmer(File file,int beats,int tempo) {
        this.file=file;
        this.beats=beats;
        this.tempo=tempo;
        double falseDuration=getDurationOfWavInSeconds(file)*1000;
        double correctDuration=60000*beats/tempo;
        double timeToCut=falseDuration-correctDuration;

        TrimAudio(file,(int)timeToCut,(int)correctDuration);
        double newDur=getDurationOfWavInSeconds(new File("src\\project\\looperFinal.wav"));
        System.out.println();



    }
    public static double getDurationOfWavInSeconds(File file)
    {
        AudioInputStream stream = null;
        try
        {
            stream = AudioSystem.getAudioInputStream(file);
            AudioFormat format = stream.getFormat();
            return file.length() / format.getSampleRate() / (format.getSampleSizeInBits() / 8.0) / format.getChannels();
        }
        catch (Exception e)
        {
            return -1;
        }
        finally
        {
            try { stream.close(); } catch (Exception ex) { }
        }
    }

    public static void TrimAudio(File file, int startMs, int msToCopy) {
        AudioInputStream inputStream = null;
        AudioInputStream shortenedStream = null;
        try {
            AudioFileFormat fileFormat = AudioSystem.getAudioFileFormat(file);
            AudioFormat format = fileFormat.getFormat();
            inputStream = AudioSystem.getAudioInputStream(file);
            int bytesPerMs = format.getFrameSize() * (int)format.getFrameRate()/1000;
            inputStream.skip(startMs * bytesPerMs);
            long framesOfAudioToCopy = msToCopy * (int)format.getFrameRate()/1000;
            shortenedStream = new AudioInputStream(inputStream, format, framesOfAudioToCopy);
            File destinationFile = new File("src\\project\\looperFinal.wav");
            AudioSystem.write(shortenedStream, fileFormat.getType(), destinationFile);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) try { inputStream.close(); } catch (Exception e) { e.printStackTrace(); }
            if (shortenedStream != null) try { shortenedStream.close(); } catch (Exception e) { e.printStackTrace(); }
        }
    }



}