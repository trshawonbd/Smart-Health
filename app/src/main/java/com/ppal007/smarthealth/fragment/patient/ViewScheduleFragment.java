package com.ppal007.smarthealth.fragment.patient;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ppal007.smarthealth.R;
import com.ppal007.smarthealth.adapter.progressBar.ProgressBarAdapter;
import com.ppal007.smarthealth.adapter.rvAdapter.RvAdapterBookingSchedule;
import com.ppal007.smarthealth.adapter.sharedPref.AdapterSharedPref;
import com.ppal007.smarthealth.interFace.ScheduleBookingClick;
import com.ppal007.smarthealth.model.ModelBookingSchedule;
import com.ppal007.smarthealth.model.ModelDoctorSchedule;
import com.ppal007.smarthealth.retrofit.ApiClient;
import com.ppal007.smarthealth.retrofit.ApiInterface;

import org.json.JSONArray;
import org.json.JSONException;

import java.sql.Time;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewScheduleFragment extends Fragment implements ScheduleBookingClick {
    private void showToast(String msg){
        Toast.makeText(getContext(), ""+msg, Toast.LENGTH_SHORT).show();
    }


    public ViewScheduleFragment() {
        // Required empty public constructor
    }

    private ProgressBarAdapter progressBarAdapter;
    private AdapterSharedPref sharedPref;

    private TextView textViewSchedule;
    private RecyclerView recyclerViewScheduleBooking;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_view_schedule, container, false);
//        init progress bar adapter
        progressBarAdapter = new ProgressBarAdapter(getContext());
//        init sharedPref
        sharedPref = new AdapterSharedPref(Objects.requireNonNull(getContext()));
//        find xml
        textViewSchedule = view.findViewById(R.id.tvDoctorSchedule);
        recyclerViewScheduleBooking = view.findViewById(R.id.rvScheduleBook);

