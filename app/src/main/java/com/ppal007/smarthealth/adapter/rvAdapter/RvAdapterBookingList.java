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
import com.ppal007.smarthealth.model.ModelPatientRequestList;

import java.sql.Time;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class RvAdapterBookingList extends RecyclerView.Adapter<RvAdapterBookingList.MyViewHolder> {

    private Context context;
    private List<String> schedule_booking_time;
    private ArrayList<ModelPatientRequestList> patientDetails;

    public RvAdapterBookingList(Context context, List<String> schedule_booking_time, ArrayList<ModelPatientRequestList> patientDetails) {
        this.context = context;
        this.schedule_booking_time = schedule_booking_time;
        this.patientDetails = patientDetails;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.rv_custom_booking_list,parent,false);

        return new RvAdapterBookingList.MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        //set raw time
        holder.textViewRawTime.setText(schedule_booking_time.get(position));

//        convert start time
        long startTimeMilli = Long.parseLong(schedule_booking_time.get(position));
        Time startTime = new Time(startTimeMilli);
        @SuppressLint("SimpleDateFormat")
        Format startTimeFormat = new SimpleDateFormat("HH:mm:ss");
        String _start_time = startTimeFormat.format(startTime);
//        set start time
        holder.textViewStartTime.setText(_start_time);

//        convert end time
        int v = Integer.parseInt(holder.textViewRawTime.getText().toString());
        String _endTime = String.valueOf(v+900000);
        long endTimeMilli = Long.parseLong(_endTime);
        Time endTime = new Time(endTimeMilli);
        @SuppressLint("SimpleDateFormat")
        Format endTimeFormat = new SimpleDateFormat("HH:mm:ss");
        String _end_time = endTimeFormat.format(endTime);
//        set end time
        holder.textViewEndTime.setText(_end_time);
//        set patient details
        holder.textViewPatientName.setText("Patient Name : "+patientDetails.get(position).getName());
        holder.textViewPatientGender.setText("Gender : "+patientDetails.get(position).getGender());
        holder.textViewPatientAge.setText("Age : "+patientDetails.get(position).getAge());

    }

    @Override
    public int getItemCount() {
        return schedule_booking_time.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textViewRawTime,textViewStartTime,textViewEndTime,textViewPatientName,textViewPatientGender,textViewPatientAge;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewRawTime = itemView.findViewById(R.id.tvRaw_time_id);
            textViewStartTime = itemView.findViewById(R.id.tvRvBookingStart);
            textViewEndTime = itemView.findViewById(R.id.tvRvBookingEnd);
            textViewPatientName = itemView.findViewById(R.id.tvRvBookingPatientName);
            textViewPatientGender = itemView.findViewById(R.id.tvRvPatientGender);
            textViewPatientAge = itemView.findViewById(R.id.tvRvPatientAge);

        }
    }
}
