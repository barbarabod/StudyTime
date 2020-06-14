package com.example.studytimer.activity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import com.example.studytimer.R;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Active extends AppCompatActivity {

    private TextView timeText;
    private Button controlButton;
    private CountDownTimer countDownTimer;
    private boolean isTimerRunning;
    private long timeLeftinMillis = 60000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active);
        timeText = (TextView)findViewById(R.id.text_view_countdown);
        controlButton = (Button)findViewById(R.id.button_stop);
        controlButton.setText("Start");
    }

    public void onStartButton(View view) {

        if(isTimerRunning){
            pauseTimer();
        }
        else{
            startTimer();
        }
        Date currentTime = Calendar.getInstance().getTime();
        timeText.setText(currentTime.toString());
    }

    private void startTimer(){
        countDownTimer = new CountDownTimer(timeLeftinMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftinMillis = millisUntilFinished;
                updateCountdownText();
                controlButton.setText("Stop");

            }

            @Override
            public void onFinish() {

            }
        }.start();

        isTimerRunning = true;
    }
    private void pauseTimer(){

    }

    private void updateCountdownText(){
        int minutes = (int) timeLeftinMillis/1000/60;
        int seconds = (int) timeLeftinMillis/1000 % 60;
        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d",minutes, seconds);
        timeText.setText(timeLeftFormatted);
    }
}
