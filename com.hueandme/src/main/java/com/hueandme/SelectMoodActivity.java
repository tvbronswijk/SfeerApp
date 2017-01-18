package com.hueandme;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.hueandme.sfeer.EmotionController;

/**
 * Created by Sander on 13-1-2017.
 */

public class SelectMoodActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectmood);
        getSupportActionBar().setTitle("Moods");

        Button happyButton = (Button) findViewById(R.id.happybuttonMood);
        happyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView activeImage = (TextView) findViewById(R.id.activeMood);

                activeImage.setText(getResources().getString(R.string.btn_happy));
                EmotionController.setCurrentEmotion(SelectMoodActivity.this, EmotionController.Emotion.Happy);
            }
        });

        Button angryButton = (Button) findViewById(R.id.angrybuttonMood);
        angryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView activeImage = (TextView) findViewById(R.id.activeMood);
                activeImage.setText(getResources().getString(R.string.btn_angry));
                EmotionController.setCurrentEmotion(SelectMoodActivity.this, EmotionController.Emotion.Comfort);
            }
        });

        Button sadButton = (Button) findViewById(R.id.sadbuttonMood);
        sadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView activeImage = (TextView) findViewById(R.id.activeMood);
                activeImage.setText(getResources().getString(R.string.btn_sad));
                EmotionController.setCurrentEmotion(SelectMoodActivity.this, EmotionController.Emotion.Peaceful);
            }
        });

        Button phlegmaticButton = (Button) findViewById(R.id.phlegmaticbuttonMood);
        phlegmaticButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView activeImage = (TextView) findViewById(R.id.activeMood);
                activeImage.setText(getResources().getString(R.string.btn_phlegmatic));
                EmotionController.setCurrentEmotion(SelectMoodActivity.this, EmotionController.Emotion.Optimistic);
            }
        });

        Button comfortButton = (Button) findViewById(R.id.comfortbuttonMood);
        comfortButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView activeImage = (TextView) findViewById(R.id.activeMood);
                activeImage.setText(getResources().getString(R.string.btn_comfort));
                EmotionController.setCurrentEmotion(SelectMoodActivity.this, EmotionController.Emotion.Inspired);
            }
        });
    }
}
