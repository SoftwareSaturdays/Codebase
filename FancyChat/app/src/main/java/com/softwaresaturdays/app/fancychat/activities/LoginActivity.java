package com.softwaresaturdays.app.fancychat.activities;

import android.os.Bundle;
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
        // TODO find and reference the id of mSignIn button
        final EditText etName = findViewById(R.id.etName);
        etName.requestFocus();

        // Set a listener to run code whenever the sign in button is clicker
        mSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processSignInInfo(etName.getText().toString());
            }
        });
    }

    // TODO implement the function. Take in entered name and check if it is empty
    private void processSignInInfo(String username) {
        if (username.isEmpty()) {
            // TODO if name is empty, show a Snackbar to prompt user to enter a name
        } else {
            // TODO Else, create an intent to start the ChatActivity and pass over the name value
            // HINT: chatIntent.putExtra("name", username);
        }
    }
}
