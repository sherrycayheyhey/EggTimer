package com.chromsicle.eggtimer;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.nio.channels.SeekableByteChannel;

public class MainActivity extends AppCompatActivity {

    //these need to be accessed by multiple methods
    TextView timerText;
    SeekBar minuteSeekBar;
    Boolean counterIsActive = false;
    Button goButton;
    CountDownTimer countDownTimer;

    public void resetTimer(){
        timerText.setText("0:30");
        minuteSeekBar.setProgress(30);
        minuteSeekBar.setEnabled(true);
        countDownTimer.cancel();
        goButton.setText("Start!");
        counterIsActive = false;
    }

    public CountDownTimer getCountDownTimer() {
        return countDownTimer;
    }

    public void startButtonClicked (View view) {

        //if the timer is already active, reset to starting state
        if (counterIsActive) {
            resetTimer();

        //if the timer isn't already active, set things like normal
        } else {


            counterIsActive = true;
            minuteSeekBar.setEnabled(false);
            goButton.setText("STOP");

            //100 is added here because the timer is a little off and this is a hack to make it end when we really want
             countDownTimer = new CountDownTimer(minuteSeekBar.getProgress() * 1000 + 100, 1000) {
                @Override
                public void onTick(long l) {
                    updateTimer((int) l / 1000); //converts into seconds
                }

                @Override
                public void onFinish() {
                    //Log.i("finished", "timer done");
                    MediaPlayer mplayer = MediaPlayer.create(getApplicationContext(), R.raw.partyhorn);
                    mplayer.start();
                    resetTimer();
                }
            }.start();
        }
    }

    public void updateTimer(int secondsLeft){
        int minutes = secondsLeft / 60;
        int seconds = secondsLeft - (minutes * 60);

        //proper formatting for when the number is a 0, displays as 00 instead
        String secondString = Integer.toString(seconds);
        if(seconds <= 9){
            secondString = "0" + secondString;
        }

        //set the TextView to be whatever the Seekbar is set to
        timerText.setText(minutes + ":" + secondString);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //make it so the seekBar controls the number of minutes on the timer
        //find the SeekBar and TextView
        minuteSeekBar = findViewById(R.id.minutesSeekBar);
        timerText = findViewById(R.id.timerText);
        goButton = findViewById(R.id.startButton);

        minuteSeekBar.setMax(600); //10 minute max
        minuteSeekBar.setProgress(30); //where it starts

        minuteSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                updateTimer(i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });



    }
}
