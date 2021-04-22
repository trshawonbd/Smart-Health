package com.ppal007.smarthealth.adapter.rvAdapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ppal007.smarthealth.R;
import com.ppal007.smarthealth.interFace.PrescriptionSymtomClick;
import com.ppal007.smarthealth.model.ModelPatientSymptomPrescription;

import java.sql.Time;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class RvAdapterSymptomPrescription extends RecyclerView.Adapter<RvAdapterSymptomPrescription.MyViewHolder> {

    private Context context;
    private ArrayList<ModelPatientSymptomPrescription> presList;
    private PrescriptionSymtomClick presClick;

    public RvAdapterSymptomPrescription(Context context, ArrayList<ModelPatientSymptomPrescription> presList, PrescriptionSymtomClick presClick) {
        this.context = context;
        this.presList = presList;
        this.presClick = presClick;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.symptom_prescription_rv_sample,parent,false);

        return new RvAdapterSymptomPrescription.MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        //set tv
        //convert date
        long millisecond = Long.parseLong(presList.get(position).getPresDate());
        Date date = new Date(millisecond);
        @SuppressLint("SimpleDateFormat")
        Format formatter = new SimpleDateFormat("dd-MMM-yyyy");
        String _date = formatter.format(date);

        holder.textViewDate.setText("Date : "+_date);

        //convert time
        long startTimeMilli = Long.parseLong(presList.get(position).getPresTime());
        Time startTime = new Time(startTimeMilli);
        @SuppressLint("SimpleDateFormat")
        Format startTimeFormat = new SimpleDateFormat("HH:mm:ss");
        String _time = startTimeFormat.format(startTime);
        holder.textViewTime.setText("Time : "+_time);

        //click listener
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presClick.onClick(String.valueOf(position),
                        presList.get(position).getSymptomTblId(),
                        presList.get(position).getPrescription(),
                        presList.get(position).getPresDate(),
                        presList.get(position).getPresTime());
            }
        });

    }

    @Override
    public int getItemCount() {
        return presList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textViewDate, textViewTime;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewDate = itemView.findViewById(R.id.tv_pres_dt);
            textViewTime = itemView.findViewById(R.id.tv_pres_tm);

        }
    }
}
