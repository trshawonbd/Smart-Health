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
import com.ppal007.smarthealth.interFace.RvSymptomListClick;
import com.ppal007.smarthealth.model.ModelPatientSymptoms;

import java.sql.Time;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class RvAdapterSymptomList extends RecyclerView.Adapter<RvAdapterSymptomList.MyViewHolder> {

    private Context context;
    private String _patientId;
    private String _patientName;
    private ArrayList<ModelPatientSymptoms> rvSymptomList;
    private RvSymptomListClick rvSymptomListClick;

    public RvAdapterSymptomList(Context context, String _patientId, String _patientName, ArrayList<ModelPatientSymptoms> rvSymptomList,RvSymptomListClick rvSymptomListClick) {
        this.context = context;
        this._patientId = _patientId;
        this._patientName = _patientName;
        this.rvSymptomList = rvSymptomList;
        this.rvSymptomListClick = rvSymptomListClick;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.rv_sample_symptom_list,parent,false);

        return new RvAdapterSymptomList.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.textViewId.setText(rvSymptomList.get(position).getId());
        holder.textViewName.setText(_patientName);
//        convert long to date
        long millisecond = Long.parseLong(rvSymptomList.get(position).getSubMitDate());
        Date date = new Date(millisecond);
        @SuppressLint("SimpleDateFormat") Format formatter = new SimpleDateFormat("dd-MMM-yyyy");
//        set det to textView
        holder.textViewDate.setText(formatter.format(date));

//        convert long to time
        long startTimeMilli = Long.parseLong(rvSymptomList.get(position).getSubmitTime());
        Time startTime = new Time(startTimeMilli);
        @SuppressLint("SimpleDateFormat")
        Format startTimeFormat = new SimpleDateFormat("HH:mm:ss");
        String _time = startTimeFormat.format(startTime);
//        set text
        holder.textViewTime.setText(_time);

//        click listener
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rvSymptomListClick.symptomClick(
                        position,
                        rvSymptomList.get(position).getId(),
                        rvSymptomList.get(position).getBpUp(),
                        rvSymptomList.get(position).getBpDown(),
                        rvSymptomList.get(position).getSpo2(),
                        rvSymptomList.get(position).getEcg(),
                        rvSymptomList.get(position).getGlBfrEat(),
                        rvSymptomList.get(position).getGlAftrEat(),
                        rvSymptomList.get(position).getTemperature(),
                        rvSymptomList.get(position).getSubMitDate()
                );
            }
        });
    }

    @Override
    public int getItemCount() {
        return rvSymptomList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textViewId,textViewName,textViewDate,textViewTime;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewId = itemView.findViewById(R.id.rv_sample_symptom_list_id);
            textViewName = itemView.findViewById(R.id.rv_sample_symptom_list_name);
            textViewDate = itemView.findViewById(R.id.rv_sample_symptom_list_dt);
            textViewTime = itemView.findViewById(R.id.rv_sample_symptom_list_time);
        }
    }
}
