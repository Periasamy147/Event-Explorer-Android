package com.example.eventexplorer;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

public class resetPasswordActivity extends AppCompatActivity {

    EditText emailEditText;
    Button resetButton;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        emailEditText = findViewById(R.id.id_email);
        resetButton = findViewById(R.id.id_reset);
        auth = FirebaseAuth.getInstance();

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(resetPasswordActivity.this, "Enter your email", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Send password reset email
                auth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(resetPasswordActivity.this, "Password reset email sent", Toast.LENGTH_SHORT).show();
                                    // Redirect to login activity
                                    Intent intent = new Intent(resetPasswordActivity.this, loginActivity.class);
                                    startActivity(intent);
                                    finish(); // Close the current activity to prevent going back to it when pressing back button
                                } else {
                                    // Check if the exception is due to user not found
                                    if (task.getException() instanceof FirebaseAuthInvalidUserException) {
                                        Toast.makeText(resetPasswordActivity.this, "Account is not registered", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(resetPasswordActivity.this, "Failed to send password reset email", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        });
            }
        });


    }
}
