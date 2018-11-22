package com.example.alien.course06task01;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.Date;

import static android.text.format.DateFormat.getDateFormat;
import static android.text.format.DateFormat.getTimeFormat;

class MessageViewHolder extends RecyclerView.ViewHolder {

    private TextView mUserName;
    private TextView mText;
    private TextView mDate;
    private DateFormat mDateFormat;
    private DateFormat mTimeFormat;
    private RelativeLayout mRelativeLayout;


    public MessageViewHolder(View itemView) {
        super(itemView);

        mUserName = itemView.findViewById(R.id.tv_user);
        mText = itemView.findViewById(R.id.tv_text);
        mDate = itemView.findViewById(R.id.tv_timestamp);
        mRelativeLayout = itemView.findViewById(R.id.cl_background);
        mDateFormat = getDateFormat(itemView.getContext());
        mTimeFormat = getTimeFormat(itemView.getContext());
    }

    public void bind(Message message, String userName) {
        if (message != null) {
            if (userName != null && userName.equals(message.getUsername())) {
                mRelativeLayout.setBackground(itemView.getContext().getDrawable(R.drawable.bg_message_hilight));
            } else {
                mRelativeLayout.setBackground(itemView.getContext().getDrawable(R.drawable.bg_message));
            }
            mRelativeLayout.requestLayout();

            mUserName.setText(message.getUsername());
            mText.setText(message.getText());
            Date date = message.getTime();
            if (date != null) {
                String text = mDateFormat.format(date)
                        + " "
                        + mTimeFormat.format(date);
                mDate.setText(text);
            }
        }
    }
}
