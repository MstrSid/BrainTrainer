package by.kos.braintrainer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private TextView textViewQuestion;
    private TextView textViewTimer;
    private TextView textViewAnswersCount;
    private TextView textViewAnswer0;
    private TextView textViewAnswer1;
    private TextView textViewAnswer2;
    private TextView textViewAnswer3;
    private ArrayList<TextView> answers;
    private boolean isOver = false;

    private String question;
    private int rightAnswer;
    private int rightAnswerPosition;
    private boolean isPositive;
    private int min = 1;
    private int max = 100;
    private int countOfQuestions = 0;
    private int scoreRightAnswers = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        answers = new ArrayList<>();
        textViewAnswer0 = findViewById(R.id.textViewAnswer0);
        textViewAnswer1 = findViewById(R.id.textViewAnswer1);
        textViewAnswer2 = findViewById(R.id.textViewAnswer2);
        textViewAnswer3 = findViewById(R.id.textViewAnswer3);
        textViewQuestion = findViewById(R.id.textViewQuestion);
        textViewTimer = findViewById(R.id.textViewTimer);
        textViewAnswersCount = findViewById(R.id.textViewAnswersCount);
        answers.add(textViewAnswer0);
        answers.add(textViewAnswer1);
        answers.add(textViewAnswer2);
        answers.add(textViewAnswer3);
        CountDownTimer timer = new CountDownTimer(300000,1000) {
            @Override
            public void onTick(long l) {
                textViewTimer.setText(getTime(l));
                if(l<=10000){
                    textViewTimer.setTextColor(getResources().getColor(android.R.color.holo_red_light));
                }
            }

            @Override
            public void onFinish() {
                isOver = true;
                SharedPreferences maxScore = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                int max = maxScore.getInt("max",0);
                if(scoreRightAnswers >= max){
                    maxScore.edit().putInt("max", scoreRightAnswers).apply();
                }
                Intent finishIntent = new Intent(MainActivity.this, ScoreActivity.class);
                finishIntent.putExtra("score", scoreRightAnswers);
                startActivity(finishIntent);

            }
        };
        playNext();
        timer.start();
    }

    private void playNext(){
        String score;
        generateQuestion();
        for(int i = 0; i<answers.size(); i++){
            if(i == rightAnswerPosition){
                answers.get(i).setText(Integer.toString(rightAnswer));
            } else{
                answers.get(i).setText(Integer.toString(generateWrongAnswers()));
            }
        }
        score = String.format("Верно: %s из %s вопросов", scoreRightAnswers, countOfQuestions);
        textViewAnswersCount.setText(score);
    }

    private void generateQuestion(){
        int a = (int) (Math.random()*(max-min+1)+min);
        int b = (int) (Math.random()*(max-min+1)+min);
        int mark = (int) (Math.random()*2);
        isPositive = mark == 1;
        if(isPositive){
            rightAnswer = a + b;
            question = String.format("%s + %s", a,b);
        } else {
            rightAnswer = a - b;
            question = String.format("%s - %s", a,b);
        }
        rightAnswerPosition = (int) (Math.random()*4);
        textViewQuestion.setText(question);
    }

    private int generateWrongAnswers(){
        int result;
        do {
            result = (int) ((Math.random() * max * 2 + 1) - (max - min));
        } while (result == rightAnswer);
        return result;
    }

    public void onClickAnswer(View view) {
        if (!isOver) {
            TextView textView = (TextView) view;
            int userAnswer = Integer.parseInt(textView.getText().toString());
            if (userAnswer == rightAnswer) {
                scoreRightAnswers++;
                Toast.makeText(this, "Верно", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Ошибка, правильный ответ: " + rightAnswer, Toast.LENGTH_SHORT).show();
            }
            countOfQuestions++;
            playNext();
        }
    }

    private String getTime(long ms){
        int seconds = (int) (ms / 1000);
        int minutes = seconds / 60;
        seconds = seconds % 60;
        return String.format("%02d:%02d", minutes , seconds);
    }
}