package com.example.eventexplorer;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class signUpActivity extends AppCompatActivity {

    EditText mail, pass, Cpass;
    Button signUp;
    ProgressBar progressBar;
    TextView toLogin;
    FirebaseAuth auth;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = auth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(getApplicationContext(), profileRegisterActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private static final String TAG = "SignUpActivity"; // Added for logging purposes

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        auth = FirebaseAuth.getInstance();
        mail = findViewById(R.id.editTextEmailOrNumber);
        pass = findViewById(R.id.editTextPassword);
        signUp = findViewById(R.id.buttonSignUp);
        progressBar = findViewById(R.id.progressBar);
        toLogin = findViewById(R.id.toLogin);
        Cpass = findViewById(R.id.editTextConfirmPassword);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                String email, password, confirmPassword;
                email = String.valueOf(mail.getText());
                password = String.valueOf(pass.getText());
                confirmPassword = String.valueOf(Cpass.getText());

                if (TextUtils.isEmpty(email)){
                    Toast.makeText(signUpActivity.this, "Enter Email.", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    return;
                }

                if (TextUtils.isEmpty(password)){
                    Toast.makeText(signUpActivity.this, "Enter Password", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    return;
                }

                if (!password.equals(confirmPassword)) {
                    Toast.makeText(signUpActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    return;
                }

                auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(signUpActivity.this, task -> {
                            progressBar.setVisibility(View.GONE);
                            if (task.isSuccessful()) {
                                FirebaseUser user = auth.getCurrentUser();
                                if (user != null) {
                                    // Send email verification
                                    user.sendEmailVerification()
                                            .addOnCompleteListener(task1 -> {
                                                if (task1.isSuccessful()) {
                                                    Toast.makeText(signUpActivity.this, "Verification email sent to " + email, Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Log.e(TAG, "sendEmailVerification", task1.getException());
                                                    Toast.makeText(signUpActivity.this, "Failed to send verification email.", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }
                            } else {
                                Toast.makeText(signUpActivity.this, "Authentication failed: " + task.getException().getMessage(),
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });



        // Set onClickListener for "Sign up" TextView to navigate to login activity
        toLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(signUpActivity.this, loginActivity.class);
                startActivity(intent);
            }
        });
    }
}
