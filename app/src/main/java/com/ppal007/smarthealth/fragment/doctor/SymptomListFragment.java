package com.ppal007.smarthealth.fragment.doctor;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ppal007.smarthealth.R;
import com.ppal007.smarthealth.adapter.progressBar.ProgressBarAdapter;
import com.ppal007.smarthealth.adapter.rvAdapter.RvAdapterSymptomList;
import com.ppal007.smarthealth.interFace.RvSymptomListClick;
import com.ppal007.smarthealth.model.ModelPatientRequestList;
import com.ppal007.smarthealth.model.ModelPatientSymptoms;
import com.ppal007.smarthealth.retrofit.ApiClient;
import com.ppal007.smarthealth.retrofit.ApiInterface;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SymptomListFragment extends Fragment implements RvSymptomListClick {
    private void showToast(String msg){
        Toast.makeText(getContext(), ""+msg, Toast.LENGTH_SHORT).show();
    }


    public SymptomListFragment() {
        // Required empty public constructor
    }

    private ProgressBarAdapter progressBarAdapter;
    private RecyclerView recyclerViewPatientSymptom;
    private String _id,_name,_gender,_age,_email,_phone;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_symptom_list, container, false);
//        init progress bar
        progressBarAdapter = new ProgressBarAdapter(getContext());
        progressBarAdapter.startLoadingDialog();
//        get bundle value from Doctor home fragment
        savedInstanceState = getArguments();
        if (savedInstanceState!=null){
            _id = savedInstanceState.getString("ex_patient_id");
            _name = savedInstanceState.getString("ex_patient_name");
            _gender = savedInstanceState.getString("ex_patient_gender");
            _age = savedInstanceState.getString("ex_patient_age");
            _email = savedInstanceState.getString("ex_patient_email");
            _phone = savedInstanceState.getString("ex_patient_phone");
        }

//        retrieve symptom list
        retrieveSymptomList();

//        finding xml
        recyclerViewPatientSymptom = view.findViewById(R.id.rv_patient_symptom_list);
        return view;
    }

    private void retrieveSymptomList() {
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<List<ModelPatientSymptoms>> call = apiInterface.retrieve_symptom_list(_id);
        call.enqueue(new Callback<List<ModelPatientSymptoms>>() {
            @Override
            public void onResponse(Call<List<ModelPatientSymptoms>> call, Response<List<ModelPatientSymptoms>> response) {
                if (response.isSuccessful() && response.body()!=null){
                    List<ModelPatientSymptoms> model = response.body();

                    Gson gson = new Gson();
                    String jsonStr = gson.toJson(model);

                    JSONArray jsonArray = null;
                    try {
                        jsonArray = new JSONArray(jsonStr);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    List<String> _id = new ArrayList<>();
                    //List<String> _notify_id = new ArrayList<>();
                    List<String> _bpUp = new ArrayList<>();
                    List<String> _bpDown = new ArrayList<>();
                    List<String> _spo2 = new ArrayList<>();
                    List<String> _ecg = new ArrayList<>();
                    List<String> _glBfr = new ArrayList<>();
                    List<String> _glAftr = new ArrayList<>();
                    List<String> _temperature = new ArrayList<>();
                    List<String> _date = new ArrayList<>();
                    List<String> _time = new ArrayList<>();
                    for(int i = 0; i < Objects.requireNonNull(jsonArray).length(); i++){
                        try {
                            _id.add(jsonArray.getJSONObject(i).getString("id"));
                            //_notify_id.add(jsonArray.getJSONObject(i).getString("notify_tbl_id"));
                            _bpUp.add(jsonArray.getJSONObject(i).getString("bp_up"));
                            _bpDown.add(jsonArray.getJSONObject(i).getString("bp_down"));
                            _spo2.add(jsonArray.getJSONObject(i).getString("spo2"));
                            _ecg.add(jsonArray.getJSONObject(i).getString("ecg"));
                            _glBfr.add(jsonArray.getJSONObject(i).getString("gl_befr_eat"));
                            _glAftr.add(jsonArray.getJSONObject(i).getString("gl_aftr_eat"));
                            _temperature.add(jsonArray.getJSONObject(i).getString("temperature"));
                            _date.add(jsonArray.getJSONObject(i).getString("submit_date"));
                            _time.add(jsonArray.getJSONObject(i).getString("submit_time"));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

//                    init symptom model
                    ArrayList<ModelPatientSymptoms> rvSymptomList = new ArrayList<>();
                    for (int i = 0; i < _id.size(); i++){
                        ModelPatientSymptoms model_patient_symptom = new ModelPatientSymptoms(
                                _id.get(i),
//                                _notify_id.get(i),
                                _bpUp.get(i),
                                _bpDown.get(i),
                                _spo2.get(i),
                                _ecg.get(i),
                                _glBfr.get(i),
                                _glAftr.get(i),
                                _temperature.get(i),
                                _date.get(i),
                                _time.get(i)
                        );
                        rvSymptomList.add(model_patient_symptom);

                    }

//                    init recycler view symptom
                    initRecyclerViewSymptom(rvSymptomList);

                }else {
                    showToast("something wrong to retrieve symptom list!");
                    progressBarAdapter.dismissDialog();
                }
            }

            @Override
            public void onFailure(Call<List<ModelPatientSymptoms>> call, Throwable t) {
                showToast(t.toString());
                progressBarAdapter.dismissDialog();
            }
        });
    }

    private void initRecyclerViewSymptom(ArrayList<ModelPatientSymptoms> rvSymptomList) {
        RvAdapterSymptomList adapter = new RvAdapterSymptomList(getContext(),_id,_name,rvSymptomList,this);
        recyclerViewPatientSymptom.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewPatientSymptom.setHasFixedSize(true);
        recyclerViewPatientSymptom.setAdapter(adapter);
//        dismiss progress bar
        progressBarAdapter.dismissDialog();
    }


    @Override
    public void symptomClick(int position, String symptomTblId, String bpUp, String bpDown, String spo2, String ecg, String glBfr, String glAfr, String tmperature, String submitDate) {
        Fragment fragment = new SymptomDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putString("ex_patient_id_from_symptom_list",_id);
        bundle.putString("ex_patient_name_from_symptom_list",_name);
        bundle.putString("ex_patient_gender_from_symptom_list",_gender);
        bundle.putString("ex_patient_age_from_symptom_list",_age);
        bundle.putString("ex_patient_email_from_symptom_list",_email);
        bundle.putString("ex_patient_phone_from_symptom_list",_phone);
        bundle.putString("ex_symptom_tbl_id_from_symptom_list",symptomTblId);
        bundle.putString("ex_bp_up_from_symptom_list",bpUp);
        bundle.putString("ex_bp_down_from_symptom_list",bpDown);
        bundle.putString("ex_spo2_from_symptom_list",spo2);
        bundle.putString("ex_ecg_from_symptom_list",ecg);
        bundle.putString("ex_gl_bfr_from_symptom_list",glBfr);
        bundle.putString("ex_gl_aftr_from_symptom_list",glAfr);
        bundle.putString("ex_tempture_from_symptom_list",tmperature);
        bundle.putString("ex_date_from_symptom_list",submitDate);
        fragment.setArguments(bundle);

        Objects.requireNonNull(getFragmentManager())
                .beginTransaction()
                .replace(R.id.hostFragmentId,fragment)
                .addToBackStack(null).commit();
    }
}