package com.hueandme;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Sander on 13-1-2017.
 */

public class SelectMoodActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectmood);
        Button happyButton = (Button) findViewById(R.id.happybuttonMood);
        happyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView activeImage = (TextView) findViewById(R.id.activeMood);
                activeImage.setText("@string/btn_happy");
            }
        });

        Button angryButton = (Button) findViewById(R.id.angrybuttonMood);
        angryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView activeImage = (TextView) findViewById(R.id.activeMood);
                activeImage.setText("@string/btn_angry");
            }
        });

        Button sadButton = (Button) findViewById(R.id.sadbuttonMood);
        sadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView activeImage = (TextView) findViewById(R.id.activeMood);
                activeImage.setText("@string/btn_sad");
            }
        });

        Button phlegmaticButton = (Button) findViewById(R.id.phlegmaticbuttonMood);
        phlegmaticButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView activeImage = (TextView) findViewById(R.id.activeMood);
                activeImage.setText("@string/btn_phlegmatic");
            }
        });

        Button comfortButton = (Button) findViewById(R.id.comfortbuttonMood);
        comfortButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView activeImage = (TextView) findViewById(R.id.activeMood);
                activeImage.setText("@string/btn_comfort");
            }
        });
    }
}
