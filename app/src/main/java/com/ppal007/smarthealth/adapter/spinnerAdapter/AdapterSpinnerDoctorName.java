package com.ppal007.smarthealth.adapter.spinnerAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ppal007.smarthealth.R;
import com.ppal007.smarthealth.model.ModelRetrieveDoctorName;

import java.util.List;

public class AdapterSpinnerDoctorName extends ArrayAdapter<ModelRetrieveDoctorName> {

    public AdapterSpinnerDoctorName(Context context, List<ModelRetrieveDoctorName> doctorList){
        super(context,0,doctorList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position,convertView,parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position,convertView,parent);
    }

    private View initView(int position, View convertView, ViewGroup parent){
        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.custom_spinner_doctor_name_layout,parent,false
            );
        }
        TextView textViewId = convertView.findViewById(R.id.tvSpinnerDoctor_id);
        TextView textViewName = convertView.findViewById(R.id.tvSpinnerDoctor_name);

        ModelRetrieveDoctorName doctorName = getItem(position);

//        set resource
        textViewId.setText(doctorName.getId());
        textViewName.setText(doctorName.getName());

        return convertView;
    }
}
