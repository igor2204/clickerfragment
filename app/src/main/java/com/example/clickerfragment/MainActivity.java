package com.example.clickerfragment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private int score = 0;
    private int targetScore = 100;
    private boolean gameEnded = false;

    final static String KEY_RESULT = "result";

    private static final String ELAPSED_TIME_PASSED_KEY = "elapsedTimePassed";

    private long elapsedTimePassed = 0;

    private TextView textTime;

    private TextView numberText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        TextView numberText = findViewById(R.id.number);
        TextView textTime = findViewById(R.id.textTimer);
        Button clickButton = findViewById(R.id.buttonclick);
        Button resetBtn = findViewById(R.id.resetBtn);
        TextView savetime = findViewById(R.id.savetime);
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        resetBtn.setVisibility(View.GONE);
        elapsedTimePassed = preferences.getLong(ELAPSED_TIME_PASSED_KEY, 0);

        clickButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!gameEnded) {
                    score += 5;  // Увеличить количество очков при нажатии кнопки
                    if (score >= targetScore) {
                        replaceFragment(new fragment_wictory());
                        clickButton.setVisibility(View.GONE);
                        resetBtn.setVisibility(View.VISIBLE);
                        MediaPlayer play = MediaPlayer.create(MainActivity.this, R.raw.wictory);
                        play.start();
                    }
                }
                savetime.setText("Рекордное время " + elapsedTimePassed);
                TextView numberText = findViewById(R.id.number);
                numberText.setText("Количество очков: " + score);
                MediaPlayer play = MediaPlayer.create(MainActivity.this, R.raw.punch);
                play.start();
            }
        });


        new CountDownTimer(30000, 1000) {
            public void onTick(long l) {
                textTime.setText("" + l / 1000);
                elapsedTimePassed =(30 - l)/1000;

            }

            public void onFinish() {
                if (score < targetScore) {
                    replaceFragment(new fragment_lose());
                    resetBtn.setVisibility(View.VISIBLE);
                    clickButton.setVisibility(View.GONE);
                    MediaPlayer play = MediaPlayer.create(MainActivity.this, R.raw.lose);
                    play.start();

                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putLong(ELAPSED_TIME_PASSED_KEY, elapsedTimePassed);
                    editor.apply();
                }
            }

        }.start();
        resetBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }
        });
    }
    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager= getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout,fragment);
        fragmentTransaction.commit();
    }



}