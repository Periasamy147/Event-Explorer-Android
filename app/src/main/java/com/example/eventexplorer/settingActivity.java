package com.example.eventexplorer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class settingActivity extends AppCompatActivity {

    FirebaseAuth auth;
    Button logout;
    FirebaseUser user;
    TextView textView; // Declare textView here

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        auth = FirebaseAuth.getInstance();
        logout = findViewById(R.id.logoutButton);
        user = auth.getCurrentUser();

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                // Add your code to navigate to the login screen here
                Intent intent = new Intent(settingActivity.this, loginActivity.class);
                // Clear the activity stack and start the login activity as a new task
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

    }
}
