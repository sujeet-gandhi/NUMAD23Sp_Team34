package com.neu.numad23sp_team_34.project.chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.neu.numad23sp_team_34.R;
import com.neu.numad23sp_team_34.project.chat.ChatAdapter;
import com.neu.numad23sp_team_34.project.chat.ChatMessage;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView chatRecyclerView;
    private ChatAdapter chatAdapter;
    private List<ChatMessage> chatMessages = new ArrayList<>();

    private EditText chatInput;
    private Button chatSendButton;

    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private String currentUserId;
    private String storyAuthorId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.project_chat);

        storyAuthorId = getIntent().getStringExtra("storyAuthorId");

        databaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        currentUserId = firebaseAuth.getCurrentUser().getUid();

        chatRecyclerView = findViewById(R.id.chatRecyclerView);
        chatAdapter = new ChatAdapter(this, chatMessages, currentUserId);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        chatRecyclerView.setLayoutManager(layoutManager);
        chatRecyclerView.setAdapter(chatAdapter);

        chatInput = findViewById(R.id.chatInput);
        chatSendButton = findViewById(R.id.chatSendButton);
        chatSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });

        loadMessages();
    }

    private void sendMessage() {
        String messageText = chatInput.getText().toString().trim();

        if (!TextUtils.isEmpty(messageText)) {
            String senderId = firebaseAuth.getCurrentUser().getUid();
            String senderName = firebaseAuth.getCurrentUser().getDisplayName();

            ChatMessage chatMessage = new ChatMessage(senderId, senderName, messageText);

            databaseReference.child("chats").child(storyAuthorId).child("messages").push().setValue(chatMessage, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError error, DatabaseReference ref) {
                    if (error != null) {
                        Toast.makeText(ChatActivity.this, "Error sending message", Toast.LENGTH_SHORT).show();
                    } else {
                        chatInput.setText("");
                    }
                }
            });
        }
    }

    private void loadMessages() {
        databaseReference.child("chats").child(storyAuthorId).child("messages").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                ChatMessage chatMessage = dataSnapshot.getValue(ChatMessage.class);
                chatMessages.add(chatMessage);
                chatAdapter.notifyDataSetChanged();
                chatRecyclerView.scrollToPosition(chatMessages.size() - 1);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s ) {
        }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
        }

        @Override
        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Toast.makeText(ChatActivity.this, "Error loading messages", Toast.LENGTH_SHORT).show();
        }
    });
}
}

