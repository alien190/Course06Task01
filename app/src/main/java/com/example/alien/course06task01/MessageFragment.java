package com.example.alien.course06task01;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.regex.Pattern;


public class MessageFragment extends Fragment {

    private static final String TAG = "MessageFragmentTAG";
    private static final String COLLECTION_NAME = "messages";

    private FirebaseFirestore mDatabase;
    private ImageButton mSendImageButton;
    private EditText mMessageEditText;
    private RecyclerView mRecyclerView;
    private View mView;
    private FirebaseUser mUser;
    private MessageAdapter mAdapter;
    private ListenerRegistration mRegistration;

    public static MessageFragment newInstance() {

        Bundle args = new Bundle();

        MessageFragment fragment = new MessageFragment();
        fragment.setArguments(args);
        fragment.setRetainInstance(true);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.fr_messages, container, false);
            mUser = FirebaseAuth.getInstance().getCurrentUser();
            if (mUser != null) {
                initFirestore();
                mSendImageButton = mView.findViewById(R.id.bt_send);
                mMessageEditText = mView.findViewById(R.id.et_message);
                mRecyclerView = mView.findViewById(R.id.recycler_messages);
                initRecycler();
            }
        }
        return mView;
    }


    private void initRecycler() {
        mAdapter = new MessageAdapter();
        mAdapter.setUserName(mUser.getDisplayName());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void initListener() {
        mRegistration = mDatabase.collection(COLLECTION_NAME)
                .orderBy("time", Query.Direction.ASCENDING)
                .addSnapshotListener(this::snapshotListener);
    }

    private void clearListener() {
        if (mRegistration != null) {
            mRegistration.remove();
        }
    }

    private void snapshotListener(QuerySnapshot queryDocumentSnapshots, FirebaseFirestoreException e) {
        if (e != null) {
            Log.w(TAG, "Listen failed.", e);
            return;
        }
        if (queryDocumentSnapshots != null && !queryDocumentSnapshots.getMetadata().hasPendingWrites())
            if (!queryDocumentSnapshots.isEmpty()) {
                for (DocumentChange documentChange : queryDocumentSnapshots.getDocumentChanges()) {
                    Log.d(TAG, " data changes: " + documentChange.getDocument());
                    mAdapter.addMessage(documentChange.getDocument().toObject(Message.class));
                }
                mRecyclerView.scrollToPosition(mAdapter.getItemCount() - 1);
            } else {
                Log.d(TAG, " data: empty");
            }
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
        initListener();
    }

    @Override
    public void onStop() {
        mSendImageButton.setOnClickListener(null);
        clearListener();
        super.onStop();
    }

    private void onSend(View view) {
        String message = mMessageEditText.getText().toString();
        if (isMessageCorrect(message)) {
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
            return false;
        }
        return true;
    }

    private void showMessageTextError(String error) {
        mMessageEditText.setError(error);
    }

    private void sendMessage(String message) {
        mDatabase.collection(COLLECTION_NAME)
                .add(new Message(message, mUser.getDisplayName()))
                .addOnSuccessListener(d -> Log.d(TAG, "sendMessage: successful, text:" + message))
                .addOnFailureListener(e -> Log.d(TAG, "sendMessage: failure, error:" + e.getMessage()));
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
