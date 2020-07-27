package com.example.travelj2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;


public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = "SignUpActivity";
    public FirebaseAuth mAuth;
    Button signUpButton;
    EditText signUpEmailTextInput;
    EditText signUpPasswordTextInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        mAuth = FirebaseAuth.getInstance();

        signUpEmailTextInput = findViewById(R.id.textEmailAddress);
        signUpPasswordTextInput = findViewById(R.id.textPassword);
        signUpButton = findViewById(R.id.registerButton);


        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (signUpEmailTextInput.getText().toString().contentEquals("")) {
                    Toast.makeText(getApplicationContext(), "Please enter email", Toast.LENGTH_LONG).show();
                } else if (signUpPasswordTextInput.getText().toString().contentEquals("")) {
                    Toast.makeText(getApplicationContext(), "Please enter password!", Toast.LENGTH_LONG).show();
                } else {


                    mAuth.createUserWithEmailAndPassword(signUpEmailTextInput.getText().toString(), signUpPasswordTextInput.getText().toString()).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "createUserWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                String email, uid;
                                if (user != null) {
                                    email = user.getEmail();
                                    uid = user.getUid();
                                    HashMap<Object, String> hashMap = new HashMap<>();
                                    hashMap.put("email", email);
                                    hashMap.put("uid", uid);
                                    hashMap.put("name", "");
                                    hashMap.put("phone", "");
                                    hashMap.put("image", "");
                                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                                    DatabaseReference reference = database.getReference("Users");
                                    reference.child(uid).setValue(hashMap);
                                    user.sendEmailVerification()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Log.d(TAG, "Email sent.");

                                                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                                                RegisterActivity.this);
                                                        alertDialogBuilder.setTitle("Please verify your e-mail address!");
                                                        alertDialogBuilder
                                                                .setMessage("A verification e-mail had been sent to your e-mail address.")
                                                                .setCancelable(false)
                                                                .setPositiveButton("Sign In", new DialogInterface.OnClickListener() {
                                                                    public void onClick(DialogInterface dialog, int id) {

                                                                        Intent signInIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                                                                        RegisterActivity.this.finish();
                                                                        startActivity(signInIntent);
                                                                    }
                                                                });

                                                        // create alert dialog
                                                        AlertDialog alertDialog = alertDialogBuilder.create();

                                                        // show it
                                                        alertDialog.show();


                                                    }
                                                }
                                            });
                                }

                            } else {
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();

                            }

                        }
                    });

                }

            }
        });


    }
}
