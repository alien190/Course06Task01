package com.example.alien.course06task01;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageViewHolder> {
    private List<Message> mMessages = new ArrayList<>();
    private String mUserName;

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.li_message, viewGroup, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder messageViewHolder, int i) {
        messageViewHolder.bind(getItem(i), mUserName);
    }

    @Override
    public int getItemCount() {
        return mMessages.size();
    }

    private Message getItem(int index) {
        if (index >= 0 && index < mMessages.size()) {
            return mMessages.get(index);
        } else {
            throw new IllegalArgumentException("index out of bounds");
        }
    }

    public void addMessage(Message message) {
        if (message != null && !mMessages.contains(message)) {
            mMessages.add(message);
            notifyItemInserted(mMessages.size() - 1);
        }
    }

    public void setUserName(String userName) {
        mUserName = userName;
    }
}
