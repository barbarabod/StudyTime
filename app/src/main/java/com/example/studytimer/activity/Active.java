package com.example.studytimer.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.studytimer.R;
import com.example.studytimer.dboperation.DBHelper;
import com.example.studytimer.dboperation.TypeOfAction;
import com.example.studytimer.dboperation.dbobjects.Times;

import java.sql.SQLException;
import java.util.Date;
import java.util.Locale;

public class Active extends AppCompatActivity {

    private DBHelper dbHelper;
    private long dataBaseStartTime;
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
        dbHelper = new DBHelper(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active);
        timeText = findViewById(R.id.text_view_countdown);
        infoText = findViewById(R.id.text_information);
        controlButton = findViewById(R.id.button_stop);
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
    }

    private void startTimer(){
        if(isOnPause){
            timeLeftInMillis = 30000;
        }
        else{
            timeLeftInMillis = 60000;
        }
        dataBaseStartTime = System.currentTimeMillis();
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
                long mills = 500L;
                Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

                if(isOnPause){
                    try {
                        dbHelper.create(new Times(TypeOfAction.PAUSE,new Date(dataBaseStartTime), new Date(System.currentTimeMillis())));
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    infoText.setText("Ucz się!\n Do przerwy zostało:");
                }
                else{
                    try {
                        dbHelper.create(new Times(TypeOfAction.WORK,new Date(dataBaseStartTime), new Date(System.currentTimeMillis())));
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    infoText.setText("Przerwa!");
                }
                isOnPause = !isOnPause;
                if (vibrator.hasVibrator()) {
                    vibrator.vibrate(mills);
                }
                startTimer();
            }
        }.start();

        isTimerRunning = true;
    }
    private void stopTimer(){
        countDownTimer.cancel();
        isTimerRunning = false;
        if(isOnPause){
            try {
                dbHelper.create(new Times(TypeOfAction.PAUSE,new Date(dataBaseStartTime), new Date(System.currentTimeMillis())));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        else{
            try {
                dbHelper.create(new Times(TypeOfAction.WORK,new Date(dataBaseStartTime), new Date(System.currentTimeMillis())));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
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
        outState.putLong("dataBaseStartTime", dataBaseStartTime);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);
        timeLeftInMillis = savedInstanceState.getLong("timeLeftInMillis");
        isTimerRunning = savedInstanceState.getBoolean("isTimerRunning");
        isOnPause = savedInstanceState.getBoolean("isOnPause");
        dataBaseStartTime = savedInstanceState.getLong("dataBaseStartTime");

        if(isTimerRunning){
            endTime = savedInstanceState.getLong("endTime");
            timeLeftInMillis = endTime - System.currentTimeMillis();
            startTimer();
        }
    }
}
