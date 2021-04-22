package com.ppal007.smarthealth.fragment.doctor;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ppal007.smarthealth.R;
import com.ppal007.smarthealth.adapter.progressBar.ProgressBarAdapter;
import com.ppal007.smarthealth.model.ModelPatientSymptomPrescription;
import com.ppal007.smarthealth.model.ModelPatientSymptoms;
import com.ppal007.smarthealth.retrofit.ApiClient;
import com.ppal007.smarthealth.retrofit.ApiInterface;

import java.sql.Time;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SymptomDetailsFragment extends Fragment {
    private void showToast(String msg){
        Toast.makeText(getContext(), ""+msg, Toast.LENGTH_SHORT).show();
    }

    public SymptomDetailsFragment() {
        // Required empty public constructor
    }

    private ProgressBarAdapter progressBarAdapter;
    private String _patientId,_patientName,_patientGender,_patientAge,_patientEmail,_patientPhone,
            _symptomTblId,_bpUp,_bpDown,_spo2,_ecg,_glBfr,_glAftr,_tmprature,_dt;
    private TextView textViewBpUp,textViewBpDown,textViewTemperature,textViewSpo2,textViewEcg,textViewGlBfr,textViewGlAftr,
    textViewPatientName,textViewPatientGender,textViewPatientAge,textViewPatientEmail,textViewPatientPhone,textViewDate;
    private Button buttonSetValue,buttonPrescription;

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_symptom_details, container, false);
//        get bundle value from symptom list fragment
        savedInstanceState = getArguments();
        if (savedInstanceState!=null){
            _patientId = savedInstanceState.getString("ex_patient_id_from_symptom_list");
            _patientName = savedInstanceState.getString("ex_patient_name_from_symptom_list");
            _patientGender = savedInstanceState.getString("ex_patient_gender_from_symptom_list");
            _patientAge = savedInstanceState.getString("ex_patient_age_from_symptom_list");
            _patientEmail = savedInstanceState.getString("ex_patient_email_from_symptom_list");
            _patientPhone = savedInstanceState.getString("ex_patient_phone_from_symptom_list");
            _symptomTblId = savedInstanceState.getString("ex_symptom_tbl_id_from_symptom_list");
            _bpUp = savedInstanceState.getString("ex_bp_up_from_symptom_list");
            _bpDown = savedInstanceState.getString("ex_bp_down_from_symptom_list");
            _spo2 = savedInstanceState.getString("ex_spo2_from_symptom_list");
            _ecg = savedInstanceState.getString("ex_ecg_from_symptom_list");
            _glBfr = savedInstanceState.getString("ex_gl_bfr_from_symptom_list");
            _glAftr = savedInstanceState.getString("ex_gl_aftr_from_symptom_list");
            _tmprature = savedInstanceState.getString("ex_tempture_from_symptom_list");
            _dt = savedInstanceState.getString("ex_date_from_symptom_list");
        }
//        init progress bar
        progressBarAdapter = new ProgressBarAdapter(getContext());
//        finding xml
        textViewBpUp = view.findViewById(R.id.tvBpUpDetails);
        textViewBpDown = view.findViewById(R.id.tvBpDownDetails);
        textViewTemperature = view.findViewById(R.id.tvTemperatureDetails);
        textViewSpo2 = view.findViewById(R.id.tvSpo2Details);
        textViewEcg = view.findViewById(R.id.tvEcgDetails);
        textViewGlBfr = view.findViewById(R.id.tvGlBfrDetails);
        textViewGlAftr = view.findViewById(R.id.tvGlAftrDetails);
        textViewPatientName = view.findViewById(R.id.tv_patient_name);
        textViewPatientGender = view.findViewById(R.id.tv_patient_gender);
        textViewPatientAge = view.findViewById(R.id.tv_patient_age);
        textViewPatientEmail = view.findViewById(R.id.tv_patient_email);
        textViewPatientPhone = view.findViewById(R.id.tv_patient_phone);
        buttonSetValue = view.findViewById(R.id.btnSetValueForNotification);
        textViewDate = view.findViewById(R.id.tv_patient_date);
        buttonPrescription = view.findViewById(R.id.btnPrescriptionForNotification);

//        init views
        textViewPatientName.setText("Name : "+_patientName);
        textViewPatientGender.setText("Gender : "+_patientGender);
        textViewPatientAge.setText("Age : "+_patientAge);
        textViewPatientEmail.setText("Email : "+_patientEmail);
        textViewPatientPhone.setText("Phone : "+_patientPhone);
