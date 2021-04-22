package com.ppal007.smarthealth.fragment.patient;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ppal007.smarthealth.R;
import com.ppal007.smarthealth.adapter.progressBar.ProgressBarAdapter;
import com.ppal007.smarthealth.adapter.rvAdapter.RvAdapterRequestList;
import com.ppal007.smarthealth.adapter.rvAdapter.RvAdapterSymptomPrescription;
import com.ppal007.smarthealth.adapter.sharedPref.AdapterSharedPref;
import com.ppal007.smarthealth.interFace.PrescriptionSymtomClick;
import com.ppal007.smarthealth.model.ModelPatientRequestList;
import com.ppal007.smarthealth.model.ModelPatientSymptomPrescription;
import com.ppal007.smarthealth.model.ModelPatientSymptoms;
import com.ppal007.smarthealth.model.ModelRegisterAsDoctor;
import com.ppal007.smarthealth.retrofit.ApiClient;
import com.ppal007.smarthealth.retrofit.ApiInterface;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PrescriptionFragment extends Fragment implements PrescriptionSymtomClick {
    private static final String TAG = "PrescriptionFragment";
    private void showToast(String msg){
        Toast.makeText(getContext(), ""+msg, Toast.LENGTH_SHORT).show();
    }

    public PrescriptionFragment() {
        // Required empty public constructor
    }

    private ProgressBarAdapter progressBarAdapter;
    private AdapterSharedPref sharedPref;
    private RecyclerView recyclerViewPres;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_prescription, container, false);
        //init progress bar
        progressBarAdapter = new ProgressBarAdapter(getContext());
        //init sharedPref
        sharedPref = new AdapterSharedPref(getContext());
        //find xml
        recyclerViewPres = view.findViewById(R.id.rv_symptom_pres);

        //retrieve prescription
        retrievePrescription();

        return view;
    }

    private void retrievePrescription() {
        //progress bar
        progressBarAdapter.startLoadingDialog();
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<List<ModelPatientSymptomPrescription>> call = apiInterface.retrieve_symptom_prescription(sharedPref.getUserId());
        call.enqueue(new Callback<List<ModelPatientSymptomPrescription>>() {
            @Override
            public void onResponse(Call<List<ModelPatientSymptomPrescription>> call, Response<List<ModelPatientSymptomPrescription>> response) {
                if (response.isSuccessful() && response.body()!=null){
                    List<ModelPatientSymptomPrescription> model = response.body();

                    Gson gson = new Gson();
                    String jsonStr = gson.toJson(model);
                    JSONArray jsonArray = null;
                    try {
                        jsonArray = new JSONArray(jsonStr);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    List<String> _id = new ArrayList<>();
                    List<String> _symptomTblId = new ArrayList<>();
                    List<String> _pres = new ArrayList<>();
                    List<String> _date = new ArrayList<>();
                    List<String> _time = new ArrayList<>();
                    for(int i = 0; i < Objects.requireNonNull(jsonArray).length(); i++){
                        try {
                            _id.add(jsonArray.getJSONObject(i).getString("id"));
                            _symptomTblId.add(jsonArray.getJSONObject(i).getString("symptom_tbl_id"));
                            _pres.add(jsonArray.getJSONObject(i).getString("prescription"));
                            _date.add(jsonArray.getJSONObject(i).getString("pres_date"));
                            _time.add(jsonArray.getJSONObject(i).getString("pres_time"));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    //init model
                    ArrayList<ModelPatientSymptomPrescription> presList = new ArrayList<>();
                    for (int i = 0; i < _id.size(); i++){
                        ModelPatientSymptomPrescription model_patient_pres = new ModelPatientSymptomPrescription(
                                _id.get(i),
                                _symptomTblId.get(i),
                                _pres.get(i),
                                _date.get(i),
                                _time.get(i)
                        );
                        presList.add(model_patient_pres);

                    }

                    //init recycler view
                    initRecyclerView(presList);

                }else {
                    progressBarAdapter.dismissDialog();
                    showToast("something wrong to retrieve prescription!");
                }
            }

            @Override
            public void onFailure(Call<List<ModelPatientSymptomPrescription>> call, Throwable t) {
                Log.d(TAG, "onFailure: "+t.toString());
                progressBarAdapter.dismissDialog();
            }
        });
    }

    //for recycler view
    private void initRecyclerView(ArrayList<ModelPatientSymptomPrescription> presList) {
        RvAdapterSymptomPrescription adapter = new RvAdapterSymptomPrescription(getContext(),presList,this);
        recyclerViewPres.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewPres.setHasFixedSize(true);
        recyclerViewPres.setAdapter(adapter);
//        dismiss progress bar
        progressBarAdapter.dismissDialog();
    }

    @Override
    public void onClick(String position, String symptomId, String pres, String dt, String tm) {
        //retrieve doctor info
        progressBarAdapter.startLoadingDialog();
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<ModelRegisterAsDoctor> call = apiInterface.retrieve_doctor_info(sharedPref.getUserId());
        call.enqueue(new Callback<ModelRegisterAsDoctor>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<ModelRegisterAsDoctor> call, Response<ModelRegisterAsDoctor> response) {
                if (response.isSuccessful() && response.body()!=null){
                    ModelRegisterAsDoctor model = response.body();
                    if (model.isSuccess()){
                        String _name = model.getName();
                        String _email = model.getEmail();
                        String _phone = model.getPhone();

                        //get patient symptom
                        get_symptom(symptomId,_name,_email,_phone,pres);



                    }else {
                        progressBarAdapter.dismissDialog();
                        showToast(model.getMessage());
                    }
                }else {
                    progressBarAdapter.dismissDialog();
                    showToast("something wrong to retrieve doctor info");
                }
            }

            @Override
            public void onFailure(Call<ModelRegisterAsDoctor> call, Throwable t) {
                Log.d(TAG, "onFailure: "+t.toString());
                progressBarAdapter.dismissDialog();
            }
        });


    }

    private void get_symptom(String symptomId, String name, String email, String phone, String pres) {
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<ModelPatientSymptoms> call = apiInterface.retrieve_patient_symptom_by_symptom_tbl_id(symptomId);
        call.enqueue(new Callback<ModelPatientSymptoms>() {
            @Override
            public void onResponse(Call<ModelPatientSymptoms> call, Response<ModelPatientSymptoms> response) {
                if (response.isSuccessful() && response.body()!=null){
                    ModelPatientSymptoms model = response.body();
                    if (model.isSuccess()){
                        String _bpUp = model.getBpUp();
                        String _bpDown = model.getBpDown();
                        String _spo2 = model.getSpo2();
                        String _ecg = model.getEcg();
                        String _glBfr = model.getGlBfrEat();
                        String _glAftr = model.getGlAftrEat();
                        String _temp = model.getTemperature();

                        //init dialog
                        init_dialog(name,email,phone,_bpUp,_bpDown,_spo2,_ecg,_glBfr,_glAftr,_temp,pres);
                    }else {
                        progressBarAdapter.dismissDialog();
                        showToast(model.getMessage());
                    }
                }else {
                    progressBarAdapter.dismissDialog();
                    showToast("something wrong to retrieve symptom");
                }
            }

            @Override
            public void onFailure(Call<ModelPatientSymptoms> call, Throwable t) {
                progressBarAdapter.dismissDialog();
                Log.d(TAG, "onFailure: "+t.toString());
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void init_dialog(String name, String email, String phone, String bpUp, String bpDown, String spo2, String ecg, String glBfr, String glAftr, String temp, String pres) {
        progressBarAdapter.dismissDialog();
        //show dialog
        Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_symtomp_prescription);
        dialog.setCanceledOnTouchOutside(false);

        //find xml
        TextView textViewPresSymptom = dialog.findViewById(R.id.tv_pres_dialog);
        TextView textViewPres = dialog.findViewById(R.id.pres_symptomDialog);
        TextView textViewName = dialog.findViewById(R.id.tvSympPresName);
        TextView textViewEmail = dialog.findViewById(R.id.tvSympPresEmail);
        TextView textViewPhone = dialog.findViewById(R.id.tvSympPresPhone);
        Button buttonCancel = dialog.findViewById(R.id.btnCustomDialogSymptomPrescription_Cancel);
        Button buttonSave = dialog.findViewById(R.id.btnCustomDialogSymptomPrescriptionSave);

        //init text view
        textViewName.setText("Name : "+name);
        textViewEmail.setText(email);
        textViewPhone.setText(phone);
        textViewPres.setText(pres);
        //set symptom
        String _symptom = "BP : "+bpUp+" / "+bpDown+"\n"+
                "Temperature : "+temp+"\n"+
                "SPO2 : "+spo2+"\n"+
                "ECG : "+ecg+"\n"+
                "Gl before eating : "+glBfr+"\n"+
                "Gl after eating : "+glAftr+"\n";
        textViewPresSymptom.setText(_symptom);

        //save button click listener
        buttonSave.setOnClickListener(v -> {
            //permissions
            ActivityCompat.requestPermissions(Objects.requireNonNull(getActivity()), new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, PackageManager.PERMISSION_GRANTED);

            // create pdf
            PdfDocument myPdfDoc = new PdfDocument();
            PdfDocument.PageInfo myPageInfo = new  PdfDocument.PageInfo.Builder(300,600,1).create();
            PdfDocument.Page myPage = myPdfDoc.startPage(myPageInfo);

            Paint myPaint = new Paint();
            //final string
            String myString = "Name : "+name+"\n"+"Email : "+email+"\n"+"Phone No : "+phone+"\n\n"+pres;

            int x=10, y=25;
            for (String lines : myString.split("\n")){
                myPage.getCanvas().drawText(lines, x, y, myPaint);
                y+=myPaint.descent()-myPaint.ascent();
            }
            myPdfDoc.finishPage(myPage);

            //File outputFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), pdfName);
            //save pdf
//            String myFilePath = Environment.getExternalStorageDirectory().getPath()+"/my_prescription.pdf";
            String myFilePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString()+"/my_prescription.pdf";
            File myFile = new File(myFilePath);

            try {
                myPdfDoc.writeTo(new FileOutputStream(myFile));
                //dismiss dialog
                dialog.dismiss();
                showToast("Prescription Saved");
            } catch (IOException e) {
                e.printStackTrace();
                //dismiss dialog
                dialog.dismiss();
                showToast("Error");
                Log.d(TAG, "onClick: "+e.toString());
            }
            myPdfDoc.close();


        });

        //cancel button click listener
        buttonCancel.setOnClickListener(v -> dialog.dismiss());

        //show dialog
        dialog.show();
    }
}