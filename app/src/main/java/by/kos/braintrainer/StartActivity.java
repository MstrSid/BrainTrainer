package by.kos.braintrainer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.slider.Slider;

import by.kos.braintrainer.databinding.ActivityStartBinding;

public class StartActivity extends AppCompatActivity {
    private ActivityStartBinding binding;

    @SuppressLint("StringFormatMatches")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStartBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        binding.textView.setText(String.format(getResources().getString(R.string.choose_minutes_count), (int)binding.slider.getValue()));
        binding.slider.addOnSliderTouchListener(new Slider.OnSliderTouchListener() {
            @Override
            public void onStartTrackingTouch(@NonNull Slider slider) {
            }

            @Override
            public void onStopTrackingTouch(@NonNull Slider slider) {
                binding.textView.setText(String.format(getResources().getString(R.string.choose_minutes_count), (int)binding.slider.getValue()));
            }
        });
        binding.btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long startTime = (long) binding.slider.getValue() * 60000;
                Intent intent = new Intent(StartActivity.this, MainActivity.class);
                intent.putExtra("startTime", startTime);
                startActivity(intent);
            }
        });
    }
}