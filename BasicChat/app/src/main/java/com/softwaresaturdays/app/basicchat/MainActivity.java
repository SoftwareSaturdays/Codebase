package com.softwaresaturdays.app.basicchat;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnSend = findViewById(R.id.btnSend);
        final EditText etMessage = findViewById(R.id.etMessage);

        // TODO Create a TextView object called tvMessage and find it by id tvMessage

        // A listener is an interface that listens to events and automatically calls a function
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Following statements executed when the button is pressed
                String enteredText = etMessage.getText().toString();

                // TODO Check to see if the text entered by the user is not empty

                // TODO Set the tvMessage to display the text entered by the user
            }
        });

    }
}
