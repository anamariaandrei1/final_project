package com.example.travelj2;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AlertDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;


public class ResetPasswordActivity extends AppCompatActivity {

    private static final String TAG = "ForgotPasswordActivity";
    public FirebaseAuth mAuth;
    Button resetPasswordButton;
    EditText emailTextInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);


        emailTextInput = findViewById(R.id.textEmailAddress);
        resetPasswordButton = findViewById(R.id.resetPassword);
        mAuth = FirebaseAuth.getInstance();
        resetPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.sendPasswordResetEmail(emailTextInput.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) Log.d(TAG, "Email sent.");

                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                        ResetPasswordActivity.this);

                                alertDialogBuilder.setTitle("Reset Password");

                                alertDialogBuilder
                                        .setMessage("A reset password link had been sent to your e-mail address.")
                                        .setCancelable(false)
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {

                                                Intent signInIntent = new Intent(ResetPasswordActivity.this, LoginActivity.class);
                                                ResetPasswordActivity.this.finish();
                                                startActivity(signInIntent);
                                            }
                                        });

                                AlertDialog alertDialog = alertDialogBuilder.create();
                                alertDialog.show();


                            }
                        });


            }
        });


    }
}
