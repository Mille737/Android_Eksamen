package com.example.listviewdemo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class  MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    ProgressBar progressBar;
    EditText email;
    EditText password;
    Button signUp;
    Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();

        progressBar = findViewById(R.id.progressBar);
        email = findViewById(R.id.email);
        password = findViewById(R.id.Password);
        signUp = findViewById(R.id.btnSignUp);
        login = findViewById(R.id.btnLogin);

        signUp.setOnClickListener(v -> {
            progressBar.setVisibility(View.VISIBLE);
            mAuth.createUserWithEmailAndPassword(email.getText().toString(),
                    password.getText().toString())
                    .addOnCompleteListener(task -> {
                        progressBar.setVisibility(View.GONE );
                        if(task.isSuccessful()) {
                            Toast.makeText(MainActivity.this, "Registered Successfully",
                                    Toast.LENGTH_LONG).show();
                            email.setText("");
                            password.setText("");
                        } else {
                            Toast.makeText(MainActivity.this, task.getException().getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });
        });
        login.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, LoginActivity.class)));
    }
}
