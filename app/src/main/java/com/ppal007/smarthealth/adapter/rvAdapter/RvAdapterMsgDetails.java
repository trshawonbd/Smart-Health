package com.ppal007.smarthealth.adapter.rvAdapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.codesgood.views.JustifiedTextView;
import com.google.gson.Gson;
import com.ppal007.smarthealth.R;
import com.ppal007.smarthealth.interFace.PrescripInMsgClick;
import com.ppal007.smarthealth.model.ModelMessage;
import com.ppal007.smarthealth.model.ModelPrescriptionMsg;
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

public class RvAdapterMsgDetails extends RecyclerView.Adapter<RvAdapterMsgDetails.MyViewHolder> {

    private Context context;
    private ArrayList<ModelMessage> rvMessageList;
    private PrescripInMsgClick msgClick;

    public RvAdapterMsgDetails(Context context, ArrayList<ModelMessage> rvMessageList, PrescripInMsgClick msgClick) {
        this.context = context;
        this.rvMessageList = rvMessageList;
        this.msgClick = msgClick;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.rv_sample_message_details,parent,false);

        return new RvAdapterMsgDetails.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
//        set date
//        long millisecond = Long.parseLong(rvMessageList.get(position).getMsgDate());
//        Date date = new Date(millisecond);
//        @SuppressLint("SimpleDateFormat")
//        Format formatter = new SimpleDateFormat("dd-MMM-yyyy");
//        holder.textViewDate.setText(formatter.format(date));
////        set time
//        long startTimeMilli = Long.parseLong(rvMessageList.get(position).getMsgTime());
//        Time startTime = new Time(startTimeMilli);
//        @SuppressLint("SimpleDateFormat")
//        Format startTimeFormat = new SimpleDateFormat("HH:mm:ss");
//        String _time = startTimeFormat.format(startTime);
//        holder.textViewTime.setText(_time);
//        set message

        //holder.justifiedTextView.setText(rvMessageList.get(position).getMsg());


        holder.textViewId.setText(rvMessageList.get(position).getId());
        holder.textViewMsg.setText(rvMessageList.get(position).getMsg());

        //retrieve doctor msg
        get_doctor_msg(rvMessageList.get(position).getId(),position,holder.recyclerView);

//        button click listener

    }

    private void get_doctor_msg(String id, int position, RecyclerView recyclerView) {
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<List<ModelPrescriptionMsg>> call = apiInterface.retrieve_doctor_msg_prescription(id);
        call.enqueue(new Callback<List<ModelPrescriptionMsg>>() {
            @Override
            public void onResponse(Call<List<ModelPrescriptionMsg>> call, Response<List<ModelPrescriptionMsg>> response) {
                if (response.isSuccessful() && response.body()!=null){
                    List<ModelPrescriptionMsg> modelPrescriptionMsgs = response.body();
                    Gson gson = new Gson();
                    String json_str = gson.toJson(modelPrescriptionMsgs);

                    JSONArray jsonArray = null;
                    try {
                        jsonArray = new JSONArray(json_str);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    ArrayList<String> _id = new ArrayList<>();
                    ArrayList<String> _prescription = new ArrayList<>();
                    for(int i = 0; i < Objects.requireNonNull(jsonArray).length(); i++){
                        try {
                            _id.add(jsonArray.getJSONObject(i).getString("id"));
                            _prescription.add(jsonArray.getJSONObject(i).getString("prescription"));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    //init model
                    ArrayList<ModelPrescriptionMsg> model_msg_prescription_doctor = new ArrayList<>();
                    for (int i = 0; i < _id.size(); i++){
                        ModelPrescriptionMsg model_pres_list = new ModelPrescriptionMsg(_id.get(i),_prescription.get(i));
                        model_msg_prescription_doctor.add(model_pres_list);

                    }

                    //init rv child
                    init_rv_child(recyclerView,model_msg_prescription_doctor,position);


                }else {
                    Toast.makeText(context, "something wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<ModelPrescriptionMsg>> call, Throwable t) {
                Toast.makeText(context, ""+t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void init_rv_child(RecyclerView recyclerView, ArrayList<ModelPrescriptionMsg> model_msg_prescription_doctor, int position) {
        RvAdapterChildShowMsg adapterChildShowMsg = new RvAdapterChildShowMsg(context,model_msg_prescription_doctor,position);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context,
                RecyclerView.VERTICAL,
                false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapterChildShowMsg);
    }

    @Override
    public int getItemCount() {
        return rvMessageList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textViewId,textViewMsg;
        RecyclerView recyclerView;
        JustifiedTextView justifiedTextView;
        Button buttonPrescription;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewId = itemView.findViewById(R.id.d_msg_id);
            textViewMsg = itemView.findViewById(R.id.doctor_msg_text);
            recyclerView = itemView.findViewById(R.id.rv_child_d_msg);

        }
    }
}
