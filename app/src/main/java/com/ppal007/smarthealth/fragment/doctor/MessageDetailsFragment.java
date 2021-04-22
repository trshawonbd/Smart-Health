package com.ppal007.smarthealth.fragment.doctor;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ppal007.smarthealth.R;
import com.ppal007.smarthealth.adapter.progressBar.ProgressBarAdapter;
import com.ppal007.smarthealth.adapter.rvAdapter.RvAdapterMsgDetails;
import com.ppal007.smarthealth.adapter.rvAdapter.RvAdapterRequestList;
import com.ppal007.smarthealth.adapter.sharedPref.AdapterSharedPref;
import com.ppal007.smarthealth.interFace.PrescripInMsgClick;
import com.ppal007.smarthealth.model.ModelMessage;
import com.ppal007.smarthealth.model.ModelPatientRequestList;
import com.ppal007.smarthealth.model.ModelPrescriptionMsg;
import com.ppal007.smarthealth.retrofit.ApiClient;
import com.ppal007.smarthealth.retrofit.ApiInterface;

import org.json.JSONArray;
import org.json.JSONException;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessageDetailsFragment extends Fragment implements PrescripInMsgClick {

    private void showToast(String msg){
        Toast.makeText(getContext(), ""+msg, Toast.LENGTH_SHORT).show();
    }

    public MessageDetailsFragment() {
        // Required empty public constructor
    }

    private ProgressBarAdapter progressBarAdapter;
    private AdapterSharedPref sharedPref;
    private String _id,_name,_gender,_age,_email,_phone;
    private RecyclerView recyclerViewMessageDetails;
    private TextView textViewName,textViewGender,textViewAge;
    private EditText editText_msg;
    private ImageView imageView_send_msg;
    private ArrayList<ModelMessage> rvMessageList;
    private RvAdapterMsgDetails adapter;


    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_message_details, container, false);
//        init progressbar adapter
        progressBarAdapter = new ProgressBarAdapter(getContext());
//        progressBarAdapter.startLoadingDialog();
//        init sharedPref
        sharedPref = new AdapterSharedPref(getContext());
//        get bundle value from show message
        savedInstanceState = getArguments();
        if (savedInstanceState!=null){
            _id = savedInstanceState.getString("ex_patient_id_from_show_message");
            _name = savedInstanceState.getString("ex_patient_name_from_show_message");
            _gender = savedInstanceState.getString("ex_patient_gender_from_show_message");
            _age = savedInstanceState.getString("ex_patient_age_from_show_message");
            _email = savedInstanceState.getString("ex_patient_email_from_show_message");
            _phone = savedInstanceState.getString("ex_patient_phone_from_show_message");
        }

//        find xml
        recyclerViewMessageDetails = view.findViewById(R.id.rvMessageDetails);
        textViewName = view.findViewById(R.id.tvName_msg_details);
        textViewGender = view.findViewById(R.id.tvGender_msg_details);
        textViewAge = view.findViewById(R.id.tvAge_msg_details);
        editText_msg = view.findViewById(R.id.etSendMsg_form_doctorId);
        imageView_send_msg = view.findViewById(R.id.msgSend_from_doctorId);

//        set value in text view
        textViewName.setText("Name : "+_name);
        textViewGender.setText("Gender : "+_gender);
        textViewAge.setText("Age : "+_age);

