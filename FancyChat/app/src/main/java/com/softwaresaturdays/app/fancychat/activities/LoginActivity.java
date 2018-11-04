package com.softwaresaturdays.app.fancychat.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.softwaresaturdays.app.fancychat.R;

public class LoginActivity extends AppCompatActivity {

    public static final String TAG = "LOGIN_ACTIVITY";
    private Button mSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Reference UI elements
        mSignIn = findViewById(R.id.sign_in_button);
        final EditText etName = findViewById(R.id.etName);
        etName.requestFocus();

        // Set a listener to run code whenever the sign in button is clicker
        mSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etName.getText().toString().isEmpty()) {
                    // Show notification if no name has been entered
                    Snackbar.make(mSignIn, "Please enter name", Snackbar.LENGTH_SHORT).show();
                } else {
                    // Create an intent and pass in the user's name to the ChatActivity
                    Intent chatIntent = new Intent(LoginActivity.this, ChatActivity.class);
                    chatIntent.putExtra("name", etName.getText().toString());
                    startActivity(chatIntent);
                }
            }
        });
    }
}
