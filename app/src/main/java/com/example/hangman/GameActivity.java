package com.example.hangman;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.regex.Pattern;


public class GameActivity extends AppCompatActivity {

    Button hint_btn;
    boolean isHighScore = false;
    TextView guess, high_score, hint, score, timer, life;
    long sec, the_high_score;
    short point_per_letter = 2, rightAttempts = 0, the_score = 0, wrongAttempts = 0, max_life = 3, the_life = max_life, time_per_letter = 20;
    char [] dashes;
    CountDownTimer downTimer;
    String randomCountry;
    String level, newText;
    ArrayList<Button> buttons;
    ArrayList <String> Countries, level_1_countries, level_2_countries, level_3_countries, selected_country_list;

//    static final String STATE_SCORE = "player_score";
//    static final String STATE_LEVEL = "player_level";
//    static final String STATE_HINT = "player_hint";
//    static final String STATE_GUESS = "player_guess";
//    static final String STATE_COUNTRY = "player_guess";

//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//        outState.putInt(STATE_SCORE, the_score);
//        outState.putString(STATE_LEVEL, level);
//        outState.putString(STATE_COUNTRY, randomCountry);
//        outState.putString(STATE_HINT, hint.getText().toString());
//        outState.putString(STATE_GUESS, guess.getText().toString());
//        super.onSaveInstanceState(outState);
//    }
//
//    @Override
//    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
//        super.onRestoreInstanceState(savedInstanceState);
//        the_score = savedInstanceState.getInt(STATE_SCORE);
//        score_set();
//        level = savedInstanceState.getString(STATE_LEVEL);
//        randomCountry = savedInstanceState.getString(STATE_COUNTRY);
//        hint.setText(savedInstanceState.getString(STATE_HINT));
//        guess.setText(savedInstanceState.getString(STATE_GUESS));
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        getSupportActionBar().
                setBackgroundDrawable(new ColorDrawable(Color.parseColor("#000099")));

        init();
        readAsset();
        getSupportActionBar().setTitle("Hangman");
        getSupportActionBar().setSubtitle("Akinyeleib Welcomes you");
        getLevel();
        generate(selected_country_list);
        retrieve_high_score();

    }

    // To pause timer on minimize
    @Override
    protected void onPause() {
        super.onPause();
        Log.w("warn", "Paused");
    }

    // To resume timer on restart
    @Override
    protected void onRestart() {
        super.onRestart();
        Log.w("warn", "Restart");
    }

    //    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        Intent intent = new Intent(this, MainMenu.class);
