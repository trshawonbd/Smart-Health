package com.ppal007.smarthealth.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ppal007.smarthealth.R;
import com.ppal007.smarthealth.adapter.progressBar.ProgressBarAdapter;
import com.ppal007.smarthealth.adapter.sharedPref.AdapterSharedPref;
import com.ppal007.smarthealth.model.ModelForgotPass;
import com.ppal007.smarthealth.model.ModelLogin;
import com.ppal007.smarthealth.retrofit.ApiClient;
import com.ppal007.smarthealth.retrofit.ApiInterface;
import com.ppal007.smarthealth.utils.Common;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private void showToast(String msg) {
        Toast.makeText(this, "" + msg, Toast.LENGTH_SHORT).show();
    }

    private Spinner spinnerLoginMood;
    private TextView textViewRegister,textViewForgotPass;
    private EditText editTextEmail, editTextPassword;
    private Button btnLogin;
    private boolean drawableETPassCheck = false;
    private ProgressBarAdapter progressBarAdapter;
    private AdapterSharedPref sharedPref;
    private ImageView imageView;

    private boolean _drawableCheck = false;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        hide title bar
        WindowManager.LayoutParams attrs = getWindow().getAttributes();
        attrs.flags = WindowManager.LayoutParams.FLAG_FULLSCREEN;
        getWindow().setAttributes(attrs);
        setContentView(R.layout.activity_login);
//        init progress bar
        progressBarAdapter = new ProgressBarAdapter(LoginActivity.this);
//        init sharedPref
        sharedPref = new AdapterSharedPref(getApplicationContext());

//        check if login
        if (sharedPref.read_login_status() && !sharedPref.getLoginMood().equals("")){
            String _mode = sharedPref.getLoginMood();
            if (_mode.equals(Common.AS_DOCTOR)){
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                intent.putExtra("ex_login_mood_from_login", Common.AS_DOCTOR);
                startActivity(intent);
                finish();
            }else if (_mode.equals(Common.AS_PATIENT)){
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                intent.putExtra("ex_login_mood_from_login", Common.AS_PATIENT);
                startActivity(intent);
                finish();
            }

        }

//        finding xml
        spinnerLoginMood = findViewById(R.id.spinnerSelectLoginMood);
        textViewRegister = findViewById(R.id.tvRegister);
        textViewForgotPass = findViewById(R.id.tvForgotPassLogin);
        editTextEmail = findViewById(R.id.etEmailLogin);
        editTextPassword = findViewById(R.id.etPasswordlLogin);
        btnLogin = findViewById(R.id.btnLogin);
        imageView = findViewById(R.id.image_view_login);
        //set image
        Glide.with(this).load(R.mipmap.ic_launcher_round).circleCrop().into(imageView);

//        set drawable in password field
        editTextPassword.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_lock_24, 0, R.drawable.ic_baseline_visibility_off_24, 0);

//        init spinner login mood
        initSpinner();

//        show and hide password
        editTextPassword.setOnTouchListener((v, event) -> {
            final int DRAWABLE_LEFT = 0;
            final int DRAWABLE_TOP = 1;
            final int DRAWABLE_RIGHT = 2;
            final int DRAWABLE_BOTTOM = 3;

            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getRawX() >= (editTextPassword.getRight() - editTextPassword.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                    // your action here
                    if (!drawableETPassCheck) {
                        editTextPassword.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_lock_24, 0, R.drawable.ic_baseline_visibility_24, 0);
                        editTextPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                        drawableETPassCheck = true;

                    } else {
                        editTextPassword.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_lock_24, 0, R.drawable.ic_baseline_visibility_off_24, 0);
                        editTextPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        drawableETPassCheck = false;

                    }

                    return true;
                }
            }
            return false;
        });

//        forgot password text view click listener
        textViewForgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initForgotPass();
            }
        });

//        login button click listener
        btnLogin.setOnClickListener(v -> {
            initLogin();
        });

//        text view register click listener
        textViewRegister.setOnClickListener(v -> initRegisterMood());
    }

