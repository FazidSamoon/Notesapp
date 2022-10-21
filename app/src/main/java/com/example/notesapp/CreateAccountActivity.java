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

public class CreateAccountActivity extends AppCompatActivity {
    MaterialButton button;
    EditText email, password, confirmPassword;
    ProgressBar progressBar;
    TextView textViewLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creaate_account);

        email = findViewById(R.id.email_edit_text);
        password = findViewById(R.id.password_edit_text);
        confirmPassword = findViewById(R.id.confirm_password_edit_text);
        button = findViewById(R.id.signup_button);
        textViewLogin = findViewById(R.id.loginTextViewButton);
        progressBar = findViewById(R.id.signup_progressbar);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailEditText = email.getText().toString();
                String passwordEditText = password.getText().toString();
                String confirmPasswordEditText = confirmPassword.getText().toString();

                boolean isValidated = validateData(emailEditText, passwordEditText, confirmPasswordEditText);

                if(!isValidated) {
                    return;
                }else{
                    createAccount(emailEditText, passwordEditText);
                }
            }
        });

        textViewLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });
    }


    public void createAccount(String email, String password){
        changeInProgress(true);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(CreateAccountActivity.this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isComplete()) {
                            //success
                            Toast toast = Toast.makeText(getApplicationContext(), "Account creation successful, check email to verify", Toast.LENGTH_LONG);
                            toast.show();
                            firebaseAuth.getCurrentUser().sendEmailVerification();
                            firebaseAuth.signOut();
                            finish();
                        } else {
                            //failure message
                            Toast toast = Toast.makeText(getApplicationContext(), task.getException().getLocalizedMessage(), Toast.LENGTH_LONG);
                            toast.show();
                        }
                    }
                });
    }

    public void changeInProgress(boolean progressStatus) {
        if (progressStatus) {
            progressBar.setVisibility(View.VISIBLE);
            button.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.GONE);
            button.setVisibility(View.VISIBLE);
        }
    }

    boolean validateData(String emailAddress, String passwordText, String confirmPasswordText ) {
        if (!Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches()) {
            email.setError("Email address is invalid");
            return false;
        }
        if (passwordText.length() < 6) {
            password.setError("Password should have 6 characters");
            return false;
        }
        if (!passwordText.equals(confirmPasswordText)) {
            confirmPassword.setError("Password not matched");
            return false;
        }

        return true;
    }
}