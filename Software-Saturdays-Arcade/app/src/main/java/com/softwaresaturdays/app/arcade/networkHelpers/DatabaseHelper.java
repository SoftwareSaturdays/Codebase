package com.softwaresaturdays.app.arcade.networkHelpers;

import android.util.Log;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.softwaresaturdays.app.arcade.models.Game;
import com.softwaresaturdays.app.arcade.models.Message;
import com.softwaresaturdays.app.arcade.models.TextMessage;
import com.softwaresaturdays.app.arcade.models.User;

import java.util.ArrayList;

import javax.annotation.Nullable;

public class DatabaseHelper {

    // Database organized as key-value pairs
    // Each key can either point to a document or a collection of documents
    public static final String KEY_USERS = "users";
    public static final String KEY_MESSAGES = "messages";
    public static final String KEY_GAMES = "games";
    private static final String TAG = "DATABASE_HELPER:";

    public static void uploadUserInfo(User user) {
        // TODO Read Firestore documentation and implement uploading user info on the database

        // HINT: Use SetOption.merge() to avoid overwriting fields such as registration fcm token
    }

    public static void getUserInfo(String userId, final OnUserInfoFetchListener listener) {
        // TODO get user info from the database ONCE (not real time) and send back to the listener

        // HINT: Hold CTRL + Click on OnUserInfoFetchListener above to see how the interface is defined
    }

    public static void getAllUsersInfo(final OnUserInfoFetchListener listener) {
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference colRef = db.collection(KEY_USERS);

        // TODO get all the users in REALTIME and send back the list of Users to the listener
    }

    public static void uploadMessage(Message message) {
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference colRef = db.collection(KEY_MESSAGES);

        colRef.document(message.getTimestamp() + "").set(message);
    }

    public static void deleteMessage(Message mSelectedMessage) {
        // TODO reference and then delete the selected message from the database
    }


    public static void fetchMessages(int limit, final OnDatabaseFetchListener listener) {
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference colRef = db.collection(KEY_MESSAGES);

        // Real time listener, onEvent() is called automatically when a new message is added or old messages are deleted
        colRef.orderBy("timestamp", Query.Direction.DESCENDING).limit(limit).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots,
                                @javax.annotation.Nullable FirebaseFirestoreException e) {

                if (queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty()) {
                    Log.d(TAG, "Current data: " + queryDocumentSnapshots.size() + " messages");

                    ArrayList<Message> messages = new ArrayList<>();
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                        // Cast the document into the right type of message
                        if (documentSnapshot.get("type").equals(Message.TYPE_TEXT_MESSAGE)) {
                            try {
                                TextMessage text = documentSnapshot.toObject(TextMessage.class);
                                // automatically reverses the list of messages to get them in the correct order
                                messages.add(0, text);
                            } catch (Exception e2) {
                                e2.printStackTrace();
                            }
                        } else if (documentSnapshot.get("type").equals(Message.TYPE_GIF_MESSAGE)) {
                            // Implementation in Next Session
                        }
                    }

                    listener.onMessagesFetched(messages);

                } else {
                    Log.d(TAG, "Current data: null");
                }
            }
        });
    }


    public static void updateGameHighScore(Game updatedGame) {
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection(KEY_GAMES).document(updatedGame.getTitle());

        docRef.set(updatedGame, SetOptions.merge());
    }

    public static void getGameHighScores(final onGamesFetchListener listener) {
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference colRef = db.collection(KEY_GAMES);

        colRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty()) {
                    Log.d(TAG, "Current data: " + queryDocumentSnapshots.size() + " games");

                    ArrayList<Game> games = new ArrayList<>();
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                        try {
                            Game game = documentSnapshot.toObject(Game.class);
                            games.add(game);
                        } catch (Exception e2) {
                            e2.printStackTrace();
                        }
                    }

                    listener.onGamesFetched(games);

                } else {
                    Log.d(TAG, "Current data: null");
                }
            }
        });
    }

    public interface onGamesFetchListener {
        void onGamesFetched(ArrayList<Game> games);
    }

    public interface OnDatabaseFetchListener {
        void onMessagesFetched(ArrayList<Message> messages);
    }

    public interface OnUserInfoFetchListener {
        void onUserInfoFetched(User user);

        void onAllUsersInfoFetched(ArrayList<User> allUsers);
    }
}
