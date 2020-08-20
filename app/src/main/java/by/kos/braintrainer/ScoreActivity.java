package by.kos.braintrainer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ScoreActivity extends AppCompatActivity {

    private TextView textViewResult;
    private Button buttonNewGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        Intent intent = getIntent();
        textViewResult = findViewById(R.id.textViewResult);
        buttonNewGame = findViewById(R.id.buttonNewGame);
        SharedPreferences maxScore = PreferenceManager.getDefaultSharedPreferences(this);
        int max = maxScore.getInt("max", 0);
        if(intent != null && intent.hasExtra("score")) {
            int result = intent.getIntExtra("score", 0);
            String score = String.format("Ваш результат: %s правильных ответов.\n\nМаксимальное количество правильных ответов: %s",
                    result, max);
            textViewResult.setText(score);
        }
    }

    public void onClickStartNewGame(View view) {
        Intent newGame = new Intent(this, MainActivity.class);
        startActivity(newGame);
    }
}