//        retrieve messages
        retrieveMessages();

        //init send message
        sendMessage();

        return view;
    }

    private void sendMessage() {
        imageView_send_msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //showToast("ok");
                //get message
                String _msg = editText_msg.getText().toString();

                if (_msg.isEmpty()){
                    editText_msg.setError("Enter Message");
                    editText_msg.requestFocus();
                }else {
                    String patientId = null;
                    String doctorId = null;
                    String pres = null;
                    String time = null;

//                    ArrayList<ModelMessage> list = RvAdapterMsgDetails.rvMessageList;

                    List<String> id_list = new ArrayList<>();

                    for (ModelMessage st : rvMessageList){
//                        msgTblId = st.getId();
                        patientId = st.getPatientId();
                        doctorId = st.getDoctorId();
//                        pres = st.getMsg();
                        time = st.getMsgTime();

                        id_list.add(st.getId());


                    }

                    String msgTbl_Id = id_list.get(0);
//                    showToast(""+iiii);

                    ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
                    Call<ModelPrescriptionMsg> call = apiInterface.send_prescription_msg(msgTbl_Id,patientId,doctorId,_msg,time);
                    call.enqueue(new Callback<ModelPrescriptionMsg>() {
                        @Override
                        public void onResponse(Call<ModelPrescriptionMsg> call, Response<ModelPrescriptionMsg> response) {
                            if (response.isSuccessful() && response.body()!=null){
                                ModelPrescriptionMsg model = response.body();
                                if (model.isSuccess()){
                                    editText_msg.setText("");

                                    //Reload();
                                    adapter.notifyDataSetChanged();
                                }else {
                                    showToast(model.getMessage());
                                }
                            }else {
                                showToast("something wrong to send prescription");
                            }

                        }

                        @Override
                        public void onFailure(Call<ModelPrescriptionMsg> call, Throwable t) {
                            showToast(t.toString());
                        }
                    });
                }
            }
        });
    }

    private void retrieveMessages() {
        progressBarAdapter.startLoadingDialog();
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<List<ModelMessage>> listCall = apiInterface.retrieve_message_details(_id);
        listCall.enqueue(new Callback<List<ModelMessage>>() {
            @Override
            public void onResponse(Call<List<ModelMessage>> call, Response<List<ModelMessage>> response) {
                if (response.isSuccessful() && response.body()!=null){
                    List<ModelMessage> model = response.body();

                    Gson gson = new Gson();
                    String jsonStr = gson.toJson(model);

                    JSONArray jsonArray = null;
                    try {
                        jsonArray = new JSONArray(jsonStr);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    List<String> _id = new ArrayList<>();
                    List<String> _doctorId = new ArrayList<>();
                    List<String> _patientId = new ArrayList<>();
                    List<String> _message = new ArrayList<>();
                    List<String> _msgDate = new ArrayList<>();
                    List<String> _msgTime = new ArrayList<>();
                    for(int i = 0; i < Objects.requireNonNull(jsonArray).length(); i++){
                        try {
                            _id.add(jsonArray.getJSONObject(i).getString("id"));
                            _doctorId.add(jsonArray.getJSONObject(i).getString("doctor_id"));
                            _patientId.add(jsonArray.getJSONObject(i).getString("patient_id"));
                            _message.add(jsonArray.getJSONObject(i).getString("msg"));
                            _msgDate.add(jsonArray.getJSONObject(i).getString("msg_date"));
                            _msgTime.add(jsonArray.getJSONObject(i).getString("msg_time"));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

//                    init model
                    rvMessageList = new ArrayList<>();
                    for (int i = 0; i < _id.size(); i++){
                        ModelMessage _model = new ModelMessage(
                                _id.get(i),
                                _patientId.get(i),
                                _doctorId.get(i),
                                _message.get(i),
                                _msgDate.get(i),
                                _msgTime.get(i)


                        );
                        rvMessageList.add(_model);
                    }

//                    init recycler view
                    initRecyclerView(rvMessageList);

                }else {
                    showToast("something wrong to retrieve message details");
                    progressBarAdapter.dismissDialog();
                }
            }

            @Override
            public void onFailure(Call<List<ModelMessage>> call, Throwable t) {
                showToast(t.toString());
                progressBarAdapter.dismissDialog();
            }
        });
    }

//    for recycler view initialize
    private void initRecyclerView(ArrayList<ModelMessage> rvMessageList) {
        adapter = new RvAdapterMsgDetails(getContext(),rvMessageList,this);
        recyclerViewMessageDetails.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewMessageDetails.setHasFixedSize(true);
        recyclerViewMessageDetails.setAdapter(adapter);
//        dismiss progress bar
        progressBarAdapter.dismissDialog();
    }

    @Override
    public void onMessageClick(String position, String msgTblId, String patientId, String doctorId, String msgDate, String msgTime) {
        Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog_msg_prescription);
        dialog.setCanceledOnTouchOutside(false);

//        find xml
        View view = dialog.findViewById(R.id.viewId_prescription);
        ProgressBar progressBar = dialog.findViewById(R.id.progressCustom_send_prescription);
        EditText etPress = dialog.findViewById(R.id.etPrescription);
        Button buttonSend = dialog.findViewById(R.id.btnSendPrescription);
        Button buttonCancel = dialog.findViewById(R.id.btnCancelPrescription);

//        init views
        view.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);

//        send button click listener
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String _pres = etPress.getText().toString();

                if (_pres.isEmpty()){
                    showToast("Prescription Empty!");
                }else {
                    view.setVisibility(View.GONE);
                    progressBar.setVisibility(View.VISIBLE);

                    String _time = get_current_time();
                    //insert
                    ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
                    Call<ModelPrescriptionMsg> call = apiInterface.send_prescription_msg(msgTblId,patientId,doctorId,_pres,_time);
                    call.enqueue(new Callback<ModelPrescriptionMsg>() {
                        @Override
                        public void onResponse(Call<ModelPrescriptionMsg> call, Response<ModelPrescriptionMsg> response) {
                            if (response.isSuccessful() && response.body()!=null){
                                ModelPrescriptionMsg model = response.body();
                                if (model.isSuccess()){
                                    progressBar.setVisibility(View.GONE);
                                    view.setVisibility(View.VISIBLE);
                                    etPress.setText("");
                                    dialog.dismiss();
                                }else {
                                    progressBar.setVisibility(View.GONE);
                                    view.setVisibility(View.VISIBLE);

                                    showToast(model.getMessage());
                                }
                            }else {
                                progressBar.setVisibility(View.GONE);
                                view.setVisibility(View.VISIBLE);

                                showToast("something wrong to send prescription");
                            }
                        }

                        @Override
                        public void onFailure(Call<ModelPrescriptionMsg> call, Throwable t) {
                            progressBar.setVisibility(View.GONE);
                            view.setVisibility(View.VISIBLE);

                            showToast(t.toString());
                        }
                    });
                }

            }
        });

//        button cancel click listener
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        // show dialog
        dialog.show();
    }


    private String get_current_time(){
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String _tm = sdf.format(new Date());

        Time t = Time.valueOf(_tm);
        long l = t.getTime();

        return Long.toString(l);

    }

    FragmentManager fragmentManager;
    public void Reload(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            fragmentManager.beginTransaction().detach(this).commitNow();
            fragmentManager.beginTransaction().attach(this).commitNow();
        } else {
            fragmentManager.beginTransaction().detach(this).attach(this).commit();
        }
    }
}