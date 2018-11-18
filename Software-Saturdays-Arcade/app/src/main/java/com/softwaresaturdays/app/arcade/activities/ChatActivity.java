package com.softwaresaturdays.app.arcade.activities;

import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.messaging.FirebaseMessaging;
import com.makeramen.roundedimageview.RoundedImageView;
import com.softwaresaturdays.app.arcade.MyApplication;
import com.softwaresaturdays.app.arcade.R;
import com.softwaresaturdays.app.arcade.adapters.ChatAdapter;
import com.softwaresaturdays.app.arcade.adapters.GameAdapter;
import com.softwaresaturdays.app.arcade.models.Game;
import com.softwaresaturdays.app.arcade.models.Message;
import com.softwaresaturdays.app.arcade.models.TextMessage;
import com.softwaresaturdays.app.arcade.networkHelpers.DatabaseHelper;
import com.softwaresaturdays.app.arcade.networkHelpers.NetworkHelper;

import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {

    private RecyclerView mRvChat;
    private RecyclerView mRvGames;
    private ArrayList<Message> mMessages = new ArrayList<>();
    private ArrayList<Game> mAllGames = new ArrayList<>();
    private ChatAdapter mChatAdapter;
    private GameAdapter mGameAdapter;
    private RelativeLayout mRlGameInfo;
    private TextView mTvUserHighScore;
    private RoundedImageView mIvProfile;
    private Game mSelectedGame;
    private int mLimit = 12;
    private LinearLayoutManager mLayoutManager;
    private SwipeRefreshLayout mSwipeContainer;
    private EditText mEtTextMessage;
    private Message mSelectedMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        DatabaseHelper.fetchMessages(mLimit, new DatabaseHelper.OnDatabaseFetchListener() {
            @Override
            public void onMessagesFetched(ArrayList<Message> messages) {
                refreshChatList(messages);
                scrollToEnd();
            }
        });

        subscribeToNotifications();
    }

    // Refresh the recycler view with updated messages
    private void refreshChatList(ArrayList<Message> messages) {
        // Update messages REAL-TIME and update list view
        mMessages = messages;
        mChatAdapter = new ChatAdapter(ChatActivity.this, mMessages, new ChatAdapter.OnItemClickListener() {
            @Override
            public void onClick(Message message) {
                // Clicked on message
            }

            @Override
            public void onLongClick(Message message, View v) {
                // Long click on message enables a popup to let users delete the message
                if (message.getUserId().equals(MyApplication.currUser.getUid())) {
                    PopupMenu popupMenu = new PopupMenu(ChatActivity.this, v);
                    popupMenu.setOnMenuItemClickListener(ChatActivity.this);
                    popupMenu.inflate(R.menu.popup_menu_message);
                    popupMenu.show();
                    mSelectedMessage = message;
                }
            }
        });
        if (mRvChat != null) {
            mRvChat.setAdapter(mChatAdapter);
        }
    }

    private void scrollToEnd() {
        mLayoutManager.scrollToPosition(mChatAdapter.getItemCount() - 1);
    }

    // Subscribe to the topic "arcade", all notifications are sent users subscribed to the topic "arcade"
    private void subscribeToNotifications() {
        FirebaseMessaging.getInstance().subscribeToTopic("arcade");
    }

    private void unsubscribeToNotifications() {
        FirebaseMessaging.getInstance().unsubscribeFromTopic("arcade");
    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        // Used to make sure the view contents move up when soft keyboard comes up
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        return super.onCreateView(name, context, attrs);
    }

    // Fetch the next 12 messages and then refresh the recycler view
    void refreshChatList() {
        mLimit += 12;
        DatabaseHelper.fetchMessages(mLimit, new DatabaseHelper.OnDatabaseFetchListener() {
            @Override
            public void onMessagesFetched(ArrayList<Message> messages) {
                refreshChatList(messages);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        final Toolbar mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        // Link Java code variables to UI elements
        mRvChat = findViewById(R.id.rvChat);
        mRvGames = findViewById(R.id.rvGames);
        mRlGameInfo = findViewById(R.id.rlGameInfo);
        mTvUserHighScore = findViewById(R.id.tvUserHighScore);
        ImageView ivSend = findViewById(R.id.ivSend);
        mIvProfile = findViewById(R.id.ivProfile);
        mEtTextMessage = findViewById(R.id.etTextMessage);
        CardView cvPlayButton = findViewById(R.id.cvPlayButton);
        mSwipeContainer = findViewById(R.id.swipeContainer);

        // Needed to set global state - used to check that notifications are not displayed when app is foreground
        MyApplication.isForeground = true;

        // Setup refresh listener which triggers new data loading
        mSwipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                mSwipeContainer.setRefreshing(false);
                refreshChatList();
            }
        });

        // Configure the refreshing colors
        mSwipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        // Assert throws error if condition is not true. Good for debugging
        assert MyApplication.currUser != null;

        // Load profile pic using Glide
        Glide.with(this).load(MyApplication.currUser.getPhotoUrl()).into(mIvProfile);

        // Game info only visible when clicked on a game
        mRlGameInfo.setVisibility(View.GONE);

        // Set clicklisteners to the activity so all onClick callbacks are together
        cvPlayButton.setOnClickListener(this);
        ivSend.setOnClickListener(this);
        mEtTextMessage.setOnClickListener(this);
        mIvProfile.setOnClickListener(this);

        setupChatList();

        setupGamesList();
    }

    private void setupGamesList() {
        mAllGames.add(new Game("2048"));
        mAllGames.add(new Game("TicTacToe"));
        mAllGames.add(new Game("Pac Man"));
        mAllGames.add(new Game("Hang Man"));
        mAllGames.add(new Game("Flappy Bird"));

        mGameAdapter = new GameAdapter(this, mAllGames, new GameAdapter.OnItemClickListener() {
            @Override
            public void onClick(Game game) {
                if (mRlGameInfo.getVisibility() == View.GONE || !mSelectedGame.getTitle().equals(game.getTitle())) {
                    fetchAndShowGameInfo(game);
                } else {
                    hidGameInfo();
                }

                // Clicked on Game
                mSelectedGame = game;
            }
        });

        mRvGames.setAdapter(mGameAdapter);
        mRvGames.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
    }

    private void hidGameInfo() {
        mRlGameInfo.setVisibility(View.GONE);
    }

    private void fetchAndShowGameInfo(Game game) {
        mRlGameInfo.setVisibility(View.VISIBLE);
        if (MyApplication.currUser.getHighScores() != null && MyApplication.currUser.getHighScores().get(game.getTitle()) != null) {
            mTvUserHighScore.setText("Your high score: " + MyApplication.currUser.getHighScores().get(game.getTitle()));
        } else {
            mTvUserHighScore.setText("Your high score: UNAVAILABLE");
        }
    }

    private void setupChatList() {
        mChatAdapter = new ChatAdapter(this, mMessages, new ChatAdapter.OnItemClickListener() {
            @Override
            public void onClick(Message message) {
                // Clicked on message
            }

            @Override
            public void onLongClick(Message message, View v) {
                // Long click on message
                if (message.getUserId().equals(MyApplication.currUser.getUid())) {
                    PopupMenu popupMenu = new PopupMenu(ChatActivity.this, v);
                    popupMenu.setOnMenuItemClickListener(ChatActivity.this);
                    popupMenu.inflate(R.menu.popup_menu_message);
                    popupMenu.show();
                    mSelectedMessage = message;
                }
            }
        });

        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        mRvChat.setAdapter(mChatAdapter);
        mRvChat.setLayoutManager(mLayoutManager);
        scrollToEnd();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.cvPlayButton:
                if (mSelectedGame != null) {
                    switch (mSelectedGame.getTitle()) {
                        default:
                            Snackbar.make(mRvChat, mSelectedGame.getTitle() + " is Under construction", Snackbar.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.ivSend:
                String text = mEtTextMessage.getText().toString();
                if (text.isEmpty()) {
                    Snackbar.make(mRvChat, "Please enter searchText", Snackbar.LENGTH_SHORT).show();
                } else {
                    // Create new searchText message
                    Message message = new TextMessage(text);
                    // Upload message to database
                    DatabaseHelper.uploadMessage(message);

                    // Send notifications to all devices
                    NetworkHelper.sendNotifications(MyApplication.currUser.getName() + " says " + text,
                            MyApplication.currUser.getUid(), ChatActivity.this);
                }
                mEtTextMessage.setText("");
                break;
            case R.id.etTextMessage:
                new CountDownTimer(200, 200) {
                    public void onFinish() {
                        // When timer is finished
                        scrollToEnd();
                    }

                    public void onTick(long millisUntilFinished) {
                    }
                }.start();
                break;
            case R.id.ivProfile:
                // TODO implement a PopupMenu to show the menu represented by "popup_menu.xml"
                // HINT: Set menu item click listener to ChatActivity.this so following
                // onMenuItemClick() is called automatically when menu item is clicker

        }

    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        int id = menuItem.getItemId();
        switch (id) {
            case R.id.notificationsOFF:
                unsubscribeToNotifications();
                Snackbar.make(mIvProfile, "Notifications turned OFF", Snackbar.LENGTH_SHORT).show();
                break;
            case R.id.notificationsON:
                subscribeToNotifications();
                Snackbar.make(mIvProfile, "Notifications turned ON", Snackbar.LENGTH_SHORT).show();
                break;
            case R.id.delete:
                if (mSelectedMessage != null) {
                    DatabaseHelper.deleteMessage(mSelectedMessage);
                }
        }
        return false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        MyApplication.isForeground = false;
    }
}
