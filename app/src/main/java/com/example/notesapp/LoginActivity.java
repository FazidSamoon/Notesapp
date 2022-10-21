package com.example.notesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    EditText email, password;
    MaterialButton button;
    TextView createAccountButton;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.email_edit_text);
        password = findViewById(R.id.password_edit_text);
        button = findViewById(R.id.login_button);
        createAccountButton = findViewById(R.id.createAccountTextViewButton);
        progressBar = findViewById(R.id.login_progressbar);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeInProgress(true);
                String emailVal = email.getText().toString();
                String passwordVal = password.getText().toString();

                boolean res = validateData(emailVal, passwordVal);
                if (!res) {
                    return;
                }

                loginUserWIthFirebase(emailVal, passwordVal);
            }
        });

        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CreateAccountActivity.class);
                startActivity(intent);
            }
        });
    }

    void changeInProgress(boolean state) {
        if (state) {
            progressBar.setVisibility(View.VISIBLE);
            button.setVisibility(View.GONE);
        } else {
            button.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
        }
    }

    boolean validateData(String emailInput, String passwordInput) {
        if(!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            email.setError("Email is invalid");
            return false;
        }
        if (passwordInput.length() < 6) {
            password.setError("Password is invalid");
            return false;
        }
        return true;
    }

    void loginUserWIthFirebase(String email, String password) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        changeInProgress(true);
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                changeInProgress(false);
                if (task.isSuccessful()) {
                    if (firebaseAuth.getCurrentUser().isEmailVerified()) {
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();
                    } else {
                        Utilities.createToast(getApplicationContext(),"Please verify your email address");
                    }
                } else {
                    Utilities.createToast(getApplicationContext(), task.getException().getLocalizedMessage());
                }
            }
        });
    }
}