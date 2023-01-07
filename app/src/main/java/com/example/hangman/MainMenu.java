package com.example.hangman;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainMenu extends AppCompatActivity {

    TextView about, continue_text, new_game, level, help_menu, high_scores, quit;
    String game_level;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        getSupportActionBar().hide();

        init();
        continue_text.setVisibility(View.GONE);

        try {
            String result = getIntent().getStringExtra("check");
            if (result.equals("pass"))
                continue_text.setEnabled(true);
        } catch (Exception e) {
            continue_text.setEnabled(false);
        }

    }


    public void init() {

        about = findViewById(R.id.about);
        about.setOnClickListener(v -> {
            Intent intent = new Intent(MainMenu.this, About.class);
            startActivity(intent);
        });

        continue_text = findViewById(R.id.continue_text);
//        continue_text.setOnClickListener(v -> {
//            Intent intent = new Intent(MainMenu.this, GameActivity.class);
//            startActivity(intent);
//        });

        new_game = findViewById(R.id.new_game);
        new_game.setOnClickListener(v -> {
            Intent intent = new Intent(MainMenu.this, GameActivity.class);
            intent.putExtra("level", game_level);
            startActivity(intent);
        });

        level = findViewById(R.id.level);
        level.setOnClickListener(v -> {
            AlertDialog.Builder b = new AlertDialog.Builder(MainMenu.this);
            b.setTitle("Select Level");
            String[] types = {"Random", "Beginner", "Master", "Legendary"};
            b.setItems(types, (dialog, which) -> {
                game_level = types[which];
                HelperClass.writeToFile(game_level, "level.txt", getApplicationContext());
            });
            b.show();
        });

        help_menu = findViewById(R.id.help);
        help_menu.setOnClickListener(v -> {
            Intent intent = new Intent(MainMenu.this, HelpMenu.class);
            startActivity(intent);
        });

        high_scores = findViewById(R.id.high_scores);
        high_scores.setOnClickListener(v -> {
            Intent intent = new Intent(MainMenu.this, HighScoreActivity.class);
            startActivity(intent);
        });

        quit = findViewById(R.id.quit);
        quit.setOnClickListener(v -> new AlertDialog.Builder(MainMenu.this)
                .setTitle("Confirm Exit")
                .setMessage("Are you sure you want to exit?").setCancelable(true)
                .setPositiveButton("Yes", (dialog, which) -> System.exit(0)).show());

    }

}