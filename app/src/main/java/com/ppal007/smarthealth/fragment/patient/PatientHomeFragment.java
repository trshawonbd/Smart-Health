package com.ppal007.smarthealth.fragment.patient;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ppal007.smarthealth.R;
import com.ppal007.smarthealth.adapter.progressBar.ProgressBarAdapter;
import com.ppal007.smarthealth.adapter.sharedPref.AdapterSharedPref;
import com.ppal007.smarthealth.model.ModelPatientSymptoms;
import com.ppal007.smarthealth.retrofit.ApiClient;
import com.ppal007.smarthealth.retrofit.ApiInterface;

import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PatientHomeFragment extends Fragment {
    private void showToast(String msg){
        Toast.makeText(getContext(), ""+msg, Toast.LENGTH_SHORT).show();
    }


    public PatientHomeFragment() {
        // Required empty public constructor
    }

    private AdapterSharedPref sharedPref;
    private ProgressBarAdapter progressBarAdapter;
    private TextView textViewTitle;
    private EditText editTextBpUp,editTextBpDown,editTextTemperature,editTextSpo2,editTextEcg,editTextGlBfrEat,editTextGlAftrEat;
    private Button buttonSubmit,buttonSensore;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_patient_home, container, false);
//        init progress bar adapter
        progressBarAdapter = new ProgressBarAdapter(getContext());
//        init sharedPref
        sharedPref = new AdapterSharedPref(Objects.requireNonNull(getContext()));
//        finding xml
        textViewTitle = view.findViewById(R.id.tvPatientTitle);
        editTextBpUp = view.findViewById(R.id.et_bp_up_patient);
        editTextBpDown = view.findViewById(R.id.et_bp_down_patient);
        editTextTemperature = view.findViewById(R.id.et_temperature_patient);
        editTextSpo2 = view.findViewById(R.id.et_spo2_patient);
        editTextEcg = view.findViewById(R.id.et_ecg_patient);
        editTextGlBfrEat = view.findViewById(R.id.et_gl_bfr_eat_patient);
        editTextGlAftrEat = view.findViewById(R.id.gl_aftr_eat_patient);
        buttonSubmit = view.findViewById(R.id.btnSubmitPatientId);
        buttonSensore = view.findViewById(R.id.btnSubmitSensorId);
//        set title
        textViewTitle.setText(sharedPref.getUserName());

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        submit button click listener
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initBtnSubmit();
            }
        });
        //sensor click event
        buttonSensore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast("UpComing...");
            }
        });
    }

//    for button submit
    private void initBtnSubmit() {
        String _bpUp = editTextBpUp.getText().toString().trim();
        String _bpDown = editTextBpDown.getText().toString().trim();
        String _temperature = editTextTemperature.getText().toString();
        String _spo2 = editTextSpo2.getText().toString();
        String _ecg = editTextEcg.getText().toString();
        String glBfr = editTextGlBfrEat.getText().toString();
        String glAftr = editTextGlAftrEat.getText().toString();

        if (_bpUp.isEmpty()){
            editTextBpUp.setError("Please Enter Your Up BP");
            editTextBpUp.requestFocus();
        }else if (_bpDown.isEmpty()){
            editTextBpDown.setError("Please Enter Your Down BP");
            editTextBpDown.requestFocus();
        }else if (_temperature.isEmpty()){
            editTextTemperature.setError("Please Enter Your Temperature");
            editTextTemperature.requestFocus();
        }else if (_spo2.isEmpty()){
            editTextSpo2.setError("Please Enter SPO2");
            editTextSpo2.requestFocus();
        }else if (_ecg.isEmpty()){
            editTextEcg.setError("Please Enter ECG");
            editTextEcg.requestFocus();
        }else if (glBfr.isEmpty()){
            editTextGlBfrEat.setError("Please Enter Glucose Before Eating");
            editTextGlBfrEat.requestFocus();
        }else if (glAftr.isEmpty()){
            editTextGlAftrEat.setError("Please Enter Glucose After Eating");
            editTextGlAftrEat.requestFocus();
        }else {
//            show progress bar
            progressBarAdapter.startLoadingDialog();
//            get current time
            String _crnt_time = get_current_time();
//            insert data
            ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
            Call<ModelPatientSymptoms> call = apiInterface.send_patient_symptoms_to_doctor(
                    sharedPref.getUserId(),
                    _bpUp,
                    _bpDown,
                    _spo2,
                    _ecg,
                    glBfr,
                    glAftr,
                    _temperature,
                    _crnt_time
            );
            call.enqueue(new Callback<ModelPatientSymptoms>() {
                @Override
                public void onResponse(Call<ModelPatientSymptoms> call, Response<ModelPatientSymptoms> response) {
                    if (response.isSuccessful() && response.body()!=null){
                        ModelPatientSymptoms model = response.body();
                        if (model.isSuccess()){
//                            hide progress bar
                            progressBarAdapter.dismissDialog();

//                            clear views
                            editTextBpUp.setText("");
                            editTextBpDown.setText("");
                            editTextTemperature.setText("");
                            editTextSpo2.setText("");
                            editTextEcg.setText("");
                            editTextGlBfrEat.setText("");
                            editTextGlAftrEat.setText("");
                        }else {
                            progressBarAdapter.dismissDialog();
                            showToast(model.getMessage());
                        }
                    }else {
                        progressBarAdapter.dismissDialog();
                        showToast("something wrong to insert symptoms!");
                    }
                }

                @Override
                public void onFailure(Call<ModelPatientSymptoms> call, Throwable t) {
                    showToast(t.toString());
                    progressBarAdapter.dismissDialog();
                }
            });
        }

    }

//    get current time
    private String get_current_time(){
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String _tm = sdf.format(new Date());

        Time t = Time.valueOf(_tm);
        long l = t.getTime();

        return Long.toString(l);

    }
}