//        convert long to date
        long millisecond = Long.parseLong(_dt);
        Date date = new Date(millisecond);
        @SuppressLint("SimpleDateFormat") Format formatter = new SimpleDateFormat("dd-MMM-yyyy");
        textViewDate.setText("Date : "+formatter.format(date));

        textViewBpUp.setText(_bpUp);
        textViewBpDown.setText(_bpDown);
        textViewTemperature.setText(_tmprature);
        textViewSpo2.setText(_spo2);
        textViewEcg.setText(_ecg);
        textViewGlBfr.setText(_glBfr);
        textViewGlAftr.setText(_glAftr);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        prescription button click listener
        buttonPrescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                showToast(_symptomTblId);
                Dialog dialog = new Dialog(getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.custom_symptom_prescription);
                dialog.setCanceledOnTouchOutside(false);

                //find xml
                EditText editTextPrescription = dialog.findViewById(R.id.etSymptomPrescription);
                Button btnCancel = dialog.findViewById(R.id.btnCustomDialogSymptomPrescriptionCancel);
                Button btnSend = dialog.findViewById(R.id.btnCustomDialogSymptomPrescriptionSend);
                View _view = dialog.findViewById(R.id.view_pres_id);
                ProgressBar progressBar = dialog.findViewById(R.id.progressCustomPressId);

                //button cancel click listener
                btnCancel.setOnClickListener(v1 -> dialog.dismiss());
                //button send click listener
                btnSend.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String _press = editTextPrescription.getText().toString();

                        if (_press.isEmpty()){
                            editTextPrescription.setError("Prescription is empty!");
                            editTextPrescription.requestFocus();
                        }else {
                            String _time = get_current_time();
                            String _year_month = get_year_month();
                            //send prescription
                            _view.setVisibility(View.GONE);
                            progressBar.setVisibility(View.VISIBLE);
                            ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
                            Call<ModelPatientSymptomPrescription> call = apiInterface.send_prescription_to_patient_symptom(_symptomTblId,_press,_time,_year_month);
                            call.enqueue(new Callback<ModelPatientSymptomPrescription>() {
                                @Override
                                public void onResponse(Call<ModelPatientSymptomPrescription> call, Response<ModelPatientSymptomPrescription> response) {
                                    if (response.isSuccessful() && response.body()!=null){
                                        ModelPatientSymptomPrescription model = response.body();
                                        if (model.isSuccess()){
                                            progressBar.setVisibility(View.GONE);
                                            _view.setVisibility(View.VISIBLE);
                                            dialog.dismiss();
                                        }else {
                                            showToast(model.getMessage());
                                            progressBar.setVisibility(View.GONE);
                                            _view.setVisibility(View.VISIBLE);
                                        }
                                    }else {
                                        showToast("something wrong to send prescription");
                                        progressBar.setVisibility(View.GONE);
                                        _view.setVisibility(View.VISIBLE);
                                    }
                                }

                                @Override
                                public void onFailure(Call<ModelPatientSymptomPrescription> call, Throwable t) {
                                    showToast(t.toString());
                                    progressBar.setVisibility(View.GONE);
                                    _view.setVisibility(View.VISIBLE);
                                }
                            });
                        }
                    }
                });

                // show dialog
                dialog.show();
            }
        });
//        button value set click listener
        buttonSetValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBarAdapter.startLoadingDialog();
