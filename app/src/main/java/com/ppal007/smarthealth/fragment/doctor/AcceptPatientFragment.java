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
import com.ppal007.smarthealth.adapter.rvAdapter.RvAdapterBookingList;
import com.ppal007.smarthealth.adapter.rvAdapter.RvAdapterRequestList;
import com.ppal007.smarthealth.adapter.sharedPref.AdapterSharedPref;
import com.ppal007.smarthealth.interFace.RvRequestClick;
import com.ppal007.smarthealth.model.ModelBookingSchedule;
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


public class AcceptPatientFragment extends Fragment {
    private static final String TAG = "AcceptPatientFragment";
    private void showToast(String msg){
        Toast.makeText(getContext(), ""+msg, Toast.LENGTH_SHORT).show();
    }


    public AcceptPatientFragment() {
        // Required empty public constructor
    }

    private ProgressBarAdapter progressBarAdapter;
    private AdapterSharedPref sharedPref;
    private RecyclerView recyclerViewAcceptPatient;
//    private ArrayList<ModelPatientRequestList> rvRequestList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_accept_patient, container, false);
//        init progress bar adapter
        progressBarAdapter = new ProgressBarAdapter(getContext());
//        init sharedPref
        sharedPref = new AdapterSharedPref(Objects.requireNonNull(getContext()));
//        finding xml
        recyclerViewAcceptPatient = view.findViewById(R.id.rvPatientAccept);

//        retrieve patient accept list
        retrieveBooking();
        return view;
    }

    private void retrieveBooking() {
        progressBarAdapter.startLoadingDialog();
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<List<ModelBookingSchedule>> call = apiInterface.retrieve_patient_accept_booking(sharedPref.getUserId());
        call.enqueue(new Callback<List<ModelBookingSchedule>>() {
            @Override
            public void onResponse(Call<List<ModelBookingSchedule>> call, Response<List<ModelBookingSchedule>> response) {
                if (response.isSuccessful() && response.body()!=null){
                    List<ModelBookingSchedule> model = response.body();

                    Gson gson = new Gson();
                    String json_str = gson.toJson(model);

                    JSONArray jsonArray = null;
                    try {
                        jsonArray = new JSONArray(json_str);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    List<String> _schedule_booking_time = new ArrayList<>();
                    for(int i = 0; i < Objects.requireNonNull(jsonArray).length(); i++){
                        try {
                            _schedule_booking_time.add(jsonArray.getJSONObject(i).getString("schedule_boking_time"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
//                    retrieve patient details
                    retrievePatentDetails(_schedule_booking_time);
                }else {
                    progressBarAdapter.dismissDialog();
                    showToast("something wrong to retrieve booking schedule");
                }
            }

            @Override
            public void onFailure(Call<List<ModelBookingSchedule>> call, Throwable t) {
                progressBarAdapter.dismissDialog();
                Log.d(TAG, "onFailure: "+t.toString());
            }
        });
    }

    private void retrievePatentDetails(List<String> schedule_booking_time) {
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<List<ModelPatientRequestList>> call = apiInterface.retrieve_patient_detail_match_by_booking_doctor_id(sharedPref.getUserId());
        call.enqueue(new Callback<List<ModelPatientRequestList>>() {
            @Override
            public void onResponse(Call<List<ModelPatientRequestList>> call, Response<List<ModelPatientRequestList>> response) {
                if (response.isSuccessful() && response.body()!=null){
                    List<ModelPatientRequestList> model = response.body();

                    Gson gson = new Gson();
                    String _json_str = gson.toJson(model);

                    JSONArray jsonArray = null;
                    try {
                        jsonArray = new JSONArray(_json_str);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

//                    patient details
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

//                    init model
                    ArrayList<ModelPatientRequestList> patientDetails = new ArrayList<>();
                    for (int i = 0; i < _id.size(); i++){
                        ModelPatientRequestList model_patient_request = new ModelPatientRequestList(
                                _id.get(i),
                                _name.get(i),
                                _email.get(i),
                                _phone.get(i),
                                _age.get(i),
                                _gender.get(i)
                        );
                        patientDetails.add(model_patient_request);

                    }

//                    init recycler view
                    initRv(schedule_booking_time,patientDetails);
                }else {
                    progressBarAdapter.dismissDialog();
                    showToast("something wrong to retrieve patient details");
                }
            }

            @Override
            public void onFailure(Call<List<ModelPatientRequestList>> call, Throwable t) {
                Log.d(TAG, "onFailure: "+t.toString());
                progressBarAdapter.dismissDialog();
            }
        });
    }

    private void initRv(List<String> schedule_booking_time, ArrayList<ModelPatientRequestList> patientDetails) {
        RvAdapterBookingList adapterBookingList = new RvAdapterBookingList(getContext(),schedule_booking_time,patientDetails);
        recyclerViewAcceptPatient.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewAcceptPatient.setHasFixedSize(true);
        recyclerViewAcceptPatient.setAdapter(adapterBookingList);
        progressBarAdapter.dismissDialog();
    }


}