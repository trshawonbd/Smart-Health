package com.ppal007.smarthealth.activity.message.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ppal007.smarthealth.R;
import com.ppal007.smarthealth.activity.message.model.Chat;

import java.util.List;

/**
 * Created by ppal on 21-Feb-21.
 */
public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MyViewHolder>{

    private static final int MSG_TYPE_LEFT = 0;
    private static final int MSG_TYPE_RIGHT = 1;
    private Context context;
    private List<Chat> chatList;
    private FirebaseUser fUser;

    public MessageAdapter(Context context, List<Chat> chatList) {
        this.context = context;
        this.chatList = chatList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view;
        if (viewType == MSG_TYPE_LEFT){
            view = inflater.inflate(R.layout.chat_item_left, parent, false);
        }else{
            view = inflater.inflate(R.layout.chat_item_right, parent, false);
        }
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Chat chat = chatList.get(position);

        holder.textView_msg.setText(chat.getMessage());

    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textView_msg;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textView_msg = itemView.findViewById(R.id.show_msg);
        }
    }

    @Override
    public int getItemViewType(int position) {
        fUser = FirebaseAuth.getInstance().getCurrentUser();
//chatList.get(position).getSender().equals(fUser.getUid())
        if (fUser.getUid().equals(chatList.get(position).getSender())){
            return MSG_TYPE_RIGHT;
        }else {
            return MSG_TYPE_LEFT;
        }
    }
}
