package com.example.alien.course06task01;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SecondActivity extends AppCompatActivity {

    private TextView mUserNameTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            MainActivity.start(this);
            finish();
        }
        setContentView(R.layout.activity_second);
        mUserNameTextView = findViewById(R.id.tv_username);
        mUserNameTextView.setText(user.getDisplayName());
    }

    public static void start(Context context) {
        if (context != null) {
            Intent intent = new Intent(context, SecondActivity.class);
            context.startActivity(intent);
        } else {
            throw new IllegalArgumentException("Context can't be null");
        }
    }
}