//                get data
                ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
                Call<ModelPatientSymptoms> cal = apiInterface.retrieve_patient_default_value(_patientId);
                cal.enqueue(new Callback<ModelPatientSymptoms>() {
                    @Override
                    public void onResponse(Call<ModelPatientSymptoms> call, Response<ModelPatientSymptoms> response) {
                        if (response.isSuccessful() && response.body()!=null){
                            ModelPatientSymptoms model = response.body();
                            String _id;
                            String _bp_up;
                            String _bp_down;
                            String _tmp;
                            String _spo2;
                            String _ecg;
                            String _glBfr;
                            String _glAfr;
                            if (model.isSuccess()){
                                _id = model.getId();
                                _bp_up = model.getBpUp();
                                _bp_down = model.getBpDown();
                                _tmp = model.getTemperature();
                                _spo2 = model.getSpo2();
                                _ecg = model.getEcg();
                                _glBfr = model.getGlBfrEat();
                                _glAfr = model.getGlAftrEat();


                            }else {
                                _id = "";
                                _bp_up = "";
                                _bp_down = "";
                                _tmp = "";
                                _spo2 = "";
                                _ecg = "";
                                _glBfr = "";
                                _glAfr = "";

                            }
                            progressBarAdapter.dismissDialog();
                            initBtnSerDefaultValue(_id,_bp_up,_bp_down,_tmp,_spo2,_ecg,_glBfr,_glAfr);
                        }else {
                            progressBarAdapter.dismissDialog();
                            showToast("something wrong to retrieve default value");
                        }
                    }

                    @Override
                    public void onFailure(Call<ModelPatientSymptoms> call, Throwable t) {
                        progressBarAdapter.dismissDialog();
                        showToast(t.toString());
                    }
                });
                //initBtnSerDefaultValue();
            }
        });
    }

    private void initBtnSerDefaultValue(String id,String bpUp,String bpDown,String tmp,String spo2,String ecg,String glBfr,String glAfr) {
        Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog_for_set_default_value);
        dialog.setCanceledOnTouchOutside(false);

//        find xml
        TextView textViewTitle = dialog.findViewById(R.id.tv_custom_dialog_set_default_value_title);
        View view = dialog.findViewById(R.id.viewSetValue_id);
        ProgressBar progressBar = dialog.findViewById(R.id.progressCustom_set_val);
        EditText editTextBpUp = dialog.findViewById(R.id.etBpUpDetails_set_val);
        EditText editTextBpDown = dialog.findViewById(R.id.etBpDownDetails_set_val);
        EditText editTextTemperature = dialog.findViewById(R.id.etTemperature_set_val);
        EditText editTextSPO2 = dialog.findViewById(R.id.etSpo2_set_val);
        EditText editTextEcg = dialog.findViewById(R.id.etEcg_set_val);
        EditText editTextGlBfr = dialog.findViewById(R.id.etGlBfr_set_val);
        EditText editTextGlAftr = dialog.findViewById(R.id.etGlAftr_set_val);
        Button cancel = dialog.findViewById(R.id.btnCall_set_val_dialogId);
        Button submit = dialog.findViewById(R.id.btnSubmit_custom_dialog_set_val);
        Button update = dialog.findViewById(R.id.btnUpdate_custom_dialog_set_val);

        cancel.setEnabled(true);
        submit.setEnabled(true);
        update.setEnabled(true);

