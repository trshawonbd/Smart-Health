package com.ppal007.smarthealth.activity.message.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ppal007.smarthealth.R;
import com.ppal007.smarthealth.activity.message.model.User;
import com.ppal007.smarthealth.activity.message.ui.MessageActivity;
import com.ppal007.smarthealth.adapter.sharedPref.AdapterSharedPref;
import com.ppal007.smarthealth.utils.Common;

import java.util.List;

/**
 * Created by ppal on 20-Feb-21.
 */
public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private Context context;
    private List<User> userList;
    //private AdapterSharedPref sharedPref;

    public UserAdapter(Context context, List<User> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.sample_user,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //User user = userList.get(position);

        holder.textView_mode.setText(userList.get(position).getId());
        holder.textView_name.setText(userList.get(position).getUsername());
        //Glide.with(context).load(R.drawable.ic_user).into(holder.imageView);




        //click event
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MessageActivity.class);
                intent.putExtra("user_id",userList.get(position).getId());
                intent.putExtra("user_name",userList.get(position).getUsername());
                context.startActivity(intent);
            }
        });



    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView textView_name,textView_mode;
        ImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.user_card);
            textView_name = itemView.findViewById(R.id.usernameRvUser);
            textView_mode = itemView.findViewById(R.id.tv_user_mode);
            imageView = itemView.findViewById(R.id.img_user_msg);
        }
    }
}
