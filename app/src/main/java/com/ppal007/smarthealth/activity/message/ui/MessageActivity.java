package com.ppal007.smarthealth.activity.message.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ppal007.smarthealth.R;
import com.ppal007.smarthealth.activity.message.adapter.MessageAdapter;
import com.ppal007.smarthealth.activity.message.model.Chat;
import com.ppal007.smarthealth.activity.message.model.User;
import com.ppal007.smarthealth.utils.Common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class MessageActivity extends AppCompatActivity {
    private static final String TAG = "MessageActivity";

    private EditText editText_msg;
    private ImageButton imageButton_send;
    private RecyclerView recyclerView;
    private TextView textView_title;

    private FirebaseUser fUser;
    private DatabaseReference reference;
    private Intent intent;
    private MessageAdapter messageAdapter;
    private List<Chat> chatList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // hide title bar
        WindowManager.LayoutParams attrs = getWindow().getAttributes();
        attrs.flags = WindowManager.LayoutParams.FLAG_FULLSCREEN;
        getWindow().setAttributes(attrs);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        Toolbar toolbar = findViewById(R.id.toolbar_msg);
        setSupportActionBar(toolbar);

        //find xml
        editText_msg=findViewById(R.id.et_msg_id);
        imageButton_send=findViewById(R.id.send_button_id);
        recyclerView=findViewById(R.id.recyclerViewMsg);
        textView_title = toolbar.findViewById(R.id.tv_msg_title);

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);



        intent = getIntent();
        String userId = intent.getStringExtra("user_id");
        String userName = intent.getStringExtra("user_name");


        fUser = FirebaseAuth.getInstance().getCurrentUser();
        textView_title.setText(userName);


        //send button click listener
        imageButton_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = editText_msg.getText().toString();

                if (msg.isEmpty()){
                    Toast.makeText(MessageActivity.this, "msg is empty!", Toast.LENGTH_SHORT).show();
                }else {
                    sendMessage(fUser.getUid(),userId,msg);
                }
                editText_msg.setText("");
            }
        });




        reference = FirebaseDatabase.getInstance().getReference(Common.FIREBASE_USER_DB).child(userId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);

                readMessage(fUser.getUid(),userId);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MessageActivity.this, ""+error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendMessage(String sender, String receiver, String message) {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender",sender);
        hashMap.put("receiver",receiver);
        hashMap.put("message",message);

        reference.child(Common.FIREBASE_CHAT_DB).push().setValue(hashMap);

    }


    private void readMessage(String uid, String userId) {
        chatList = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference(Common.FIREBASE_CHAT_DB);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chatList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Chat chat = snapshot.getValue(Chat.class);

                    if (chat.getReceiver().equals(uid) && chat.getSender().equals(userId) ||
                            chat.getReceiver().equals(userId) && chat.getSender().equals(uid)){
                        chatList.add(chat);
                    }
                    messageAdapter = new MessageAdapter(MessageActivity.this,chatList);
                    recyclerView.setAdapter(messageAdapter);
//                    recyclerView.scrollToPosition(chatList.size()-1);
                    recyclerView.scrollToPosition(recyclerView.getAdapter().getItemCount()-1);
                    messageAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MessageActivity.this, ""+error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }




    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();

    }
}