//        init title
        textViewTitle.setText(_patientName);
        if (id.equals("") && bpUp.equals("") && bpDown.equals("") && tmp.equals("") && spo2.equals("") && ecg.equals("") && glBfr.equals("") && glAfr.equals("")){
//            init views
            submit.setVisibility(View.VISIBLE);
            update.setVisibility(View.GONE);
            // submit button click listener
            submit.setOnClickListener(v -> {
                String _bpUp = editTextBpUp.getText().toString();
                String _bpDown = editTextBpDown.getText().toString();
                String _temprature = editTextTemperature.getText().toString();
                String _spo2 = editTextSPO2.getText().toString();
                String _ecg = editTextEcg.getText().toString();
                String _glBfr = editTextGlBfr.getText().toString();
                String _glAftr = editTextGlAftr.getText().toString();

                if (_bpUp.isEmpty()){
                    editTextBpUp.setError("Enter BP Up");
                    editTextBpUp.requestFocus();
                }else if (_bpDown.isEmpty()){
                    editTextBpDown.setError("Enter BP Down");
                    editTextBpDown.requestFocus();
                }else if (_temprature.isEmpty()){
                    editTextTemperature.setError("Enter Temperature");
                    editTextTemperature.requestFocus();
                }else if (_spo2.isEmpty()){
                    editTextSPO2.setError("Enter SPO2");
                    editTextSPO2.requestFocus();
                }else if (_ecg.isEmpty()){
                    editTextEcg.setError("Enter ECG");
                    editTextEcg.requestFocus();
                }else if (_glBfr.isEmpty()){
                    editTextGlBfr.setError("Enter Before Eating Glucose");
                    editTextGlBfr.requestFocus();
                }else if (_glAftr.isEmpty()){
                    editTextGlAftr.setError("Enter After Eating Glucose");
                    editTextGlAftr.requestFocus();
                }else {
//                    insert data to database
                    insertDefaultValueToDatabase(dialog,view,progressBar,_patientId,_bpUp,_bpDown,_temprature,_spo2,_ecg,_glBfr,_glAftr);

                }
            });
        }else {
//            init views for not empty
            submit.setVisibility(View.GONE);
            update.setVisibility(View.VISIBLE);

            editTextBpUp.setText(bpUp);
            editTextBpDown.setText(bpDown);
            editTextTemperature.setText(tmp);
            editTextSPO2.setText(spo2);
            editTextEcg.setText(ecg);
            editTextGlBfr.setText(glBfr);
            editTextGlAftr.setText(glAfr);

//            update button click listener
            update.setOnClickListener(v -> {
                String _bpUp = editTextBpUp.getText().toString();
                String _bpDown = editTextBpDown.getText().toString();
                String _temprature = editTextTemperature.getText().toString();
                String _spo2 = editTextSPO2.getText().toString();
                String _ecg = editTextEcg.getText().toString();
                String _glBfr = editTextGlBfr.getText().toString();
                String _glAftr = editTextGlAftr.getText().toString();

                if (_bpUp.isEmpty()){
                    editTextBpUp.setError("Enter BP Up");
                    editTextBpUp.requestFocus();
                }else if (_bpDown.isEmpty()){
                    editTextBpDown.setError("Enter BP Down");
                    editTextBpDown.requestFocus();
                }else if (_temprature.isEmpty()){
                    editTextTemperature.setError("Enter Temperature");
                    editTextTemperature.requestFocus();
                }else if (_spo2.isEmpty()){
                    editTextSPO2.setError("Enter SPO2");
                    editTextSPO2.requestFocus();
                }else if (_ecg.isEmpty()){
                    editTextEcg.setError("Enter ECG");
                    editTextEcg.requestFocus();
                }else if (_glBfr.isEmpty()){
                    editTextGlBfr.setError("Enter Before Eating Glucose");
                    editTextGlBfr.requestFocus();
                }else if (_glAftr.isEmpty()){
                    editTextGlAftr.setError("Enter After Eating Glucose");
                    editTextGlAftr.requestFocus();
                }else {
//                    insert data to database
                    insertDefaultValueToDatabase(dialog,view,progressBar,_patientId,_bpUp,_bpDown,_temprature,_spo2,_ecg,_glBfr,_glAftr);

                }
            });
        }

//        cancel button click listener
        cancel.setOnClickListener(v -> dialog.dismiss());

        // show dialog
        dialog.show();

    }

    private void insertDefaultValueToDatabase(Dialog dialog, View view, ProgressBar progressBar, String patientId, String bpUp, String bpDown, String temprature, String spo2, String ecg, String glBfr, String glAftr) {
        view.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<ModelPatientSymptoms> call = apiInterface.set_patient_symptoms_from_doctor(
                patientId,
                bpUp,
                bpDown,
                temprature,
                spo2,
                ecg,
                glBfr,
                glAftr
        );
        call.enqueue(new Callback<ModelPatientSymptoms>() {
            @Override
            public void onResponse(Call<ModelPatientSymptoms> call, Response<ModelPatientSymptoms> response) {
                if (response.isSuccessful() && response.body()!=null){
                    ModelPatientSymptoms model = response.body();
                    if (model.isSuccess()){
                        progressBar.setVisibility(View.GONE);
                        view.setVisibility(View.VISIBLE);
                        dialog.dismiss();
                        showToast(model.getMessage());
                    }else {
                        progressBar.setVisibility(View.GONE);
                        view.setVisibility(View.VISIBLE);
                        showToast(model.getMessage());
                    }
                }else {
                    progressBar.setVisibility(View.GONE);
                    view.setVisibility(View.VISIBLE);
                    showToast("something wrong to set patient default value");
                }
            }

            @Override
            public void onFailure(Call<ModelPatientSymptoms> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                view.setVisibility(View.VISIBLE);
                showToast(t.toString());
            }
        });
    }

    private String get_current_time(){
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String _tm = sdf.format(new Date());

        Time t = Time.valueOf(_tm);
        long l = t.getTime();

        return Long.toString(l);

    }

    private String get_year_month(){
        String currentDate = new SimpleDateFormat("MMM-yyyy", Locale.getDefault()).format(new Date());

        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat format = new SimpleDateFormat("MMM-yyyy");
        String strLong = null;
        try {

            Date date = format.parse(currentDate);
            assert date != null;
            long milliseconds = date.getTime();
            strLong = Long.toString(milliseconds);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return strLong;
    }
}