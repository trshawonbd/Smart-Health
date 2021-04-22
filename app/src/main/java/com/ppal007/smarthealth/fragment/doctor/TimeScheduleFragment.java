package com.ppal007.smarthealth.fragment.doctor;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.ppal007.smarthealth.R;
import com.ppal007.smarthealth.activity.LoginActivity;
import com.ppal007.smarthealth.adapter.progressBar.ProgressBarAdapter;
import com.ppal007.smarthealth.adapter.sharedPref.AdapterSharedPref;
import com.ppal007.smarthealth.model.ModelDoctorSchedule;
import com.ppal007.smarthealth.retrofit.ApiClient;
import com.ppal007.smarthealth.retrofit.ApiInterface;

import java.sql.Time;
import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class TimeScheduleFragment extends Fragment {
    private void showToast(String msg){
        Toast.makeText(getContext(), ""+msg, Toast.LENGTH_SHORT).show();
    }


    public TimeScheduleFragment() {
        // Required empty public constructor
    }

    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private AdapterSharedPref sharedPref;
    private ProgressBarAdapter progressBarAdapter;
    private TextView textViewSchedule;
    private Button buttonCreateSchedule,buttonDeleteSchedule;

    private String str_current_date_Long;
    private String str_start_time_Long;
    private String str_end_time_Long;

    private boolean schedule_bool = false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_time_schedule, container, false);
//        init progress bar adapter
        progressBarAdapter = new ProgressBarAdapter(getContext());
        //progressBarAdapter.startLoadingDialog();
//        init sharedPref
        sharedPref = new AdapterSharedPref(Objects.requireNonNull(getContext()));
//        finding xml
        textViewSchedule = view.findViewById(R.id.tvSchedule_doctor);
        buttonCreateSchedule = view.findViewById(R.id.btnCreateSchedule);
        buttonDeleteSchedule = view.findViewById(R.id.btnDeleteSchedule);

//        retrieve schedule
        retrieveSchedule();


        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        button create schedule click listener
        buttonCreateSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.custom_dialog_doctor_schedule);
                dialog.setCanceledOnTouchOutside(false);

//                find xml
                TextView textViewSelectDate = dialog.findViewById(R.id.tvCreateScheduleSelectDate);
                TextView textViewStartTime = dialog.findViewById(R.id.tvCreateScheduleSelectStartTime);
                TextView textViewEndTime = dialog.findViewById(R.id.tvCreateScheduleSelectEndTime);
                Button buttonCancel = dialog.findViewById(R.id.btnCall_schedule_dialogId);
                Button buttonCreate = dialog.findViewById(R.id.btnCreateSchedule_custom_dialog);
                View view1=dialog.findViewById(R.id.viewSchedule_id);
                ProgressBar progressBar = dialog.findViewById(R.id.progressCustom_schedule);

//                select date click listener
                textViewSelectDate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Calendar calendar = Calendar.getInstance();
                        int year = calendar.get(Calendar.YEAR);
                        int month = calendar.get(Calendar.MONTH);
                        int day = calendar.get(Calendar.DAY_OF_MONTH);

                        DatePickerDialog dialog = new DatePickerDialog(getContext(),
                                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                                mDateSetListener,
                                year, month, day);
                        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialog.show();
                    }
                });
                mDateSetListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        month = month + 1;
                        String date = day + "-" + month + "-" + year;
                        textViewSelectDate.setText(date);

                    }
                };

//                select start time
                textViewStartTime.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Calendar calenderInstance = Calendar.getInstance();
                        int hr = calenderInstance.get(Calendar.HOUR_OF_DAY);
                        int min = calenderInstance.get(Calendar.MINUTE);
                        TimePickerDialog.OnTimeSetListener onTimeListner = new TimePickerDialog.OnTimeSetListener() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                if (view.isShown()) {
                                    calenderInstance.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                    calenderInstance.set(Calendar.MINUTE, minute);

//                                    set start time
                                    textViewStartTime.setText(hourOfDay+":"+minute+":00");
                                }
                            }
                        };
                        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                                android.R.style.Theme_Holo_Light_Dialog_NoActionBar,
                                onTimeListner, hr, min, true);


                        timePickerDialog.setTitle("Set Start Time");
                        Objects.requireNonNull(timePickerDialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
                        timePickerDialog.show();
                    }
                });