//    for forgot password
    private void initForgotPass() {
        Dialog dialog = new Dialog(LoginActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_forgot_pass);
        dialog.setCanceledOnTouchOutside(false);

//        find xml
        TextView textViewTitle = dialog.findViewById(R.id.tvForgotPassTitle);
        Spinner spinner = dialog.findViewById(R.id.spinnerSelectForgotMood);
        View view = dialog.findViewById(R.id.viewForgotId);
        EditText email = dialog.findViewById(R.id.etForgotPassEmail);
        EditText phone = dialog.findViewById(R.id.etForgotPassPhone);
        EditText pass = dialog.findViewById(R.id.etForgotPassPass);
        pass.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_lock_24, 0, R.drawable.ic_baseline_visibility_off_24, 0);

        EditText conPass = dialog.findViewById(R.id.etForgotPassConPass);
        conPass.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_lock_24, 0, 0, 0);

        Button btnCancel = dialog.findViewById(R.id.btnForgotPassCancelId);
        Button btnOk = dialog.findViewById(R.id.btnForgotPassOkId);
        Button btnChange = dialog.findViewById(R.id.btnForgotPassChangeId);
        ProgressBar progressBar = dialog.findViewById(R.id.progressCustomForgotPassId);
        TextView textViewWarning = dialog.findViewById(R.id.tvWarningForgotPassId);

//        init spinner
        String[] forgotMood = getResources().getStringArray(R.array.select_login_mood);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, forgotMood) {
            @Override
            public boolean isEnabled(int position) {
                return position != 0;
            }
        };
        spinner.setAdapter(adapter);

        // init view
        pass.setVisibility(View.GONE);
        conPass.setVisibility(View.GONE);
        btnChange.setVisibility(View.GONE);
        textViewWarning.setVisibility(View.GONE);

//        cancel button click listener
        btnCancel.setOnClickListener(v -> dialog.dismiss());
//        ok button click listener
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String _forgotMode = spinner.getSelectedItem().toString();
                String _forgot_email = email.getText().toString().trim();
                String _forgot_ph = phone.getText().toString().trim();

                if (_forgotMode.equals("--select mode--")){
                    showToast("Select Mode");
                } else if (_forgot_email.isEmpty()){
                    email.setError("Please Enter Email");
                    email.requestFocus();
                }else if (!Patterns.EMAIL_ADDRESS.matcher(_forgot_email).matches()){
                    email.setError("Email Address Not Valid!");
                    email.requestFocus();
                }else if (_forgot_ph.isEmpty()){
                    phone.setError("Please Enter Phone Number");
                    phone.requestFocus();
                }else {
                    view.setVisibility(View.GONE);
                    progressBar.setVisibility(View.VISIBLE);

                    String _mode = null;
                    if (_forgotMode.equals("As Doctor")){
                        _mode = "d";
                    }else if (_forgotMode.equals("As Patient")){
                        _mode = "p";
                    }
                    ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
                    Call<ModelForgotPass> call = apiInterface.check_User_Email_And_Phone(_mode,_forgot_email,_forgot_ph);
                    call.enqueue(new Callback<ModelForgotPass>() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onResponse(Call<ModelForgotPass> call, Response<ModelForgotPass> response) {
                            if (response.isSuccessful() && response.body()!=null){
                                ModelForgotPass model = response.body();
                                if (model.isSuccess()){
                                    view.setVisibility(View.VISIBLE);
                                    progressBar.setVisibility(View.GONE);

                                    email.setVisibility(View.GONE);
                                    phone.setVisibility(View.GONE);
                                    btnOk.setVisibility(View.GONE);
                                    spinner.setVisibility(View.GONE);
                                    textViewWarning.setVisibility(View.GONE);

                                    pass.setVisibility(View.VISIBLE);
                                    conPass.setVisibility(View.VISIBLE);
                                    btnChange.setVisibility(View.VISIBLE);
//                                    set title
                                    textViewTitle.setText("Enter New Password And Confirm");

//                                    update password
                                    updatePass(view,progressBar,btnChange,pass,conPass,_forgotMode,model.getId(),dialog);

                                }else {
                                    view.setVisibility(View.VISIBLE);
                                    progressBar.setVisibility(View.GONE);

                                    textViewWarning.setVisibility(View.VISIBLE);
                                    textViewWarning.setText(model.getMessage());
                                }
                            }else {
                                view.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.GONE);

                                showToast("something wrong to check email and phone");
                            }
                        }

                        @Override
                        public void onFailure(Call<ModelForgotPass> call, Throwable t) {
                            view.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);

                            showToast(t.toString());
                        }
                    });



                }

            }
        });

        // show dialog
        dialog.show();
    }

