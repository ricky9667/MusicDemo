package com.example.sounddemo;

import androidx.appcompat.app.AppCompatActivity;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    MediaPlayer mediaPlayer;
    AudioManager audioManager;

    public int save;
    public boolean flag;

    // button for starting the music
    public void play(View view) {
        mediaPlayer.start();
    }

    // button for pausing the music
    public void pause(View view) {
        mediaPlayer.pause();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // get audio service to control media in your code
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);

        // create the media player
        mediaPlayer = MediaPlayer.create(this, R.raw.test);

        // get volume from the integer
        // NOTE: the value is not 0 - 100
        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);

        // seekbar for controlling the volume
        SeekBar volumeSeekBar = findViewById(R.id.volumeSeekBar);

        // set max value and current value of the volume seekbar
        volumeSeekBar.setMax(maxVolume);
        volumeSeekBar.setProgress(currentVolume);

        volumeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                // Log.i("Seekbar changed", Integer.toString(progress));
                // changes the system volume when changing the seek bar
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
            }

            // NOTE: these functions should be implemented

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });

        // seekbar for controlling the progress of the music
        final SeekBar scrubSeekBar = findViewById(R.id.scrubSeekBar);

        // set max value of the progress seekbar
        scrubSeekBar.setMax(mediaPlayer.getDuration());

        scrubSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.i("Seekbar Moved", Integer.toString(progress));
                // save progress to a public int
                save = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // pauses music when changing the seekbar
                mediaPlayer.pause();
                flag = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // set the new music progress when not changing the seekbar
                mediaPlayer.seekTo(save);
                flag = false;
                // start playing after changing the seekbar
                mediaPlayer.start();
            }
        });

        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if(!flag) {
                    // avoid lags when playing music
                    scrubSeekBar.setProgress(mediaPlayer.getCurrentPosition());
                }
            }
        }, 0, 200);
        // last variable sets the distance between 2 times
    }
}
