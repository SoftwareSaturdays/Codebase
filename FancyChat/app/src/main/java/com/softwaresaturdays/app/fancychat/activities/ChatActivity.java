package com.softwaresaturdays.app.fancychat.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.softwaresaturdays.app.fancychat.R;
import com.softwaresaturdays.app.fancychat.adapters.ChatAdapter;
import com.softwaresaturdays.app.fancychat.models.Message;
import com.softwaresaturdays.app.fancychat.models.TextMessage;

import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView mRvChat;
    private ArrayList<Message> mMessages = new ArrayList<>();
    private ChatAdapter mChatAdapter;
    private RoundedImageView mIvProfile;
    private LinearLayoutManager mLayoutManager;
    private EditText mEtTextMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
    }

    // Refresh the recycler view with updated messages
    private void refreshRecyclerView(ArrayList<Message> messages) {
        // Update messages REAL-TIME and update list view
        mMessages = messages;
        mChatAdapter = new ChatAdapter(ChatActivity.this, mMessages);
        if (mRvChat != null) {
            mRvChat.setAdapter(mChatAdapter);
        }
    }

    private void scrollToEnd() {
        mLayoutManager.scrollToPosition(mChatAdapter.getItemCount() - 1);
    }


    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        // Used to make sure the view contents move up when soft keyboard comes up
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        return super.onCreateView(name, context, attrs);
    }


    @Override
    protected void onStart() {
        super.onStart();

        // Link Java code variables to UI elements
        mRvChat = findViewById(R.id.rvChat);
        ImageView ivSend = findViewById(R.id.ivSend);
        mIvProfile = findViewById(R.id.ivProfile);
        mEtTextMessage = findViewById(R.id.etTextMessage);

        final String author = getIntent().getStringExtra("name");

        mIvProfile.setImageResource(R.drawable.ic_account_circle_black_36dp);

        // Set clicklisteners to the activity so all onClick callbacks are together
        ivSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = mEtTextMessage.getText().toString();
                if (text.isEmpty()) {
                    Snackbar.make(mRvChat, "Please enter searchText", Snackbar.LENGTH_SHORT).show();
                } else {
                    Message message = new TextMessage(text, author);
                    mMessages.add(message);
                    refreshRecyclerView(mMessages);
                }
                mEtTextMessage.setText("");
            }
        });

        setupChatList();
    }

    private void setupChatList() {
        mChatAdapter = new ChatAdapter(this, mMessages);

        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        mRvChat.setAdapter(mChatAdapter);
        mRvChat.setLayoutManager(mLayoutManager);
        scrollToEnd();
    }
}
