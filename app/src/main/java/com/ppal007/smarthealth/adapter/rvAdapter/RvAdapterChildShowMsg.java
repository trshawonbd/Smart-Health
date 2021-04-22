package com.ppal007.smarthealth.adapter.rvAdapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ppal007.smarthealth.R;
import com.ppal007.smarthealth.model.ModelPrescriptionMsg;

import java.util.ArrayList;

public class RvAdapterChildShowMsg extends RecyclerView.Adapter<RvAdapterChildShowMsg.MyViewHolder> {

    private Context context;
    private ArrayList<ModelPrescriptionMsg> model_pres;
    private int parent_pos;

    public RvAdapterChildShowMsg(Context context, ArrayList<ModelPrescriptionMsg> model_pres, int parent_pos) {
        this.context = context;
        this.model_pres = model_pres;
        this.parent_pos = parent_pos;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.sample_rv_child_show_msg,parent,false);

        return new RvAdapterChildShowMsg.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        //set linear layout background
//        holder.linearLayout.setBackgroundColor(Color.parseColor("#000000"));
        holder.textView.setText(model_pres.get(position).getPrescription());
    }

    @Override
    public int getItemCount() {
        return model_pres.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        LinearLayout linearLayout;
        TextView textView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            linearLayout = itemView.findViewById(R.id.linear_child_doctor_id);
            textView = itemView.findViewById(R.id.tv_doctor_msg_pres);
        }
    }
}
