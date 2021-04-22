package com.ppal007.smarthealth.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ppal007.smarthealth.R;
import com.ppal007.smarthealth.activity.message.model.User;
import com.ppal007.smarthealth.activity.message.ui.MessageActivity;
import com.ppal007.smarthealth.adapter.progressBar.ProgressBarAdapter;
import com.ppal007.smarthealth.adapter.sharedPref.AdapterSharedPref;
import com.ppal007.smarthealth.fragment.doctor.AcceptPatientFragment;
import com.ppal007.smarthealth.fragment.doctor.DoctorHomeFragment;
import com.ppal007.smarthealth.fragment.doctor.ShowMessageFragment;
import com.ppal007.smarthealth.fragment.doctor.notifi.service.MyService;
import com.ppal007.smarthealth.fragment.patient.HistoryFragment;
import com.ppal007.smarthealth.fragment.patient.PatientHomeFragment;
import com.ppal007.smarthealth.fragment.doctor.PatientRequestFragment;
import com.ppal007.smarthealth.fragment.doctor.TimeScheduleFragment;
import com.ppal007.smarthealth.fragment.patient.PrescriptionFragment;
import com.ppal007.smarthealth.fragment.patient.ViewScheduleFragment;
import com.ppal007.smarthealth.model.ModelRegisterAsDoctor;
import com.ppal007.smarthealth.model.ModelRequestCount;
import com.ppal007.smarthealth.retrofit.ApiClient;
import com.ppal007.smarthealth.retrofit.ApiInterface;
import com.ppal007.smarthealth.utils.Common;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private static final String TAG = "HomeActivity";

    private NavigationView navigationView;
    private String loginMood;
    private boolean viewFlag;
    private AdapterSharedPref sharedPref;
//    public static TextView textView_count;
    public static RelativeLayout relativeLayout_count;
    private FirebaseAuth auth;
    private FirebaseUser firebaseUser;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // hide title bar
        WindowManager.LayoutParams attrs = getWindow().getAttributes();
        attrs.flags = WindowManager.LayoutParams.FLAG_FULLSCREEN;
        getWindow().setAttributes(attrs);
        setContentView(R.layout.activity_home);
        // toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        get bundle value from login
        Bundle bundle = getIntent().getExtras();
        if (bundle!=null){
            loginMood = bundle.getString("ex_login_mood_from_login");
        }

//        init sharedPref
        sharedPref = new AdapterSharedPref(getApplicationContext());






        //navigation section#######################################################################
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        // init nav header
        View headerView = navigationView.getHeaderView(0);
        ImageView imageView = headerView.findViewById(R.id.imageViewNavHeader);
        TextView name = headerView.findViewById(R.id.navHeaderName);
        TextView gender = headerView.findViewById(R.id.navTitleGenger);
        TextView age = headerView.findViewById(R.id.navTitleAge);
        TextView email = headerView.findViewById(R.id.navTitleEmail);
        TextView phone = headerView.findViewById(R.id.navTitlePhone);



