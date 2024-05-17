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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class loginActivity extends AppCompatActivity {

    EditText mailOrName, password;
    TextView sign, resetPasssword;
    Button login;
    FirebaseAuth auth;
    ProgressBar progressBar;
    private static final String TAG = "LoginActivity"; // Added for logging purposes

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            Intent intent = new Intent(getApplicationContext(), mainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mailOrName = findViewById(R.id.id_username);
        password = findViewById(R.id.id_password);
        login = findViewById(R.id.id_login);
        sign = findViewById(R.id.toSignUp);
        progressBar = findViewById(R.id.progressBar);
        auth = FirebaseAuth.getInstance();
        resetPasssword = findViewById(R.id.resetPass);

        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(loginActivity.this, signUpActivity.class);
                startActivity(intent);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                String input, pass;
                input = mailOrName.getText().toString();
                pass = password.getText().toString();

                if (TextUtils.isEmpty(input)) {
                    Toast.makeText(loginActivity.this, "Enter Email or Name.", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    return;
                }

                if (TextUtils.isEmpty(pass)) {
                    Toast.makeText(loginActivity.this, "Enter Password", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    return;
                }

                // Check if input is an email or name
                if (android.util.Patterns.EMAIL_ADDRESS.matcher(input).matches()) {
                    loginUser(input, pass);
                } else {
                    findEmailByNameAndLogin(input, pass);
                }
            }
        });

        resetPasssword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(loginActivity.this, resetPasswordActivity.class);
                startActivity(intent);
            }
        });
    }

    private void loginUser(String email, String pass) {
        auth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(loginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            Toast.makeText(loginActivity.this, "Login Successful.", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), mainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidUserException) {
                                Toast.makeText(loginActivity.this, "User not found.", Toast.LENGTH_SHORT).show();
                            } else if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(loginActivity.this, "Invalid password.", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(loginActivity.this, "Authentication failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });

    }

    private void findEmailByNameAndLogin(String name, String pass) {
        FirebaseDatabase.getInstance().getReference("User Data")
                .orderByChild("name")
                .equalTo(name)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                                String email = userSnapshot.child("email").getValue(String.class);
                                loginUser(email, pass);
                                return;
                            }
                        } else {
                            Toast.makeText(loginActivity.this, "User not found.", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(loginActivity.this, "Database error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }
                });
    }
}

