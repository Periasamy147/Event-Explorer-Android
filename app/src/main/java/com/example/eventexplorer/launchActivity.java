package com.example.eventexplorer;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.airbnb.lottie.LottieAnimationView;

public class launchActivity extends AppCompatActivity {

    LottieAnimationView lottie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_launch);

        lottie = findViewById(R.id.lottieAnimationView2);
        lottie.animate();
        lottie.playAnimation();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Start the sign-up activity
                Intent i = new Intent(getApplicationContext(), loginActivity.class);
                startActivity(i);

                // Finish the launch activity
                finish();
            }
        }, 1000);
    }
}