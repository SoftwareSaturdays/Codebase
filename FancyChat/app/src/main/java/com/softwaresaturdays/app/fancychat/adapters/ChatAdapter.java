package com.softwaresaturdays.app.fancychat.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.softwaresaturdays.app.fancychat.R;
import com.softwaresaturdays.app.fancychat.models.Message;
import com.softwaresaturdays.app.fancychat.models.TextMessage;

import java.util.ArrayList;


public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private ArrayList<Message> messages;

    public ChatAdapter(Context context, ArrayList<Message> messages) {
        this.mContext = context;
        this.messages = messages;
    }

    // A ViewHolder is a fundamental view of each item in the list
    class ChatViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTextMessage;
        private RoundedImageView ivProfilePic;

        ChatViewHolder(View itemView) {
            super(itemView);
            // TODO Reference items in the layout
        }
    }

    // All statements in the callback called after the item view is
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {

        // create a new item view
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_message, parent, false);

        return new ChatViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, final int position) {
        final ChatViewHolder chatViewHolder = (ChatViewHolder) viewHolder;
        final Message message = messages.get(position);

        // Check if it's a text message
        if (message.getType().equals(Message.TYPE_TEXT_MESSAGE)) {

            // TODO Cast the message into TextMessage and set the text to the screen

        }

        chatViewHolder.ivProfilePic.setImageResource(R.drawable.ic_account_circle_black_36dp);
    }

    // Return the size of your dataset
    @Override
    public int getItemCount() {
        return messages.size();
    }
}
