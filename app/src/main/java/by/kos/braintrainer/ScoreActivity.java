package by.kos.braintrainer;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;

import by.kos.braintrainer.databinding.ActivityScoreBinding;

public class ScoreActivity extends AppCompatActivity {
    private ActivityScoreBinding binding;

    @SuppressLint("StringFormatMatches")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityScoreBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        int maxResult = preferences.getInt("max", 0);
        Intent intent = getIntent();
        if (intent.hasExtra("ScoreCount") && intent.hasExtra("ScoreRight")) {
            int countOfQuestions = intent.getIntExtra("ScoreCount", 0);
            int countOfRightAnswers = intent.getIntExtra("ScoreRight", 0);
            binding.tvResult.setText(String.format(getString(R.string.final_text), countOfRightAnswers, countOfQuestions, maxResult));
        }

        binding.btnStartNewGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentNewGame = new Intent(view.getContext(), MainActivity.class);
                startActivity(intentNewGame);
            }
        });
    }
}