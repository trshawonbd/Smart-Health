package com.ppal007.smarthealth.fragment.patient;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ppal007.smarthealth.R;
import com.ppal007.smarthealth.activity.message.adapter.UserAdapter;
import com.ppal007.smarthealth.activity.message.model.User;
import com.ppal007.smarthealth.adapter.progressBar.ProgressBarAdapter;
import com.ppal007.smarthealth.adapter.sharedPref.AdapterSharedPref;
import com.ppal007.smarthealth.utils.Common;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class SendMessageFragment extends Fragment {

    private void showToast(String msg){
        Toast.makeText(getContext(), ""+msg, Toast.LENGTH_SHORT).show();
    }

    private static final String TAG = "SendMessageFragment";

    public SendMessageFragment() {
        // Required empty public constructor
    }

    private ProgressBarAdapter progressBarAdapter;
    private AdapterSharedPref sharedPref;
    private RecyclerView recyclerView;

    private UserAdapter userAdapter;
    private List<User> userList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_send_message, container, false);
//        init progress bar
        progressBarAdapter = new ProgressBarAdapter(getContext());
//        iit sharedPref
        sharedPref = new AdapterSharedPref(Objects.requireNonNull(getContext()));
//        find xml
        recyclerView = view.findViewById(R.id.recyclerViewUserss);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        userList = new ArrayList<>();

//        readUsers();



        return view;
    }

//    private void readUsers() {
//        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(Common.FIREBASE_USER_DB);
//
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                userList.clear();
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
//                    User user = snapshot.getValue(User.class);
//                    if (!user.getId().equals(firebaseUser.getUid())){
//                        userList.add(user);
//                    }
//
//                }
//
//                userAdapter = new UserAdapter(getContext(),userList);
//                recyclerView.setAdapter(userAdapter);
//                progressBarAdapter.dismissDialog();
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }



}