//    for update password
    @SuppressLint("ClickableViewAccessibility")
    private void updatePass(View view,ProgressBar progressBar,Button btnChange, EditText pass, EditText conPass, String forgotMode, String id,Dialog dialog) {
//        show and hide password
        pass.setOnTouchListener((v, event) -> {
            final int DRAWABLE_LEFT = 0;
            final int DRAWABLE_TOP = 1;
            final int DRAWABLE_RIGHT = 2;
            final int DRAWABLE_BOTTOM = 3;

            if(event.getAction() == MotionEvent.ACTION_UP) {
                if(event.getRawX() >= (pass.getRight() - pass.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                    // your action here
                    if (!_drawableCheck){
                        pass.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_lock_24, 0, R.drawable.ic_baseline_visibility_24, 0);
                        conPass.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_lock_24, 0, 0, 0);
                        pass.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                        conPass.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                        _drawableCheck=true;

                    }else {
                        pass.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_lock_24, 0, R.drawable.ic_baseline_visibility_off_24, 0);
                        conPass.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_lock_24, 0, 0, 0);
                        pass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        conPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        _drawableCheck=false;

                    }

                    return true;
                }
            }
            return false;
        });


        btnChange.setOnClickListener(v -> {
            String _mode = null;
            if (forgotMode.equals("As Doctor")){
                _mode = "d";
            }else if (forgotMode.equals("As Patient")){
                _mode = "p";
            }

            String _pass = pass.getText().toString();
            String _conPass = conPass.getText().toString();

            if (_pass.isEmpty()){
                pass.setError("Please enter Password");
                pass.requestFocus();
            }else if (_conPass.isEmpty()){
                conPass.setError("Enter Confirmation Password");
                conPass.requestFocus();
            }else if (!_conPass.equals(_pass)){
                conPass.setError("Password Not Match");
                conPass.requestFocus();
            }else {
                view.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
//                update password
                ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
                Call<ModelForgotPass> call = apiInterface.update_User_Pass(_mode,id,_pass);
                call.enqueue(new Callback<ModelForgotPass>() {
                    @Override
                    public void onResponse(Call<ModelForgotPass> call, Response<ModelForgotPass> response) {
                        if (response.isSuccessful() && response.body()!=null){
                            ModelForgotPass model = response.body();
                            if (model.isSuccess()){
                                progressBar.setVisibility(View.GONE);
                                view.setVisibility(View.VISIBLE);
                                showToast(model.getMessage());
                                dialog.dismiss();
                            }else {
                                progressBar.setVisibility(View.GONE);
                                view.setVisibility(View.VISIBLE);
                                showToast(model.getMessage());
                            }
                        }else {
                            progressBar.setVisibility(View.GONE);
                            view.setVisibility(View.VISIBLE);
                            showToast("something wrong to update password");
                        }
                    }

                    @Override
                    public void onFailure(Call<ModelForgotPass> call, Throwable t) {
                        progressBar.setVisibility(View.GONE);
                        view.setVisibility(View.VISIBLE);
                        showToast(t.toString());
                    }
                });
            }

        });
    }


    private void initLogin() {
        String _loginMood = spinnerLoginMood.getSelectedItem().toString();

        if (_loginMood.equals("--select mode--")) {
            showToast("Please Select Login Mood");
        } else if (_loginMood.equals("As Doctor")) {
//            login as doctor
            String _email = editTextEmail.getText().toString();
            String _pass = editTextPassword.getText().toString().trim();

            if (_email.isEmpty()){
                editTextEmail.setError("Please Enter Email");
                editTextEmail.requestFocus();
            }else if (!Patterns.EMAIL_ADDRESS.matcher(_email).matches()){
                editTextEmail.setError("Please Enter Valid Email");
                editTextEmail.requestFocus();
            }else if (_pass.isEmpty()){
                editTextPassword.setError("Please Enter Password");
                editTextPassword.requestFocus();
            }else {
//                progress bar show
                progressBarAdapter.startLoadingDialog();
                ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
                Call<ModelLogin> call = apiInterface.user_Login("d",_email,_pass);
                call.enqueue(new Callback<ModelLogin>() {
                    @Override
                    public void onResponse(Call<ModelLogin> call, Response<ModelLogin> response) {
                        if (response.isSuccessful() && response.body()!=null){
                            ModelLogin model = response.body();
                            if (model.isSuccess()){
                                //progress bar dismiss
                                progressBarAdapter.dismissDialog();
                                //sharedPref
                                sharedPref.login_status(true);
                                sharedPref.setUserId(model.getId());
                                sharedPref.setUserName(model.getName());
                                sharedPref.setUserGender("doctor_gender");
                                sharedPref.setUserAge("doctor_age");
                                sharedPref.setUserEmail(_email);
                                sharedPref.setUser_pass(_pass);
                                sharedPref.setUserPhone(model.getPhone());
                                sharedPref.setProfile_img(model.getProfileImgPath());
                                sharedPref.setLoginMood(Common.AS_DOCTOR);
                                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                                intent.putExtra("ex_login_mood_from_login", Common.AS_DOCTOR);
                                startActivity(intent);
                                finish();

                            }else {
                                progressBarAdapter.dismissDialog();
                                showToast(model.getMessage());
                            }
                        }else {
                            progressBarAdapter.dismissDialog();
                            showToast("something wrong to login as doctor");
                        }
                    }

                    @Override
                    public void onFailure(Call<ModelLogin> call, Throwable t) {
                        progressBarAdapter.dismissDialog();
                        showToast(t.toString());
                    }
                });

            }


        } else if (_loginMood.equals("As Patient")) {
//            login as patient
            String _email = editTextEmail.getText().toString();
            String _pass = editTextPassword.getText().toString().trim();

            if (_email.isEmpty()){
                editTextEmail.setError("Please Enter Email");
                editTextEmail.requestFocus();
            }else if (!Patterns.EMAIL_ADDRESS.matcher(_email).matches()){
                editTextEmail.setError("Please Enter Valid Email");
                editTextEmail.requestFocus();
            }else if (_pass.isEmpty()){
                editTextPassword.setError("Please Enter Password");
                editTextPassword.requestFocus();
            }else {
//                progress bar show
                progressBarAdapter.startLoadingDialog();
                ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
                Call<ModelLogin> call = apiInterface.user_Login("p",_email,_pass);
                call.enqueue(new Callback<ModelLogin>() {
                    @Override
                    public void onResponse(Call<ModelLogin> call, Response<ModelLogin> response) {
                        if (response.isSuccessful() && response.body()!=null){
                            ModelLogin model = response.body();
                            if (model.isSuccess()){
                                //progress bar dismiss
                                progressBarAdapter.dismissDialog();
                                //sharedPref
                                sharedPref.login_status(true);
                                sharedPref.setUserId(model.getId());
                                sharedPref.setUserName(model.getName());
                                sharedPref.setUserGender(model.getGender());
                                sharedPref.setUserAge(model.getAge());
                                sharedPref.setUserEmail(_email);
                                sharedPref.setUser_pass(_pass);
                                sharedPref.setUserPhone(model.getPhone());
                                sharedPref.setProfile_img(model.getProfileImgPath());
                                sharedPref.setLoginMood(Common.AS_PATIENT);
                                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                                intent.putExtra("ex_login_mood_from_login", Common.AS_PATIENT);
                                startActivity(intent);
                                finish();
                            }else {
                                progressBarAdapter.dismissDialog();
                                showToast(model.getMessage());
                            }
                        }else {
                            progressBarAdapter.dismissDialog();
                            showToast("something wrong to login as patient");
                        }
                    }

                    @Override
                    public void onFailure(Call<ModelLogin> call, Throwable t) {
                        progressBarAdapter.dismissDialog();
                        showToast(t.toString());
                    }
                });
            }


        }

    }

    //    for choose register mood
    private void initRegisterMood() {
        Dialog dialog = new Dialog(LoginActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_register_mood_layout);
        dialog.setCanceledOnTouchOutside(false);

//        find resource
        Button btnDoctor = dialog.findViewById(R.id.btnCustomRegistrationMoodDoctor);
        Button btnPatient = dialog.findViewById(R.id.btnCustomRegistrationMoodPatient);

        btnDoctor.setEnabled(true);
        btnPatient.setEnabled(true);

//        button doctor click listener
        btnDoctor.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            intent.putExtra("ex_register_mood", Common.AS_DOCTOR);
            startActivity(intent);
            dialog.dismiss();
        });
//        button patient click listener
        btnPatient.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            intent.putExtra("ex_register_mood", Common.AS_PATIENT);
            startActivity(intent);
            dialog.dismiss();
        });

        // show dialog
        dialog.show();
    }

    //    for select login mood spinner
    private void initSpinner() {
        String[] loginMood = getResources().getStringArray(R.array.select_login_mood);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, loginMood) {
            @Override
            public boolean isEnabled(int position) {
                return position != 0;
            }
        };
        spinnerLoginMood.setAdapter(adapter);
    }
}