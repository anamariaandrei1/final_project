package com.example.travelj2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.travelj2.fragments.NavigationDrawerActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "SignInActivity";
    private FirebaseAuth mAuth;
    EditText emailTextInput;
    EditText passwordTextInput;
    Button signInButton;
    TextView forgotPassword;
    TextView register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailTextInput = findViewById(R.id.textEmailAddress);
        passwordTextInput = findViewById(R.id.textPassword);
        signInButton = findViewById(R.id.loginButton);
        forgotPassword = findViewById(R.id.forgotPassword);
        register = findViewById(R.id.register);
        mAuth = FirebaseAuth.getInstance();

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (emailTextInput.getText().toString().contentEquals("")) {
                    Toast.makeText(getApplicationContext(), "Please enter email", Toast.LENGTH_LONG).show();

                } else if (!Patterns.EMAIL_ADDRESS.matcher(emailTextInput.getText().toString()).matches()) {
                    Toast.makeText(getApplicationContext(), "Please enter a valid email", Toast.LENGTH_LONG).show();

                } else if (passwordTextInput.getText().toString().contentEquals("")) {
                    Toast.makeText(getApplicationContext(), "Please enter password", Toast.LENGTH_LONG).show();

                } else {
                    mAuth.signInWithEmailAndPassword(emailTextInput.getText().toString(), passwordTextInput.getText().toString())
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d(TAG, "signInWithEmail:success");

                                        FirebaseUser user = mAuth.getCurrentUser();
                                        if (user != null) {
                                            if (user.isEmailVerified()) {
                                                System.out.println("Email Verified : " + user.isEmailVerified());
                                                Intent intent = new Intent(LoginActivity.this, NavigationDrawerActivity.class);
                                                startActivity(intent);
                                                LoginActivity.this.finish();
                                            }
                                        }

                                    } else {
                                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                                        Toast.makeText(LoginActivity.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });
                }
            }
        });


        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent forgotPasswordActivity = new Intent(LoginActivity.this, ResetPasswordActivity.class);
                startActivity(forgotPasswordActivity);
                LoginActivity.this.finish();

            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent registerActivity = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(registerActivity);
                LoginActivity.this.finish();

            }
        });

    }
}
