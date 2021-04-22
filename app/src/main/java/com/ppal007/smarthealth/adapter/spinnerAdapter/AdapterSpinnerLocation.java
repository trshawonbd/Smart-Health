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
import com.ppal007.smarthealth.model.ModelRegisterAsDoctor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ppal on 26-Feb-21.
 */
public class AdapterSpinnerLocation extends ArrayAdapter<ModelRegisterAsDoctor> {

    public AdapterSpinnerLocation(Context context, List<ModelRegisterAsDoctor> doctorList){
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
                    R.layout.custom_spinner_location,parent,false
            );
        }

        TextView textViewLocation = convertView.findViewById(R.id.tv_spinner_location);

        ModelRegisterAsDoctor doctorLocation = getItem(position);

//        set resource
        textViewLocation.setText(doctorLocation.getLocation());

        return convertView;
    }
}
