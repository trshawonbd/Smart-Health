package com.ppal007.smarthealth.fragment.doctor;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ppal007.smarthealth.R;
import com.ppal007.smarthealth.activity.message.adapter.UserAdapter;
import com.ppal007.smarthealth.activity.message.model.Chat;
import com.ppal007.smarthealth.activity.message.model.User;
import com.ppal007.smarthealth.adapter.progressBar.ProgressBarAdapter;
import com.ppal007.smarthealth.adapter.sharedPref.AdapterSharedPref;
import com.ppal007.smarthealth.utils.Common;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


public class ShowMessageFragment extends Fragment {

    private void showToast(String msg){
        Toast.makeText(getContext(), ""+msg, Toast.LENGTH_SHORT).show();
    }

    private static final String TAG = "ShowMessageFragment";

    private RecyclerView recyclerView;
    private List<User> userList;
    private ProgressBarAdapter progressBarAdapter;
    private AdapterSharedPref sharedPref;


    public ShowMessageFragment() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_show_message, container, false);
        //init shared pref
        sharedPref = new AdapterSharedPref(Objects.requireNonNull(getContext()));
        //init progress bar adapter
        progressBarAdapter = new ProgressBarAdapter(getContext());
        progressBarAdapter.startLoadingDialog();
        //find xml
        recyclerView = view.findViewById(R.id.recyclerViewUsers);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        userList = new ArrayList<>();


        readUsers();

        return view;
    }

    private void readUsers() {

        final String[] msg_user_id = new String[1];

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(Common.FIREBASE_USER_DB);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {
                    if(data.child("usermode").exists() && data.child("userid").exists()) {
                        if(Objects.requireNonNull(data.child("usermode").getValue()).toString().equals(Common.MODE_D) &&
                                Objects.requireNonNull(data.child("userid").getValue()).toString().equals(sharedPref.getUserId())) {
                            //Do What You Want To Do.
                            User user = data.getValue(User.class);

                            assert user != null;
                            msg_user_id[0] = user.getId();

                        }
                    }


                }

                //get patient list which are under doctor
                retrievePatientList(msg_user_id[0],progressBarAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), ""+error.toString(), Toast.LENGTH_SHORT).show();
                progressBarAdapter.dismissDialog();
            }
        });

    }

    private void retrievePatientList(String doctorId, ProgressBarAdapter progressBarAdapter) {

        List<String> patientList = new ArrayList<>();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(Common.FIREBASE_CHAT_DB);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {
                    if(data.child("receiver").exists()) {
                        if(Objects.requireNonNull(data.child("receiver").getValue()).toString().equals(doctorId)) {
                            //Do What You Want To Do.
                            Chat chat = data.getValue(Chat.class);

                            patientList.add(chat.getSender());

                        }
                    }


                }

//                init user list
                retrieveUserList(patientList,progressBarAdapter);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), ""+error.toString(), Toast.LENGTH_SHORT).show();
                progressBarAdapter.dismissDialog();
            }
        });

    }

    private void retrieveUserList(List<String> patientIdList, ProgressBarAdapter progressBarAdapter) {

        List<String> id_list = new ArrayList<>();
        List<String> name_list = new ArrayList<>();

        List<String> newIdList = patientIdList.stream().distinct().collect(Collectors.toList());
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(Common.FIREBASE_USER_DB);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();

                for (DataSnapshot data : snapshot.getChildren()){
                    User user = data.getValue(User.class);

                    for (String id : newIdList){
                        if (data.child("id").getValue().toString().equals(id) &&
                                data.child("usermode").getValue().toString().equals(Common.MODE_P)){
                            userList.add(user);
                        }

                    }

                    UserAdapter userAdapter = new UserAdapter(getContext(),userList);
                    recyclerView.setAdapter(userAdapter);
                    progressBarAdapter.dismissDialog();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), ""+error.toString(), Toast.LENGTH_SHORT).show();
                progressBarAdapter.dismissDialog();

            }
        });


    }


}