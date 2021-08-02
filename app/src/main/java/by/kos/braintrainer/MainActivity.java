package by.kos.braintrainer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

import by.kos.braintrainer.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private String question;
    private int rightAnswer;
    private int rightAnswerPosition;
    private int operator;
    private int min = 5;
    private int max = 30;
    private ArrayList<Button> options = new ArrayList<>();
    private Toast answer_toast;
    private int countOfQuestions = 0;
    private int countOfRightAnswers;
    private boolean isGameOver = false;
    private long startTime = 50000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        if (savedInstanceState != null) {
            startTime = savedInstanceState.getLong("currentTime");
            countOfQuestions = savedInstanceState.getInt("ScoreCount");
            countOfRightAnswers = savedInstanceState.getInt("ScoreRight");
        }
        Intent intentStartTime = getIntent();
        if (intentStartTime.hasExtra("startTime")) {
            startTime = intentStartTime.getLongExtra("startTime", 50000);
        }
        buttonsToArray();
        getDataScreen();

        binding.btnAnswer0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(v);
            }
        });

        binding.btnAnswer1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(v);
            }
        });

        binding.btnAnswer2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(v);
            }
        });

        binding.btnAnswer3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(v);
            }
        });

        CountDownTimer timer = new CountDownTimer(startTime, 1000) {
            @Override

            public void onTick(long millisUntilFinished) {
                if (millisUntilFinished <= 6000) {
                    binding.txtTimer.setTextColor(getResources().getColor(R.color.red));
                }
                binding.txtTimer.setText(getTime(millisUntilFinished));
                startTime = millisUntilFinished;
            }

            @Override
            public void onFinish() {
                this.cancel();
                isGameOver = true;
                if (binding.lottieAnim != null) {
                    binding.lottieAnim.setProgress(0.0f);
                    binding.lottieAnim.pauseAnimation();
                }
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                int max = preferences.getInt("max", 0);
                if (countOfRightAnswers >= max) {
                    preferences.edit().putInt("max", countOfRightAnswers).apply();
                }
                Intent intent = new Intent(MainActivity.this, ScoreActivity.class);
                intent.putExtra("ScoreCount", countOfQuestions);
                intent.putExtra("ScoreRight", countOfRightAnswers);
                startActivity(intent);
            }
        };
        timer.start();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong("currentTime", startTime);
        outState.putInt("ScoreCount", countOfQuestions);
        outState.putInt("ScoreRight", countOfRightAnswers);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (answer_toast != null) {
            answer_toast.cancel();
        }
    }

    private String getTime(long millis) {
        int seconds = (int) millis / 1000;
        int minutes = seconds / 60;
        seconds = seconds % 60;
        return String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
    }

    private void generateQuestion() {
        int a = (int) (Math.random() * (max - min + 1) + min);
        int b = (int) (Math.random() * (max - min + 1) + min);
        operator = (int) (Math.random() * (3 - 1 + 1) + 1);
        if (operator == 1) {
            question = String.format("%s + %s", a, b);
            rightAnswer = a + b;
        }
        if (operator == 2) {
            question = String.format("%s - %s", a, b);
            rightAnswer = a - b;
        }

        if (operator == 3) {
            question = String.format("%s * %s", a, b);
            rightAnswer = a * b;
        }
        binding.txtQuestion.setText(question);
        rightAnswerPosition = (int) (Math.random() * 4);
    }

    private int generateWrongAnswer() {
        int result;
        do {
            result = (int) (Math.random() * max * 2 + 1) - (max - min);
        } while (result == rightAnswer);
        return result;
    }

    private void getDataScreen() {
        generateQuestion();
        for (int i = 0; i < options.size(); i++) {
            if (i == rightAnswerPosition) {
                options.get(i).setText(String.format("%s", rightAnswer));
            } else {
                options.get(i).setText(String.format("%s", generateWrongAnswer()));
            }
            String score = String.format("%s/%s", countOfRightAnswers, countOfQuestions);
            binding.txtScore.setText(score);
        }
    }

    private void buttonsToArray() {
        options.add(binding.btnAnswer0);
        options.add(binding.btnAnswer1);
        options.add(binding.btnAnswer2);
        options.add(binding.btnAnswer3);
    }

    @SuppressLint("StringFormatMatches")
    private void checkAnswer(View v) {
        if (answer_toast != null) {
            answer_toast.cancel();
        }
        Button button = (Button) v;
        if (!isGameOver) {
            int answer = Integer.parseInt(button.getText().toString());
            if (rightAnswer == answer) {
                countOfRightAnswers++;
                answer_toast = Toast.makeText(getApplicationContext(), R.string.toast_right_answer, Toast.LENGTH_SHORT);
            } else {
                answer_toast = Toast.makeText(getApplicationContext(), String.format(getString(R.string.toast_false_answer), rightAnswer), Toast.LENGTH_SHORT);
            }
            countOfQuestions++;
            answer_toast.show();
            getDataScreen();
        }
    }

}