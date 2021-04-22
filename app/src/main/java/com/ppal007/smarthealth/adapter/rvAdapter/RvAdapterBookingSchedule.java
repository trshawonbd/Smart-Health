package com.ppal007.smarthealth.adapter.rvAdapter;

import android.annotation.SuppressLint;
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
import com.ppal007.smarthealth.interFace.ScheduleBookingClick;

import java.sql.Time;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class RvAdapterBookingSchedule extends RecyclerView.Adapter<RvAdapterBookingSchedule.MyViewHolder> {

    private final Context context;
    private List<String> _schedule_booking_time;
    private final ArrayList<String> scheduleList;
    private ScheduleBookingClick scheduleBookingClick;

    public RvAdapterBookingSchedule(Context context, List<String> _schedule_booking_time, ArrayList<String> scheduleList, ScheduleBookingClick scheduleBookingClick) {
        this.context = context;
        this._schedule_booking_time = _schedule_booking_time;
        this.scheduleList = scheduleList;
        this.scheduleBookingClick = scheduleBookingClick;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.rv_sample_schedule_booking,parent,false);

        return new RvAdapterBookingSchedule.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.tvStLong.setText(scheduleList.get(position));

//        convert start long to time
        long startTimeMilli = Long.parseLong(scheduleList.get(position));
        Time startTime = new Time(startTimeMilli);
        @SuppressLint("SimpleDateFormat")
        Format startTimeFormat = new SimpleDateFormat("HH:mm:ss");
        String _start_time = startTimeFormat.format(startTime);
//        set start time
        holder.textViewStart.setText(_start_time);

//        convert end long to time
        int v = Integer.parseInt(holder.tvStLong.getText().toString());
        String _endTime = String.valueOf(v+900000);
        long endTimeMilli = Long.parseLong(_endTime);
        Time endTime = new Time(endTimeMilli);
        @SuppressLint("SimpleDateFormat")
        Format endTimeFormat = new SimpleDateFormat("HH:mm:ss");
        String _end_time = endTimeFormat.format(endTime);
//        set end time
        holder.textViewEnd.setText(_end_time);


        boolean found = false;
        for (String st : _schedule_booking_time){
            if (st.equals(scheduleList.get(position))) {
                found = true;
                break;
            }
        }
        if (found){
            holder.linearLayout.setBackgroundColor(Color.parseColor("#D6DBDF"));
            holder.itemView.setClickable(false);
            holder.itemView.setEnabled(false);
        }else {
            holder.linearLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
            holder.itemView.setClickable(true);
            holder.itemView.setEnabled(true);
        }

//        click listener
        holder.itemView.setOnClickListener(v1 -> scheduleBookingClick.onClickBooking(position,scheduleList.get(position)));

    }

    @Override
    public int getItemCount() {
        return scheduleList.size()-1;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvStLong,textViewStart,textViewEnd;
        LinearLayout linearLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvStLong=itemView.findViewById(R.id.tvStartScB);
            textViewStart=itemView.findViewById(R.id.tvScheduleBookingStart);
            textViewEnd=itemView.findViewById(R.id.tvScheduleBookingEnd);
            linearLayout = itemView.findViewById(R.id.lineRIdBookingSchedule);
        }
    }
}
