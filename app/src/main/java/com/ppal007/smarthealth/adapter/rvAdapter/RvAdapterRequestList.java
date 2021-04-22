package com.ppal007.smarthealth.adapter.rvAdapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ppal007.smarthealth.R;
import com.ppal007.smarthealth.interFace.RvRequestClick;
import com.ppal007.smarthealth.model.ModelPatientRequestList;

import java.util.ArrayList;

public class RvAdapterRequestList extends RecyclerView.Adapter<RvAdapterRequestList.MyViewHolder> {

    private Context context;
    private ArrayList<ModelPatientRequestList> requestLists;
    private RvRequestClick rvRequestClick;

    public RvAdapterRequestList(Context context, ArrayList<ModelPatientRequestList> requestLists, RvRequestClick rvRequestClick) {
        this.context = context;
        this.requestLists = requestLists;
        this.rvRequestClick = rvRequestClick;
    }

    @NonNull
    @Override
    public RvAdapterRequestList.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.sample_rv_request,parent,false);

        return new MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RvAdapterRequestList.MyViewHolder holder, int position) {
//        set resource
        Glide.with(context).load(R.drawable.request_img).circleCrop().into(holder.imageView);
        holder.textViewId.setText(requestLists.get(position).getId());
        holder.textViewName.setText(requestLists.get(position).getName());
        holder.textViewGender.setText("Gender : "+requestLists.get(position).getGender());
        holder.textViewAge.setText("Age : "+requestLists.get(position).getAge());

//        add click listener
        holder.itemView.setOnClickListener(v -> rvRequestClick.requestClick(
                position,
                requestLists.get(position).getId(),
                requestLists.get(position).getName(),
                requestLists.get(position).getGender(),
                requestLists.get(position).getAge(),
                requestLists.get(position).getEmail(),
                requestLists.get(position).getPhone()
        ));
    }

    @Override
    public int getItemCount() {
        return requestLists.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView textViewId,textViewName,textViewGender,textViewAge;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.rv_sample_request_img);
            textViewId = itemView.findViewById(R.id.tv_rv_sample_patient_id);
            textViewName = itemView.findViewById(R.id.tv_rv_sample_patient_name);
            textViewGender = itemView.findViewById(R.id.tv_rv_sample_gender);
            textViewAge = itemView.findViewById(R.id.tv_rv_sample_age);

        }
    }
}
