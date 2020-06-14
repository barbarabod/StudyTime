package com.example.studytimer.activity;

import android.content.SharedPreferences;
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

    private TextView infoText;
    private TextView timeText;
    private Button controlButton;
    private CountDownTimer countDownTimer;
    private boolean isTimerRunning;
    private long timeLeftInMillis = 60000;
    private long endTime;
    private boolean isOnPause;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active);
        timeText = (TextView)findViewById(R.id.text_view_countdown);
        infoText = (TextView)findViewById(R.id.text_information);
        controlButton = (Button)findViewById(R.id.button_stop);
        timeText.setVisibility(View.INVISIBLE);
        controlButton.setText("Start");
        isOnPause = false;
        infoText.setText("Zacznij naukę");
    }

    public void onStartButton(View view) {

        if(isTimerRunning){
            stopTimer();
        }
        else{
            startTimer();
        }
        Date currentTime = Calendar.getInstance().getTime();
        timeText.setText(currentTime.toString());
    }

    private void startTimer(){
        if(isOnPause){
            timeLeftInMillis = 30000;
        }
        else{
            timeLeftInMillis = 60000;
        }
        endTime = System.currentTimeMillis() + timeLeftInMillis;
        timeText.setVisibility(View.VISIBLE);
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateCountdownText();
                controlButton.setText("Koniec");

            }

            @Override
            public void onFinish() {
                if(isOnPause){
                    infoText.setText("Ucz się!\n Do przerwy zostało:");
                }
                else{
                    infoText.setText("Przerwa!");
                }
                isOnPause = !isOnPause;
                startTimer();
            }
        }.start();

        isTimerRunning = true;
    }
    private void stopTimer(){
        countDownTimer.cancel();
        isTimerRunning = false;
        isOnPause = false;
        infoText.setText("Zacznij naukę");
        controlButton.setText("Start");
        timeText.setVisibility(View.INVISIBLE);
    }

    private void updateCountdownText(){
        int minutes = (int) timeLeftInMillis/1000/60;
        int seconds = (int) timeLeftInMillis/1000 % 60;
        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d",minutes, seconds);
        timeText.setText(timeLeftFormatted);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putLong("timeLeftInMillis", timeLeftInMillis);
        outState.putBoolean("isTimerRunning", isTimerRunning);
        outState.putLong("endTime", endTime);
        outState.putBoolean("isOnPause", isOnPause);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);
        timeLeftInMillis = savedInstanceState.getLong("timeLeftInMillis");
        isTimerRunning = savedInstanceState.getBoolean("isTimerRunning");
        isOnPause = savedInstanceState.getBoolean("isOnPause");

        if(isTimerRunning){
            endTime = savedInstanceState.getLong("endTime");
            timeLeftInMillis = endTime - System.currentTimeMillis();
            startTimer();
        }
    }
}
