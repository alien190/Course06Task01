package com.example.alien.course06task01;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import java.util.regex.Pattern;

import javax.annotation.RegEx;

public class MessageFragment extends Fragment {

    private static final String TAG = "MessageFragment";

    private FirebaseFirestore mDatabase;
    private ImageButton mSendImageButton;
    private EditText mMessageEditText;
    private View mView;
    private FirebaseUser mUser;

    public static MessageFragment newInstance() {

        Bundle args = new Bundle();

        MessageFragment fragment = new MessageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fr_messages, container, false);
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        if (mUser != null) {
            initFirestore();
            mSendImageButton = mView.findViewById(R.id.bt_send);
            mMessageEditText = mView.findViewById(R.id.et_message);
        }
        return mView;
    }

    private void initFirestore() {
        mDatabase = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build();
        mDatabase.setFirestoreSettings(settings);
    }

    @Override
    public void onStart() {
        super.onStart();
        mSendImageButton.setOnClickListener(this::onSend);
    }

    @Override
    public void onStop() {
        mSendImageButton.setOnClickListener(null);
        super.onStop();
    }

    private void onSend(View view) {
        String message = mMessageEditText.getText().toString();
        if (!isMessageCorrect(message)) {
            mMessageEditText.setText("");
            hideKeyboard();
            sendMessage(message);
        }
    }

    private boolean isMessageCorrect(String message) {
        Pattern spacePattern = Pattern.compile("^ *$");
        if (message.isEmpty()) {
            showMessageTextError(getString(R.string.message_error_empty_text));
            return false;
        } else if (spacePattern.matcher(message).matches()) {
            showMessageTextError(getString(R.string.message_error_space_text));
        }
        return true;
    }

    private void showMessageTextError(String error) {
        mMessageEditText.setError(error);
    }

    private void sendMessage(String message) {
        mDatabase.collection("messages")
                .add(new Message(message, mUser.getDisplayName()))
                .addOnSuccessListener(d -> Log.d(TAG, "sendMessage: successful"))
                .addOnFailureListener(e -> Log.d(TAG, "sendMessage: failure"));
    }

    private void hideKeyboard() {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(mView.getWindowToken(), 0);
            }
        }
    }
}