//        set resource
//        set image
        String _path = Common.BASE_URL+sharedPref.getProfile_img();
        Uri img_uri = Uri.parse(_path);
        Glide.with(this).load(img_uri).circleCrop().into(imageView);
        name.setText("Name : "+sharedPref.getUserName());
        gender.setText("Gender : "+sharedPref.getUserGender());
        age.setText("Age : "+sharedPref.getUserAge());
        email.setText("Email : "+sharedPref.getUserEmail());
        phone.setText("Phone : "+sharedPref.getUserPhone());

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        // init home fragment compare to login mood
        Fragment fragment;
        if (loginMood.equals(Common.AS_DOCTOR)){
            //reloadDrawer
            reloadNavDrawer(drawer,sharedPref.getUserId());
            //call service from doctor Home activity
            startService();
            //set navigation drawer item for doctor
            navigationView.inflateMenu(R.menu.activity_main_drawer_doctor);
            viewFlag = true;
            //get request count
            get_request_count(sharedPref.getUserId());

//            init views
            name.setVisibility(View.VISIBLE);
            email.setVisibility(View.VISIBLE);
            phone.setVisibility(View.VISIBLE);
            gender.setVisibility(View.GONE);
            age.setVisibility(View.GONE);

            fragment = new DoctorHomeFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.hostFragmentId,fragment).commit();

        }else if (loginMood.equals(Common.AS_PATIENT)){
//            set navigation drawer item for patient
            navigationView.inflateMenu(R.menu.activity_main_drawer_patient);
            viewFlag = false;
//            init views
            name.setVisibility(View.VISIBLE);
            email.setVisibility(View.VISIBLE);
            phone.setVisibility(View.VISIBLE);
            gender.setVisibility(View.VISIBLE);
            age.setVisibility(View.VISIBLE);

            fragment = new PatientHomeFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.hostFragmentId,fragment).commit();

        }

    }

    private void reloadNavDrawer(DrawerLayout drawer,String doctorId) {
        drawer.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {
                get_request_count(doctorId);

            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
    }

    private void startService() {
        Intent service_intent = new Intent(HomeActivity.this,MyService.class);
        service_intent.putExtra("service_extra","view_now");
        startService(service_intent);
    }

//    private String _count;

    private ImageView imageView;
    public void get_request_count(String doctorId) {
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<ModelRequestCount> call = apiInterface.count_patient_request(doctorId);
        call.enqueue(new Callback<ModelRequestCount>() {
            @Override
            public void onResponse(Call<ModelRequestCount> call, Response<ModelRequestCount> response) {
                if (response.isSuccessful() && response.body()!=null){
                    ModelRequestCount model = response.body();
                    if (model.getMessage().equals("No_Request")){
//                        String _count = model.getCount();
//                        showCount(_count,textView_cnt,imageView,"noAlert");
                        LayoutInflater li = LayoutInflater.from(HomeActivity.this);
                        relativeLayout_count = (RelativeLayout) li.inflate(R.layout.request_counter_layout,null);
                        TextView textView_cnt = relativeLayout_count.findViewById(R.id.tv_count);
                        textView_cnt.setText("0");
                        imageView = relativeLayout_count.findViewById(R.id.img_bell);
                        navigationView.getMenu().findItem(R.id.nav_request_doctor).setActionView(relativeLayout_count);

                    }else if (model.getMessage().equals("Found_Request")){

                        String _count = model.getCount();

                        LayoutInflater li = LayoutInflater.from(HomeActivity.this);
                        relativeLayout_count = (RelativeLayout) li.inflate(R.layout.request_counter_layout,null);
                        TextView textView_cnt = relativeLayout_count.findViewById(R.id.tv_count);
                        imageView = relativeLayout_count.findViewById(R.id.img_bell);
                        navigationView.getMenu().findItem(R.id.nav_request_doctor).setActionView(relativeLayout_count);

                        showCount(_count,textView_cnt,imageView);


                    }






//                    if (model.isSuccess()){
//                         _count = model.getCount();
//
//                        LayoutInflater li = LayoutInflater.from(HomeActivity.this);
//                        relativeLayout_count = (RelativeLayout) li.inflate(R.layout.request_counter_layout,null);
//                         textView_cnt = relativeLayout_count.findViewById(R.id.tv_count);
//                         imageView = relativeLayout_count.findViewById(R.id.img_bell);
//                        navigationView.getMenu().findItem(R.id.nav_request_doctor).setActionView(relativeLayout_count);
//
//                        showCount(_count,textView_cnt,imageView,"foundAlert");
//                    }else {
//                        //Toast.makeText(HomeActivity.this, ""+model.getMessage(), Toast.LENGTH_SHORT).show();
//                        //showCount(_count,textView_cnt,imageView,"noAlert");
//                        //textView_cnt.setText("0");
////                        textView_cnt.setVisibility(View.GONE);
////                        imageView.setVisibility(View.GONE);
//                    }
                }else {
                    Toast.makeText(HomeActivity.this, "something wrong to get request count", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ModelRequestCount> call, Throwable t) {
                Log.d(TAG, "onFailure: "+t.toString());
            }
        });
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

//        flag = true == doctor and flag = false == patient
        int id = item.getItemId();
        Fragment fragment;
        if (viewFlag){

            if (id == R.id.nav_home_doctor){
                fragment = new DoctorHomeFragment();

                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.hostFragmentId,fragment)
                        .addToBackStack(null)
                        .commit();

            }else if (id == R.id.nav_time_schedule_doctor){

                fragment = new TimeScheduleFragment();
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.hostFragmentId,fragment)
                        .addToBackStack(null)
                        .commit();

            }else if (id == R.id.nav_message_doctor){
                initMessenger();

            }else if (id == R.id.nav_request_doctor){

                fragment = new PatientRequestFragment();
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.hostFragmentId,fragment)
                        .addToBackStack(null)
                        .commit();

            }else if (id == R.id.nav_accept_booking_doctor){

                fragment = new AcceptPatientFragment();
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.hostFragmentId,fragment)
                        .addToBackStack(null)
                        .commit();

            }else if (id == R.id.nav_logout_doctor){
                sharedPref.login_status(false);
                sharedPref.removeUser();

                Intent intent = new Intent(HomeActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }

        }else {
//            for patient
            if (id == R.id.nav_home_patient){

                fragment = new PatientHomeFragment();
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.hostFragmentId,fragment)
                        .addToBackStack(null)
                        .commit();

            }else if (id == R.id.nav_schedule_patient){
                fragment = new ViewScheduleFragment();
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.hostFragmentId,fragment)
                        .addToBackStack(null)
                        .commit();

            }else if (id == R.id.nav_msg_patient){

                getDoctorIfo(Common.FOR_MESSAGE_DOCTOR);

            }else if (id == R.id.nav_history_patient){

                fragment = new HistoryFragment();
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.hostFragmentId,fragment)
                        .addToBackStack(null)
                        .commit();


            }else if (id == R.id.nav_call_doctor_patient){
//                //show progressbar
                getDoctorIfo(Common.FOR_CALL_DOCTOR);

            }else if (id == R.id.nav_call_ambulance_patient){
                String phNum = "01111111111";
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+phNum));
                startActivity(intent);

            }else if (id == R.id.nav_see_prescription_patient){
                fragment = new PrescriptionFragment();
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.hostFragmentId,fragment)
                        .addToBackStack(null)
                        .commit();


            }else if (id == R.id.nav_logout_patient){
                sharedPref.login_status(false);
                sharedPref.removeUser();

                Intent intent = new Intent(HomeActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }


    private void getDoctorIfo(String reason){
        //show progressbar
        ProgressBarAdapter progressBarAdapter = new ProgressBarAdapter(HomeActivity.this);
        progressBarAdapter.startLoadingDialog();
        //call api
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<ModelRegisterAsDoctor> call = apiInterface.retrieve_doctor_info(sharedPref.getUserId());
        call.enqueue(new Callback<ModelRegisterAsDoctor>() {
            @Override
            public void onResponse(Call<ModelRegisterAsDoctor> call, Response<ModelRegisterAsDoctor> response) {
                if (response.isSuccessful() && response.body()!=null){
                    ModelRegisterAsDoctor model = response.body();
                    if (model.isSuccess()){
                        String _name = model.getName();
                        String _email = model.getEmail();
                        String _phone = model.getPhone();
                        String _doctorId = model.getId();

                        //check
                        if (reason.equals(Common.FOR_CALL_DOCTOR)){
                            //for call doctor
                            show_Dialog(_name,_email,_phone,progressBarAdapter);
                        }else if (reason.equals(Common.FOR_MESSAGE_DOCTOR)){
                            //for message doctor
                            initMessageFromPatient(_name,_doctorId,progressBarAdapter);

                        }

                    }else {
                        Toast.makeText(HomeActivity.this, ""+model.getMessage(), Toast.LENGTH_LONG).show();
                        progressBarAdapter.dismissDialog();
                    }
                }else {
                    Toast.makeText(HomeActivity.this, "something wrong to retrieve doctor name!", Toast.LENGTH_SHORT).show();
                    progressBarAdapter.dismissDialog();
                }
            }

            @Override
            public void onFailure(Call<ModelRegisterAsDoctor> call, Throwable t) {
                Log.d(TAG, "onFailure: "+t.toString());
                progressBarAdapter.dismissDialog();
            }
        });
    }

    private void initMessageFromPatient(String name, String doctorId, ProgressBarAdapter progressBarAdapter) {
//######################################################################################################################################################

        final String[] msg_user_id = new String[1];
        String[] user_name = new String[1];

        DatabaseReference mDatabaseRef = FirebaseDatabase.getInstance().getReference(Common.FIREBASE_USER_DB);

        mDatabaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {
                    if(data.child("usermode").exists()&&data.child("userid").exists()) {
                        if(Objects.requireNonNull(data.child("usermode").getValue()).toString().equals(Common.MODE_D) &&
                                Objects.requireNonNull(data.child("userid").getValue()).toString().equals(doctorId)) {
                            //Do What You Want To Do.
                            User user = data.getValue(User.class);

                            assert user != null;
                            msg_user_id[0] = user.getId();
                            user_name[0] = user.getUsername();

                        }
                    }
                }

                //login
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                mAuth.signInWithEmailAndPassword(sharedPref.getUserEmail(),sharedPref.getUser_pass())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()){
                                    Intent intent = new Intent(HomeActivity.this, MessageActivity.class);
                                    intent.putExtra("user_id",msg_user_id[0]);
                                    intent.putExtra("user_name",user_name[0]);
                                    startActivity(intent);
                                    progressBarAdapter.dismissDialog();
                                }
                            }
                        });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(HomeActivity.this, ""+error.toString(), Toast.LENGTH_SHORT).show();
                progressBarAdapter.dismissDialog();

            }
        });


    }

    //start message activity
    private void initMessenger() {
        //start progress bar
        ProgressBarAdapter progressBarAdapter = new ProgressBarAdapter(HomeActivity.this);
        progressBarAdapter.startLoadingDialog();

        auth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        String userid = sharedPref.getUserId();
        String username = sharedPref.getUserName();
        String useremail = sharedPref.getUserEmail();
        String password = sharedPref.getUser_pass();


//            login
            auth.signInWithEmailAndPassword(useremail,password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                getSupportFragmentManager()
                                        .beginTransaction()
                                        .replace(R.id.hostFragmentId,new ShowMessageFragment())
                                        .addToBackStack(null)
                                        .commit();
                                progressBarAdapter.dismissDialog();


                            }else {
                                Toast.makeText(HomeActivity.this, "login failed", Toast.LENGTH_SHORT).show();
                                progressBarAdapter.dismissDialog();


                            }

                        }
                    });


    }

    //    for call doctor
    private void show_Dialog(String name, String email, String phone, ProgressBarAdapter progressBarAdapter) {
        progressBarAdapter.dismissDialog();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Doctor Name : "+name+"\n"+"Email : "+email+"\n"+"Phone : "+phone)
                .setCancelable(false)
                .setPositiveButton("Call", (dialog, id) -> {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:"+phone));
                    startActivity(intent);
                }).setNegativeButton("Cancel", (dialog, id) -> dialog.cancel());
        AlertDialog alert = builder.create();
        alert.show();
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {

            drawer.closeDrawer(GravityCompat.START);

        } else if (getSupportFragmentManager().getBackStackEntryCount() > 0){

            getSupportFragmentManager().popBackStack();

        }else {


            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Are you sure you want to exit?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", (dialog, id) -> finish())
                    .setNegativeButton("No", (dialog, id) -> dialog.cancel());
            AlertDialog alert = builder.create();
            alert.show();

        }
    }

    //show count method
    public void showCount(String count_number, TextView textView_cnt,ImageView imageView){
        //int cnt = Integer.parseInt(count_number);
        textView_cnt.setText(count_number);
//        if (cnt > 0){
//            textView_cnt.setText(count_number);
//        }else {
//            textView_cnt.setVisibility(View.GONE);
//            imageView.setVisibility(View.GONE);
//        }





    }
}