//        intent.putExtra("check", "pass");
//        startActivity(intent);
//    }

    public void init () {

        life = findViewById(R.id.life);
        timer = findViewById(R.id.timer);
        buttons = new ArrayList<>();
        Countries = new ArrayList<>();
        level_1_countries = new ArrayList<>();
        level_2_countries = new ArrayList<>();
        level_3_countries = new ArrayList<>();
        guess = findViewById(R.id.guess);
        score = findViewById(R.id.score);
        high_score = findViewById(R.id.high_score);

//        hint_btn = findViewById(R.id.hint_btn);
        hint = findViewById(R.id.hint);

        loadTimer();
        life.setText(String.valueOf(the_life));
        score.setText(String.valueOf(the_score));

    }

    private void loadTimer() {

        downTimer = new CountDownTimer(time_per_letter * 1000, 1100) {
            @Override
            public void onTick(long millisUntilFinished) {
                NumberFormat numberFormat = new DecimalFormat("00");
                sec = (millisUntilFinished / 1000) % 60;
                timer.setText(numberFormat.format(sec));

                if (sec > 10)
                    timer.setTextColor(Color.GREEN);
                else if (sec > 5)
                    timer.setTextColor(Color.YELLOW);
                else
                    timer.setTextColor(Color.RED);

            }

            @Override
            public void onFinish() {
                the_life--;
                if (the_life >= 0) {
                    life.setText(String.valueOf(the_life));
                    loadTime();
                }

                else
                    reset_game();
            }
        };
    }

    private void loadTime() {
        downTimer.cancel();
        downTimer.start();
    }

    @NonNull
    private void getLevel() {
        level = HelperClass.readFromFile("level.txt", getApplicationContext());

        switch (level) {
            default:
                selected_country_list = Countries;
                break;
            case "Beginner":
                selected_country_list = level_1_countries;
                break;
            case "Master":
                selected_country_list = level_2_countries;
                break;
            case "Legendary":
                selected_country_list = level_3_countries;
                break;
        }
        Toast.makeText(this, "Level: " + level, Toast.LENGTH_SHORT).show();
    }

    private void dialog () {
        String text = "Failed, country is: " + randomCountry + "\n\nYou correctly guessed " + rightAttempts
                + " countries, score is " + the_score +
                ".\n\nThanks for playing.";
        AlertDialog.Builder dialog = new AlertDialog.Builder(GameActivity.this);
        dialog.setTitle("Hangman");
        dialog.setMessage(text);
        dialog.setCancelable(false);
        dialog.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                generate(selected_country_list);
            }
        });
        dialog.create().show();
    }

    private void readAsset() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(getAssets().open("countries.txt"), "UTF-8"))) {

            String mLine;
            while ((mLine = reader.readLine()) != null) {
                Countries.add(mLine);
                if (mLine.length() <= 8)
                    level_1_countries.add(mLine);
                else if (mLine.length() <= 14)
                    level_2_countries.add(mLine);
                else
                    level_3_countries.add(mLine);
            }
        } catch (IOException e) {
            Log.e("err", "Error: " + e.getMessage());
        }
    }

    private void relaunch() {
        Countries.clear();
        level_1_countries.clear();
        level_2_countries.clear();
        level_3_countries.clear();
        readAsset();
    }

    private void retrieve_high_score() {
        String retrieved_score = HelperClass.readFromFile(level, getApplicationContext());
        if (retrieved_score.length() == 0) {
            HelperClass.writeToFile(0, level, getApplicationContext());
            high_score.setText("0");
        } else {
            high_score.setText(retrieved_score);
        }
    }

    private void update_high_score() {
        the_high_score = Integer.parseInt(HelperClass.readFromFile(level, getApplicationContext()));

        if (the_score > the_high_score) {
            HelperClass.writeToFile(the_score, level, getApplicationContext());
            this.high_score.setText(String.valueOf(the_score));
        }

    }

    public void btn_click (View view) {
        Button btn = (Button) view;
        btn.setEnabled(false);
        buttons.add(btn);
        String clicked = btn.getText().toString();

        char letter = clicked.charAt(0);
        if (randomCountry.toUpperCase().contains(clicked)) {
            btn.setBackgroundColor(Color.GREEN);
            for (int i = 0; i < randomCountry.length(); i++) {

                if (letter == randomCountry.charAt(i)) {
                    dashes[i] = letter;
                    the_score += point_per_letter;
                }

            }

            score.setText(String.valueOf(the_score));
            if (the_score > the_high_score) {
                HelperClass.writeToFile(the_score, level, getApplicationContext());
                this.high_score.setText(String.valueOf(the_score));
            }

            loadTime();
            fillLetters();

        } else {
            point_per_letter = 2;
            btn.setBackgroundColor(Color.RED);
            wrongAttempts++;
            if (wrongAttempts > 5) {
                reset_game();
            }
        }
        check_complete();
    }

    private void check_complete() {
        if (newText.equals(randomCountry)) {
            rightAttempts++;

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (wrongAttempts == 0) {
                        the_life = the_life < max_life ? the_life++ : max_life;
                        life.setText(String.valueOf(the_life));
                        point_per_letter++;
                    }
                    generate(selected_country_list);
                }
            }, 1000);
        }

    }

    public void generate(ArrayList <String> Countries) {

        Collections.shuffle(Countries);
        wrongAttempts = 0;

        randomCountry = Countries.get(0);
        Countries.remove(randomCountry);

        randomCountry = randomCountry.toUpperCase();
        dashes = new char[randomCountry.length()];
        for (int i = 0; i < randomCountry.length(); i++) {
            char letter = randomCountry.charAt(i);
            if (Pattern.matches("[A-Z]", String.valueOf(letter)))
                dashes[i] = '-';
            else
                dashes[i] = letter;
        }

        for (Button btn: buttons) {
            btn.setEnabled(true);
            btn.setBackgroundColor(Color.parseColor("#eb8514"));
            btn.setTextColor(Color.BLACK);
        }
        buttons.clear();

        fillLetters();
        loadTime();

    }

    private void fill_random() {

        int rd_index = new Random().nextInt(randomCountry.length());
        dashes[rd_index] = randomCountry.charAt(rd_index);
        fillLetters();

    }

    public void fillLetters() {
        newText = "";
        for (int i = 0; i < randomCountry.length(); i++) {
            String now = dashes[i] + "";
            newText += now;
        }
        guess.setText(newText);
    }

    public void reset_game() {
        downTimer.cancel();
        dialog();
        update_high_score();
        rightAttempts = 0;
        the_score = 0;
        score.setText(String.valueOf(the_score));
        the_life = max_life;
        life.setText(String.valueOf(max_life));
        hint.setText(String.valueOf(3));
        relaunch();
    }

    public void hint_btn_click(View view) {
       int current_hints = Integer.parseInt(hint.getText().toString());
       String old_word = (String) guess.getText();
       if (current_hints > 0) {
           while (old_word.contentEquals(guess.getText()))
               fill_random();
           current_hints -= 1;
           hint.setText(String.valueOf(current_hints));
           check_complete();
       }
       else
           Toast.makeText(this, "Out of hints", Toast.LENGTH_SHORT).show();

    }

}