//                set end time click listener
                textViewEndTime.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Calendar calenderInstance = Calendar.getInstance();
                        int hr = calenderInstance.get(Calendar.HOUR_OF_DAY);
                        int min = calenderInstance.get(Calendar.MINUTE);
                        TimePickerDialog.OnTimeSetListener onTimeListner = new TimePickerDialog.OnTimeSetListener() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                if (view.isShown()) {
                                    calenderInstance.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                    calenderInstance.set(Calendar.MINUTE, minute);

//                                    set start time
                                    textViewEndTime.setText(hourOfDay+":"+minute+":00");
                                }
                            }
                        };
                        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                                android.R.style.Theme_Holo_Light_Dialog_NoActionBar,
                                onTimeListner, hr, min, true);


                        timePickerDialog.setTitle("Set End Time");
                        Objects.requireNonNull(timePickerDialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
                        timePickerDialog.show();
                    }
                });


//                create button click listener
                buttonCreate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String _date = textViewSelectDate.getText().toString();
                        String _startTime = textViewStartTime.getText().toString();
                        String _endTime = textViewEndTime.getText().toString();

                        if (_date.equals("Select Date")){
                            showToast("Please Select Date");
                        }else if (_startTime.equals("Select Start Time")){
                            showToast("Please Select Start Time");
                        }else if (_endTime.equals("Select End Time")){
                            showToast("Please Select End Time");
                        } else {
                            view1.setVisibility(View.GONE);
                            progressBar.setVisibility(View.VISIBLE);

                            convertDateToLong(_date);
                            convert_start_time_to_long(_startTime);
                            convert_end_time_to_long(_endTime);
//                            insert to database
                            ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
                            Call<ModelDoctorSchedule> call= apiInterface.create_doctor_schedule(sharedPref.getUserId(),str_current_date_Long,str_start_time_Long,str_end_time_Long);
                            call.enqueue(new Callback<ModelDoctorSchedule>() {
                                @Override
                                public void onResponse(Call<ModelDoctorSchedule> call, Response<ModelDoctorSchedule> response) {
                                    if (response.isSuccessful() && response.body()!=null){
                                        ModelDoctorSchedule model = response.body();
                                        if (model.isSuccess()){
                                            view1.setVisibility(View.VISIBLE);
                                            progressBar.setVisibility(View.GONE);

//                                            call retrieve schedule method
                                            retrieveSchedule();
                                            dialog.dismiss();
                                        }else {
                                            view1.setVisibility(View.VISIBLE);
                                            progressBar.setVisibility(View.GONE);
                                            showToast(model.getMessage());
                                            dialog.dismiss();
                                        }
                                    }else {
                                        view1.setVisibility(View.VISIBLE);
                                        progressBar.setVisibility(View.GONE);
                                        showToast("something wrong to create schedule");
                                        dialog.dismiss();
                                    }
                                }

                                @Override
                                public void onFailure(Call<ModelDoctorSchedule> call, Throwable t) {
                                    view1.setVisibility(View.VISIBLE);
                                    progressBar.setVisibility(View.GONE);
                                    showToast(t.toString());
                                    dialog.dismiss();
                                }
                            });
                        }

                    }
                });


//                cancel button click listener
                buttonCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });


                // show dialog
                dialog.show();
            }
        });

