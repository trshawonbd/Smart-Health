package com.ppal007.smarthealth.adapter.rvAdapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.ppal007.smarthealth.R;
import com.ppal007.smarthealth.interFace.ViewPrescriptionMsgClick;
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

public class RvAdapterMsgPrescription extends RecyclerView.Adapter<RvAdapterMsgPrescription.MyViewHolder> {

    private Context context;
    private ArrayList<ModelMessage> list;
//    private ViewPrescriptionMsgClick viewPrescriptionMsgClick;

    public RvAdapterMsgPrescription(Context context, ArrayList<ModelMessage> list) {
        this.context = context;
        this.list = list;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.custom_rv_msg_prescription,parent,false);

        return new RvAdapterMsgPrescription.MyViewHolder(view);
    }

    @SuppressLint({"SetTextI18n", "RtlHardcoded"})
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

//        convert date
//        long millisecond = Long.parseLong(list.get(position).getPresDate());
//        Date date = new Date(millisecond);
//        @SuppressLint("SimpleDateFormat")
//        Format formatter = new SimpleDateFormat("dd-MMM-yyyy");
//        String _date = formatter.format(date);
////        set date
//        holder.textViewDate.setText("Date : "+_date);
//
////        convert time
//        long startTimeMilli = Long.parseLong(list.get(position).getPresTime());
//        Time startTime = new Time(startTimeMilli);
//        @SuppressLint("SimpleDateFormat")
//        Format startTimeFormat = new SimpleDateFormat("HH:mm:ss");
//        String _time = startTimeFormat.format(startTime);
////        set time
//        holder.textViewTime.setText("Time : "+_time);


        holder.textView_patient_msg.setText(list.get(position).getMsg());
        holder.textView_patient_msg.setTextColor(Color.GREEN);
        holder.textView_patient_msg.setGravity(Gravity.RIGHT);
//        holder.textView_doctor_msg.setText(list.get(position).getPrescription());

        //get doctor msg
        String msgTblId = list.get(position).getId();
        holder.textView_id.setText(msgTblId);
        //Toast.makeText(context, ""+msgTblId, Toast.LENGTH_SHORT).show();

        retrieve_doctor_msg_prescription(msgTblId,position,holder.recyclerView_child);


    }

    private void retrieve_doctor_msg_prescription(String msgTblId, int parent_position, RecyclerView recyclerView_child) {
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<List<ModelPrescriptionMsg>> call = apiInterface.retrieve_doctor_msg_prescription(msgTblId);
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
                    init_rv_child(recyclerView_child,model_msg_prescription_doctor,parent_position);


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

    private void init_rv_child(RecyclerView recyclerView_child, ArrayList<ModelPrescriptionMsg> model_msg_prescription_doctor, int parent_position) {
        RvAdapterChildShowMsg adapterChildShowMsg = new RvAdapterChildShowMsg(context,model_msg_prescription_doctor,parent_position);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context,
                RecyclerView.VERTICAL,
                false);
        recyclerView_child.setLayoutManager(linearLayoutManager);
        recyclerView_child.setItemAnimator(new DefaultItemAnimator());
        recyclerView_child.setAdapter(adapterChildShowMsg);

    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textView_id,textView_patient_msg;
        RecyclerView recyclerView_child;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            textView_id = itemView.findViewById(R.id.patient_msg_id);
            textView_patient_msg = itemView.findViewById(R.id.patient_msg_text);
            recyclerView_child = itemView.findViewById(R.id.rv_child_show_msg);

        }
    }
}
