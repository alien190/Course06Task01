package com.example.alien.course06task01;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_second, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mi_logout: {
                logout();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        final Context context = this;
        AuthUI.getInstance()
                .signOut(context)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        MainActivity.start(context);
                        finish();
                    }
                });
    }
}
