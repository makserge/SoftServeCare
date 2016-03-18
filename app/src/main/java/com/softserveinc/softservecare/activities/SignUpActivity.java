package com.softserveinc.softservecare.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.softserveinc.softservecare.Constants;
import com.softserveinc.softservecare.R;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        final EditText passwordEditText = (EditText) findViewById(R.id.passwordField);
        final EditText emailEditText = (EditText)findViewById(R.id.emailField);
        Button signUpButton = (Button)findViewById(R.id.signupButton);

        final Firebase ref = new Firebase(Constants.FIREBASE_URL);

        if (signUpButton != null) {
            signUpButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final String password = passwordEditText.getText().toString().trim();
                    final String email = emailEditText.getText().toString().trim();

                    if (password.isEmpty() || email.isEmpty()) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
                        builder.setMessage(R.string.signup_error_message)
                                .setTitle(R.string.signup_error_title)
                                .setPositiveButton(android.R.string.ok, null);
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                    else {
                        ref.createUser(email, password, new Firebase.ResultHandler() {
                            @Override
                            public void onSuccess() {
                                AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
                                builder.setMessage(R.string.signup_success)
                                        .setPositiveButton(R.string.login_button_label, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(intent);
                                            }
                                        });
                                AlertDialog dialog = builder.create();
                                dialog.show();
                            }

                            @Override
                            public void onError(FirebaseError firebaseError) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
                                builder.setMessage(firebaseError.getMessage())
                                        .setTitle(R.string.signup_error_title)
                                        .setPositiveButton(android.R.string.ok, null);
                                AlertDialog dialog = builder.create();
                                dialog.show();
                            }
                        });
                    }
                }
            });
        }
    }
}
