package com.example.listviewdemo;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;


public class LoginActivity extends AppCompatActivity {

    EditText userEmail;
    EditText userPassword;
    ProgressBar progressBar;
    Button userLogin;

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        progressBar = findViewById(R.id.ProgressBar);
        userEmail = findViewById(R.id.UserEmail);
        userPassword = findViewById(R.id.UserPassword);
        userLogin =  findViewById(R.id.BtnUserLogin);

        mAuth = FirebaseAuth.getInstance();
        userLogin.setOnClickListener(v -> mAuth.signInWithEmailAndPassword(userEmail.getText().toString(),
                userPassword.getText().toString())
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        startActivity(new Intent(this, CustomListActivity.class));
                    } else {
                        Toast.makeText(LoginActivity.this,
                                task.getException().getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                }));
    }
}