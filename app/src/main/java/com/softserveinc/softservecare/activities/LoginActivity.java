package com.softserveinc.softservecare.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.softserveinc.softservecare.Constants;
import com.softserveinc.softservecare.R;
import com.softserveinc.softservecare.api.FirebaseApi;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText emailEditText = (EditText) findViewById(R.id.emailEditText);
        final EditText passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        Button loginButton = (Button) findViewById(R.id.loginButton);
        Button signUpButton = (Button) findViewById(R.id.signUpButton);

        final Firebase firebase = new Firebase(Constants.FIREBASE_URL);

        if (signUpButton != null) {
            signUpButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                    startActivity(intent);
                }
            });
        }

        if (loginButton != null) {
            loginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final String email = emailEditText.getText().toString().trim();
                    String password = passwordEditText.getText().toString().trim();

                    if (email.isEmpty() || password.isEmpty()) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                        builder.setMessage(R.string.login_error_message)
                                .setTitle(R.string.login_error_title)
                                .setPositiveButton(android.R.string.ok, null);
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                    else {
                        firebase.authWithPassword(email, password, new Firebase.AuthResultHandler() {
                            @Override
                            public void onAuthenticated(AuthData authData) {
                                FirebaseApi.getInstance().initFirebase();

                                Firebase user = firebase.child("users").child(authData.getUid());
                                user.child("email").setValue(email);

                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }

                            @Override
                            public void onAuthenticationError(FirebaseError firebaseError) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                builder.setMessage(firebaseError.getMessage())
                                        .setTitle(R.string.login_error_title)
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
