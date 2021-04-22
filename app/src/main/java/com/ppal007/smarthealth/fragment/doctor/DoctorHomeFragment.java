package com.ppal007.smarthealth.fragment.doctor;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ppal007.smarthealth.R;
import com.ppal007.smarthealth.adapter.progressBar.ProgressBarAdapter;
import com.ppal007.smarthealth.adapter.rvAdapter.RvAdapterRequestList;
import com.ppal007.smarthealth.adapter.sharedPref.AdapterSharedPref;
import com.ppal007.smarthealth.interFace.RvRequestClick;
import com.ppal007.smarthealth.model.ModelPatientRequestList;
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

public class DoctorHomeFragment extends Fragment implements RvRequestClick {
    private void showToast(String msg){
        Toast.makeText(getContext(), ""+msg, Toast.LENGTH_SHORT).show();
    }

    private static final String TAG = "DoctorHomeFragment";

    public DoctorHomeFragment() {
        // Required empty public constructor
    }

    private ProgressBarAdapter progressBarAdapter;
    private AdapterSharedPref sharedPref;
    private RecyclerView recyclerViewMyPatient;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_doctor_home, container, false);
//        init progress bar
        progressBarAdapter = new ProgressBarAdapter(getContext());
        progressBarAdapter.startLoadingDialog();
//        init sharedPref
        sharedPref = new AdapterSharedPref(Objects.requireNonNull(getContext()));
//        finding xml
        recyclerViewMyPatient = view.findViewById(R.id.rvMyPatientId);
//        retrieve my patient list
        retrieveMyPatient();

//        getActivity().startService(new Intent(getActivity(), MyService.class));

        return view;
    }



    private void retrieveMyPatient() {
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<List<ModelPatientRequestList>> call = apiInterface.retrieve_my_patient(sharedPref.getUserId());
        call.enqueue(new Callback<List<ModelPatientRequestList>>() {
            @Override
            public void onResponse(Call<List<ModelPatientRequestList>> call, Response<List<ModelPatientRequestList>> response) {
                if (response.isSuccessful() && response.body()!=null){
                    List<ModelPatientRequestList> model = response.body();

                    Gson gson = new Gson();
                    String jsonStr = gson.toJson(model);

                    JSONArray jsonArray = null;
                    try {
                        jsonArray = new JSONArray(jsonStr);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    List<String> _id = new ArrayList<>();
                    List<String> _name = new ArrayList<>();
                    List<String> _email = new ArrayList<>();
                    List<String> _phone = new ArrayList<>();
                    List<String> _age = new ArrayList<>();
                    List<String> _gender = new ArrayList<>();
                    for(int i = 0; i < Objects.requireNonNull(jsonArray).length(); i++){
                        try {
                            _id.add(jsonArray.getJSONObject(i).getString("id"));
                            _name.add(jsonArray.getJSONObject(i).getString("name"));
                            _email.add(jsonArray.getJSONObject(i).getString("email"));
                            _phone.add(jsonArray.getJSONObject(i).getString("phone"));
                            _age.add(jsonArray.getJSONObject(i).getString("age"));
                            _gender.add(jsonArray.getJSONObject(i).getString("gender"));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

//                    init my patient model
                    ArrayList<ModelPatientRequestList> rvRequestList = new ArrayList<>();
                    for (int i = 0; i < _id.size(); i++){
                        ModelPatientRequestList model_patient_request = new ModelPatientRequestList(
                                _id.get(i),
                                _name.get(i),
                                _email.get(i),
                                _phone.get(i),
                                _age.get(i),
                                _gender.get(i)
                        );
                        rvRequestList.add(model_patient_request);

                    }

//                    init recycler view my patient
                    initRvMyPatient(rvRequestList);
                }else {
                    progressBarAdapter.dismissDialog();
                    showToast("something wrong to retrieve my patient list!");
                }
            }

            @Override
            public void onFailure(Call<List<ModelPatientRequestList>> call, Throwable t) {
                Log.d(TAG, "onFailure: "+t.toString());
                progressBarAdapter.dismissDialog();
            }
        });
    }

    private void initRvMyPatient(ArrayList<ModelPatientRequestList> rvRequestList) {
        RvAdapterRequestList adapterRequestList = new RvAdapterRequestList(getContext(),rvRequestList,this);
        recyclerViewMyPatient.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewMyPatient.setHasFixedSize(true);
        recyclerViewMyPatient.setAdapter(adapterRequestList);
//        dismiss progress bar
        progressBarAdapter.dismissDialog();
    }

    @Override
    public void requestClick(int position, String id, String name, String Gender, String Age, String email, String ph) {
        Fragment fragment = new SymptomListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("ex_patient_id",id);
        bundle.putString("ex_patient_name",name);
        bundle.putString("ex_patient_gender",Gender);
        bundle.putString("ex_patient_age",Age);
        bundle.putString("ex_patient_email",email);
        bundle.putString("ex_patient_phone",ph);
        fragment.setArguments(bundle);

        Objects.requireNonNull(getFragmentManager())
                .beginTransaction()
                .replace(R.id.hostFragmentId,fragment)
                .addToBackStack(null).commit();

//        showToast(id);
    }
}