package com.ppal007.smarthealth.fragment.doctor;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ppal007.smarthealth.R;
import com.ppal007.smarthealth.activity.HomeActivity;
import com.ppal007.smarthealth.adapter.progressBar.ProgressBarAdapter;
import com.ppal007.smarthealth.adapter.rvAdapter.RvAdapterRequestList;
import com.ppal007.smarthealth.adapter.sharedPref.AdapterSharedPref;
import com.ppal007.smarthealth.interFace.RvRequestClick;
import com.ppal007.smarthealth.model.ModelAcceptPatient;
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


public class PatientRequestFragment extends Fragment implements RvRequestClick {
    private void showToast(String msg){
        Toast.makeText(getContext(), ""+msg, Toast.LENGTH_SHORT).show();
    }


    public PatientRequestFragment() {
        // Required empty public constructor
    }

    private ProgressBarAdapter progressBarAdapter;
    private AdapterSharedPref sharedPref;
    private RecyclerView recyclerViewPatientRequest;
    private ArrayList<ModelPatientRequestList> rvRequestList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_patient_request, container, false);
//        init progress bar adapter
        progressBarAdapter = new ProgressBarAdapter(getContext());
        progressBarAdapter.startLoadingDialog();
//        init sharedPref
        sharedPref = new AdapterSharedPref(Objects.requireNonNull(getContext()));
//        finding xml
        recyclerViewPatientRequest = view.findViewById(R.id.rvPatientRequest);
//        retrieve requestList
        retrieveRequestList();

        return view;
    }

//    for retrieve patient request list
    private void retrieveRequestList() {
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<List<ModelPatientRequestList>> call = apiInterface.retrieve_patient_request_list(sharedPref.getUserId());
        call.enqueue(new Callback<List<ModelPatientRequestList>>() {
            @Override
            public void onResponse(Call<List<ModelPatientRequestList>> call, Response<List<ModelPatientRequestList>> response) {
                if (response.isSuccessful() && response.body()!=null){
                    List<ModelPatientRequestList> model = response.body();

                    Gson gson = new Gson();
                    String jsonStr = gson.toJson(model);
//                    showToast(jsonStr);

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

                    //init request list model
                    rvRequestList = new ArrayList<>();
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

//                    init recycler view
                    initRecyclerView();
                }else {
                    progressBarAdapter.dismissDialog();
                    showToast("something wrong to retrieve request list");
                }
            }

            @Override
            public void onFailure(Call<List<ModelPatientRequestList>> call, Throwable t) {
                progressBarAdapter.dismissDialog();
                showToast(t.toString());
            }
        });
    }

//    for recycler view request list
    private void initRecyclerView() {
        RvAdapterRequestList adapterRequestList = new RvAdapterRequestList(getContext(),rvRequestList,this);
        recyclerViewPatientRequest.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewPatientRequest.setHasFixedSize(true);
        recyclerViewPatientRequest.setAdapter(adapterRequestList);
//      progress bar dismiss
        progressBarAdapter.dismissDialog();
    }

//    for recycler view request click
    @SuppressLint("SetTextI18n")
    @Override
    public void requestClick(int position, String id, String name, String Gender, String Age, String email, String ph) {
//        show custom dialog
        Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog_request_list);
        dialog.setCanceledOnTouchOutside(false);
//        finding xml
        TextView textViewName = dialog.findViewById(R.id.tv_custom_dialog_request_name);
        View view = dialog.findViewById(R.id.customDialogRequestView);
        ProgressBar progressBar = dialog.findViewById(R.id.progressCustomRequest);
        TextView textViewGender = dialog.findViewById(R.id.tv_custom_dialog_request_gender);
        TextView textViewAge = dialog.findViewById(R.id.tv_custom_dialog_request_age);
        TextView textViewEmail = dialog.findViewById(R.id.tv_custom_dialog_request_email);
        TextView textViewPh = dialog.findViewById(R.id.tv_custom_dialog_request_phone);
        Button buttonCancel = dialog.findViewById(R.id.btnCustomDialogRequestCancel);
        Button buttonAccept = dialog.findViewById(R.id.btnCustomDialogRequestAccept);

//        set resource
        textViewName.setText("Name : "+name);
        textViewGender.setText("Gender : "+Gender);
        textViewAge.setText("Age : "+Age);
        textViewEmail.setText("Email : "+email);
        textViewPh.setText("Phone : "+ph);

        buttonCancel.setEnabled(true);
        buttonAccept.setEnabled(true);

//        cancel button click listener
        buttonCancel.setOnClickListener(v -> {
            dialog.dismiss();
        });
//        button accept click listener
        buttonAccept.setOnClickListener(v -> {
            view.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
//            update request
            ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
            Call<ModelAcceptPatient> call = apiInterface.accept_patient(sharedPref.getUserId(),id);
            call.enqueue(new Callback<ModelAcceptPatient>() {
                @Override
                public void onResponse(Call<ModelAcceptPatient> call, Response<ModelAcceptPatient> response) {
                    if (response.isSuccessful() && response.body()!=null){
                        ModelAcceptPatient model = response.body();
                        if (model.isSuccess()){
                            progressBar.setVisibility(View.GONE);
                            view.setVisibility(View.VISIBLE);
                            dialog.dismiss();
//                            recreate patient request fragment
                            Objects.requireNonNull(getFragmentManager())
                                .beginTransaction()
                                .replace(R.id.hostFragmentId,new PatientRequestFragment())
                                .addToBackStack(null).commit();

                            //call home activity method
                            //((HomeActivity) getActivity()).get_request_count(sharedPref.getUserId());

                            //((HomeActivity) getActivity()).get_request_count(sharedPref.getUserId());


                        }else {
                            progressBar.setVisibility(View.GONE);
                            view.setVisibility(View.VISIBLE);
                            showToast(model.getMessage());
                        }
                    }else {
                        progressBar.setVisibility(View.GONE);
                        view.setVisibility(View.VISIBLE);
                        showToast("something wrong to accept request");
                    }
                }

                @Override
                public void onFailure(Call<ModelAcceptPatient> call, Throwable t) {
                    progressBar.setVisibility(View.GONE);
                    view.setVisibility(View.VISIBLE);
                    showToast(t.toString());
                }
            });
        });

        // show dialog
        dialog.show();
    }
}