package com.example.hangman;

import  androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class HelpMenu extends AppCompatActivity {

    TextView about, high_score, level;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_menu);
        getSupportActionBar().hide();

        init();
        loadTexts();
    }

    private void init() {
        about = findViewById(R.id.about);
        high_score = findViewById(R.id.high_score);
        level = findViewById(R.id.level);
    }

    private void loadTexts() {
        about.setText(readAsset("about.txt"));
        high_score.setText(readAsset("high_score.txt"));
        level.setText(readAsset("level.txt"));
    }

    public String readAsset(String fileName) {
        StringBuilder builder = new StringBuilder();
        String text = "";
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(getAssets().open(fileName), "UTF-8"))) {
            String mLine;
            while ((mLine = reader.readLine()) != null) {
                builder.append(mLine);
                text += (mLine + "\n");
            }
        } catch (IOException e) {
            Log.e("err", "Error: " + e.getMessage());
        }
//        return builder.toString();
        return text;
    }

}