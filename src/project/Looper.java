package project;

import javafx.scene.media.AudioClip;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.Media;
import javafx.embed.swing.JFXPanel;
import javafx.util.Duration;

import java.io.File;

public class Looper extends Thread{

    private MediaPlayer player;
    private AudioClip clip;
    private MediaPlayer sec;

    public AudioClip getClip() {
        return clip;

    }

    public MediaPlayer getPlayer() {
        return player;
    }

    public void run() {


            player.play();





    }


    public Looper()  {
        final JFXPanel fxPanel = new JFXPanel();
        File file=new File("src\\project\\looper.mp3");
        Media media=new Media(file.toURI().toString());
         clip=new AudioClip(file.toURI().toString());
        player = new MediaPlayer( media);
        sec=new MediaPlayer(media);
        player.setCycleCount(MediaPlayer.INDEFINITE);
        player.setOnReady(new Runnable() {
            @Override
            public void run() {
                player.play();
            }
        });
        player.setOnEndOfMedia(new Runnable() {
            @Override
            public void run() {
                player.setCycleCount(MediaPlayer.INDEFINITE);
            }
        });


    }



}
