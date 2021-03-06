package com.example.studytimer.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.studytimer.R;
import com.example.studytimer.dboperation.DBHelper;
import com.example.studytimer.dboperation.TypeOfAction;
import com.example.studytimer.dboperation.dbobjects.Times;

import java.sql.SQLException;
import java.util.Date;
import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

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
    private SharedPreferences sharedPreferences;
    private boolean terminateTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        dbHelper = new DBHelper(this);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active);
        timeText = findViewById(R.id.text_view_countdown);
        infoText = findViewById(R.id.text_information);
        controlButton = findViewById(R.id.button_stop);
        timeText.setVisibility(View.INVISIBLE);
        controlButton.setText(R.string.start_button);
        isOnPause = false;
        infoText.setText(R.string.information_start);
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
        terminateTimer = false;
        if(isOnPause){
            timeLeftInMillis = Long.parseLong(sharedPreferences.getString("break_time", "10000")) * 1000;
        }
        else{
            timeLeftInMillis = Long.parseLong(sharedPreferences.getString("study_time", "10000")) * 1000;
            infoText.setText(R.string.information_study);
        }
        dataBaseStartTime = System.currentTimeMillis();
        endTime = System.currentTimeMillis() + timeLeftInMillis;
        timeText.setVisibility(View.VISIBLE);
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateCountdownText();
                controlButton.setText(R.string.button_stop);

            }

            @Override
            public void onFinish() {
                if(terminateTimer){
                    countDownTimer.cancel();
                    return;
                }
                long mills = 500L;
                Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

                if(isOnPause){
                    try {
                        dbHelper.create(new Times(TypeOfAction.PAUSE,new Date(dataBaseStartTime), new Date(System.currentTimeMillis())));
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    infoText.setText(R.string.information_study);
                }
                else{
                    try {
                        dbHelper.create(new Times(TypeOfAction.WORK,new Date(dataBaseStartTime), new Date(System.currentTimeMillis())));
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    infoText.setText(R.string.information_break);
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
        terminateTimer = true;
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
        infoText.setText(R.string.information_start);
        controlButton.setText(R.string.start_button);
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
