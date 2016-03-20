package com.softserveinc.softservecare.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.softserveinc.softservecare.Constants;
import com.softserveinc.softservecare.R;

import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        final EditText firstNameEditText = (EditText) findViewById(R.id.firstNameEditText);
        final EditText lastNameEditText = (EditText) findViewById(R.id.lastNameEditText);
        final EditText emailEditText = (EditText) findViewById(R.id.emailEditText);
        final EditText passwordEditText = (EditText) findViewById(R.id.passwordEditText);

        Button signUpButton = (Button) findViewById(R.id.signupButton);

        final Firebase firebase = new Firebase(Constants.FIREBASE_URL);

        if (signUpButton != null) {
            signUpButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final String firstName = firstNameEditText.getText().toString().trim();
                    final String lastName = lastNameEditText.getText().toString().trim();
                    final String password = passwordEditText.getText().toString().trim();
                    final String email = emailEditText.getText().toString().trim();

                    if (firstName.isEmpty() || lastName.isEmpty() || password.isEmpty() || email.isEmpty()) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
                        builder.setMessage(R.string.signup_error_message)
                                .setTitle(R.string.signup_error_title)
                                .setPositiveButton(android.R.string.ok, null);
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    } else {
                        firebase.createUser(email, password, new Firebase.ValueResultHandler<Map<String, Object>>() {
                            @Override
                            public void onSuccess(Map<String, Object> result) {
                                String userId = result.get("uid").toString();

                                Firebase user = firebase.child("users").child(userId);
                                user.child("id").setValue(userId);

                                user.child("first_name").setValue(firstName);
                                user.child("last_name").setValue(lastName);

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
