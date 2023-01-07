package com.example.hangman;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class HighScoreActivity extends AppCompatActivity {

    TextView level_1_score, level_2_score, level_3_score, level_4_score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score);

        init();
        retrieve_high_scores();
    }

    private void init() {
        level_1_score = findViewById(R.id.level1);
        level_2_score = findViewById(R.id.level2);
        level_3_score = findViewById(R.id.level3);
        level_4_score = findViewById(R.id.level4);
    }

    private void retrieve_high_scores() {
        get_high_score("Beginner", level_1_score);
        get_high_score("Master", level_2_score);
        get_high_score("Legendary", level_3_score);
        get_high_score("Random", level_4_score);
    }

    private void get_high_score(String level, TextView view) {
        String score = HelperClass.readFromFile(level, getApplicationContext());
        if (score.equals(""))
            score = "0";
        view.setText(score);
    }

    public void reset_scores(View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm Reset");
        builder.setMessage("Sure to reset all scores?");
        builder.setPositiveButton("Yes", (dialog, which) -> {

            String [] levels = {"Beginner", "Master", "Legendary", "Random"};
            for (String level: levels)
                HelperClass.writeToFile(0, level, getApplicationContext());

            retrieve_high_scores();

            Toast.makeText(this, "Score has been reset", Toast.LENGTH_SHORT).show();

        });

        builder.show();

    }

}