//        retrieve schedule
        retrieveSchedule();


        return view;
    }

    private void retrieveSchedule() {
        progressBarAdapter.startLoadingDialog();
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<ModelDoctorSchedule> call = apiInterface.retrieve_doctor_schedule_from_patient(sharedPref.getUserId());
        call.enqueue(new Callback<ModelDoctorSchedule>() {
            @Override
            public void onResponse(Call<ModelDoctorSchedule> call, Response<ModelDoctorSchedule> response) {
                if (response.isSuccessful() && response.body()!=null){
                    ModelDoctorSchedule model = response.body();
                    if (model.isSuccess()){
                        //progressBarAdapter.dismissDialog();
                        String _schedule_date = model.getScheduleDate();
                        String _start_time = model.getScheduleTimeStart();
                        String _end_time = model.getScheduleTimeEnd();
                        String _doctor_name = model.getName();
                        String _doctorId = model.getDoctorId();

//                        convert long to date and time
                        convert_long_to_date_and_time(_schedule_date,_start_time,_end_time,_doctor_name);
//                        init schedule
                        initSchedule(_start_time,_end_time,_doctorId);
                    }else {
                        progressBarAdapter.dismissDialog();
                        textViewSchedule.setText(model.getMessage());
                    }
                }else {
                    progressBarAdapter.dismissDialog();
                    showToast("something wrong to retrieve doctor schedule from patient");
                }
            }

            @Override
            public void onFailure(Call<ModelDoctorSchedule> call, Throwable t) {
                progressBarAdapter.dismissDialog();
                showToast(t.toString());
            }
        });
    }



    private void initSchedule(String start_time, String end_time, String doctorId) {
        int start = Integer.parseInt(start_time);
        int end = Integer.parseInt(end_time);

        ArrayList<String> list = new ArrayList<>();

        do {
            list.add(String.valueOf(start));
            start=start+900000;
        }while (start<=end);


//        retrieve booking schedule list
        retrieveBookingSchedule(doctorId,list);

    }
    //    for ger schedule booking list under doctor
    private void retrieveBookingSchedule(String doctorId,ArrayList<String> list) {
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<List<ModelBookingSchedule>> listCall = apiInterface.retrieve_booking_schedule_list(doctorId);
        listCall.enqueue(new Callback<List<ModelBookingSchedule>>() {
            @Override
            public void onResponse(Call<List<ModelBookingSchedule>> call, Response<List<ModelBookingSchedule>> response) {
                if (response.isSuccessful() && response.body()!=null){
                    List<ModelBookingSchedule> model = response.body();

                    Gson gson = new Gson();
                    String _bookingSchedule = gson.toJson(model);

                    JSONArray jsonArray = null;
                    try {
                        jsonArray = new JSONArray(_bookingSchedule);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    List<String> _schedule_booking_time = new ArrayList<>();
                    List<String> _position = new ArrayList<>();
                    for(int i = 0; i < Objects.requireNonNull(jsonArray).length(); i++){
                        try {
                            _schedule_booking_time.add(jsonArray.getJSONObject(i).getString("schedule_boking_time"));
                            _position.add(jsonArray.getJSONObject(i).getString("position"));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    initRecyclerView(list,_schedule_booking_time);
                }else {
                    progressBarAdapter.dismissDialog();
                    showToast("something wrong to retrieve booking list!");
                }
            }

            @Override
            public void onFailure(Call<List<ModelBookingSchedule>> call, Throwable t) {
                progressBarAdapter.dismissDialog();
                showToast(t.toString());
            }
        });
    }


    private void initRecyclerView(ArrayList<String> list,List<String> schedule_booking_time) {
        RvAdapterBookingSchedule adapterBookingSchedule = new RvAdapterBookingSchedule(getContext(),schedule_booking_time,list,this);
        recyclerViewScheduleBooking.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewScheduleBooking.setHasFixedSize(true);
        recyclerViewScheduleBooking.setAdapter(adapterBookingSchedule);
        progressBarAdapter.dismissDialog();
    }


    @SuppressLint("SetTextI18n")
    private void convert_long_to_date_and_time(String schedule_date, String start_time, String end_time, String doctor_name) {
        long millisecond = Long.parseLong(schedule_date);
        Date date = new Date(millisecond);
        @SuppressLint("SimpleDateFormat")
        Format formatter = new SimpleDateFormat("dd-MMM-yyyy");
        String _date_str = formatter.format(date);

//        convert start time
        long startTimeMilli = Long.parseLong(start_time);
        Time startTime = new Time(startTimeMilli);
        @SuppressLint("SimpleDateFormat")
        Format startTimeFormat = new SimpleDateFormat("HH:mm:ss");
        String _start_time = startTimeFormat.format(startTime);

//        convert end time
        long endTimeMilli = Long.parseLong(end_time);
        Time endTime = new Time(endTimeMilli);
        @SuppressLint("SimpleDateFormat")
        Format endTimeFormat = new SimpleDateFormat("HH:mm:ss");
        String _end_time = endTimeFormat.format(endTime);

//        set date and time in textView
        textViewSchedule.setText("Doctor name : "+doctor_name+"\n"+"Date : "+_date_str+"\n\n"+"Start Time : "+_start_time+"\n"+"End Time : "+_end_time);
    }


    @Override
    public void onClickBooking(int pos, String _time) {
        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
        builder.setMessage("Are You Sure You Want to Booking This Schedule ?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
//                       booking schedule
                        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
                        Call<ModelBookingSchedule> call = apiInterface.booking_schedule(sharedPref.getUserId(),_time,String.valueOf(pos));
                        call.enqueue(new Callback<ModelBookingSchedule>() {
                            @Override
                            public void onResponse(Call<ModelBookingSchedule> call, Response<ModelBookingSchedule> response) {
                                if (response.isSuccessful() && response.body()!=null){
                                    ModelBookingSchedule model = response.body();
                                    if (model.isSuccess()){
                                        dialog.dismiss();

                                        retrieveSchedule();
                                    }else {
                                        dialog.dismiss();
                                        showToast(model.getMessage());
                                    }
                                }else {
                                    dialog.dismiss();
                                    showToast("something wrong to booking schedule!");
                                }
                            }

                            @Override
                            public void onFailure(Call<ModelBookingSchedule> call, Throwable t) {
                                dialog.dismiss();
                                showToast(t.toString());
                            }
                        });
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }
}