//        button delete schedule click listener
        buttonDeleteSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("Are You Sure You Want to Delete ?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface _dialog, int id) {
//                                delete schedule
                                ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
                                Call<ModelDoctorSchedule> call = apiInterface.delete_doctor_schedule(sharedPref.getUserId());
                                call.enqueue(new Callback<ModelDoctorSchedule>() {
                                    @Override
                                    public void onResponse(Call<ModelDoctorSchedule> call, Response<ModelDoctorSchedule> response) {
                                        if (response.isSuccessful() && response.body()!=null){
                                            ModelDoctorSchedule model = response.body();
                                            if (model.isSuccess()){
                                                showToast(model.getMessage());
                                                _dialog.dismiss();

                                                retrieveSchedule();
                                            }else {
                                                showToast(model.getMessage());
                                                _dialog.dismiss();
                                            }
                                        }else {
                                            showToast("something wrong to delete schedule");
                                            _dialog.dismiss();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<ModelDoctorSchedule> call, Throwable t) {
                                        showToast(t.toString());
                                        _dialog.dismiss();
                                    }
                                });
                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface _dialog, int id) {
                        _dialog.dismiss();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

    }

//    for retrieve schedule
    private void retrieveSchedule() {
        progressBarAdapter.startLoadingDialog();
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<ModelDoctorSchedule> call = apiInterface.retrieve_doctor_schedule(sharedPref.getUserId());
        call.enqueue(new Callback<ModelDoctorSchedule>() {
            @Override
            public void onResponse(Call<ModelDoctorSchedule> call, Response<ModelDoctorSchedule> response) {
                if (response.isSuccessful() && response.body()!=null){
                    ModelDoctorSchedule model = response.body();
                    if (model.isSuccess()){
                        progressBarAdapter.dismissDialog();
                        String _date_long_str = model.getScheduleDate();
                        String _start_time_long_str = model.getScheduleTimeStart();
                        String _end_time_long_str = model.getScheduleTimeEnd();

                        schedule_bool = true;

//                        convert long to date and time
                        convert_long_to_date_and_time(_date_long_str,_start_time_long_str,_end_time_long_str);
                    }else {
                        progressBarAdapter.dismissDialog();
                        textViewSchedule.setText(model.getMessage());
                        schedule_bool = false;

                        buttonCreateSchedule.setVisibility(View.VISIBLE);
                        buttonDeleteSchedule.setVisibility(View.GONE);
                    }
                }else {
                    progressBarAdapter.dismissDialog();
                    showToast("something wrong to retrieve schedule");
                    buttonCreateSchedule.setVisibility(View.VISIBLE);
                    buttonDeleteSchedule.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<ModelDoctorSchedule> call, Throwable t) {
                progressBarAdapter.dismissDialog();
                showToast(t.toString());
                buttonCreateSchedule.setVisibility(View.VISIBLE);
                buttonDeleteSchedule.setVisibility(View.GONE);
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void convert_long_to_date_and_time(String date_long_str, String start_time_long_str, String end_time_long_str) {
//        convert date
        long millisecond = Long.parseLong(date_long_str);
        Date date = new Date(millisecond);
        @SuppressLint("SimpleDateFormat")
        Format formatter = new SimpleDateFormat("dd-MMM-yyyy");
        String _date_str = formatter.format(date);

//        convert start time
        long startTimeMilli = Long.parseLong(start_time_long_str);
        Time startTime = new Time(startTimeMilli);
        @SuppressLint("SimpleDateFormat")
        Format startTimeFormat = new SimpleDateFormat("HH:mm:ss");
        String _start_time = startTimeFormat.format(startTime);

//        convert end time
        long endTimeMilli = Long.parseLong(end_time_long_str);
        Time endTime = new Time(endTimeMilli);
        @SuppressLint("SimpleDateFormat")
        Format endTimeFormat = new SimpleDateFormat("HH:mm:ss");
        String _end_time = endTimeFormat.format(endTime);

//        set date and time in textView
        textViewSchedule.setText("Date : "+_date_str+"\n\n"+"Start Time : "+_start_time+"\n"+"End Time : "+_end_time);

//        init views
        buttonCreateSchedule.setVisibility(View.GONE);
        buttonDeleteSchedule.setVisibility(View.VISIBLE);
    }

    private void convertDateToLong(String dt) {

        @SuppressLint("SimpleDateFormat")
        DateFormat df = new SimpleDateFormat("dd-MM-yyy");
        try {
            long date = df.parse(dt).getTime();
            str_current_date_Long = Long.toString(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }


    }

    private void convert_start_time_to_long(String tm){
        Time t = Time.valueOf(tm);
        long l = t.getTime();
        str_start_time_Long = Long.toString(l);
    }
    private void convert_end_time_to_long(String tm){
        Time t = Time.valueOf(tm);
        long l = t.getTime();
        str_end_time_Long = Long.toString(l);
    }


}