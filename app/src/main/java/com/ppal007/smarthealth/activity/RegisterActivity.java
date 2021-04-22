package com.ppal007.smarthealth.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ppal007.smarthealth.R;
import com.ppal007.smarthealth.adapter.spinnerAdapter.AdapterSpinnerDoctorName;
import com.ppal007.smarthealth.adapter.progressBar.ProgressBarAdapter;
import com.ppal007.smarthealth.adapter.spinnerAdapter.AdapterSpinnerLocation;
import com.ppal007.smarthealth.model.ModelRegisterAsDoctor;
import com.ppal007.smarthealth.model.ModelRegisterAsPatient;
import com.ppal007.smarthealth.model.ModelRetrieveDoctorName;
import com.ppal007.smarthealth.model.RegiDoctor;
import com.ppal007.smarthealth.retrofit.ApiClient;
import com.ppal007.smarthealth.retrofit.ApiInterface;
import com.ppal007.smarthealth.utils.Common;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    private void showToast(String msg){
        Toast.makeText(this, ""+msg, Toast.LENGTH_SHORT).show();
    }

    private static final int CAMERA_REQUEST_CODE = 11111;
    private static final int GALLERY_REQUEST_CODE = 22222;

    private static final int CAMERA_REQUEST_CODE_PROFILE = 111;
    private static final int GALLERY_REQUEST_CODE_PROFILE = 222;

    private ProgressBarAdapter progressBarAdapter;


    private TextView textViewTitle, textViewLogin,textViewSelectGender,textViewChooseDoctor;
    private String registerTitle;
    private EditText editTextName, editTextEmail, editTextPhone, editTextIdNumber,
            editTextAge, editTextPass, editTextConPass,editTextDoctorLocation;
    private Spinner spinnerChooseDoctor,spinnerLocation;
    private LinearLayout linearLayoutUploadDocument,linearLayoutLocation;
    private RelativeLayout relativeLayout;
    private ImageView imageViewDocument,imageViewProfile;
    private Button btnUploadDocument, btnRegister;
    private ImageButton imageButtonCancel,imageButtonCancelProfile;
    private RadioGroup radioGroup;
    private RadioButton radioButton;

    private boolean drawableETPassCheck = false;
    private boolean profile_bool = false;

    private Drawable drawable;
    private Bitmap bitmap;
    private Bitmap bitmap_profile;

    private String _doctorId;

    @SuppressLint({"SetTextI18n", "ClickableViewAccessibility"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // hide title bar
        WindowManager.LayoutParams attrs = getWindow().getAttributes();
        attrs.flags = WindowManager.LayoutParams.FLAG_FULLSCREEN;
        getWindow().setAttributes(attrs);
        setContentView(R.layout.activity_register);

//        get bundle value from Login Activity
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            registerTitle = bundle.getString("ex_register_mood");
        }
//        camera permission
        cameraPermission();
//        init progress bar adapter
        progressBarAdapter = new ProgressBarAdapter(RegisterActivity.this);

//        finding xml
        initView();

//        set drawable in password and confirm password field
        editTextPass.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_lock_24, 0, R.drawable.ic_baseline_visibility_off_24, 0);
        editTextConPass.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_lock_24, 0, R.drawable.ic_baseline_visibility_off_24, 0);

//        show and hide password
        initPasswordShowHide();

//        text view login click listener
        textViewLogin.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        });

//        select profile pic
        imageViewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initUploadDoc("profile_img");
            }
        });

//        set view
        if (registerTitle.equals(Common.AS_DOCTOR)) {
            textViewTitle.setText("Registration As Doctor");
//            show views
            editTextName.setVisibility(View.VISIBLE);
            editTextEmail.setVisibility(View.VISIBLE);
            editTextPhone.setVisibility(View.VISIBLE);
            editTextPass.setVisibility(View.VISIBLE);
            editTextConPass.setVisibility(View.VISIBLE);
            linearLayoutUploadDocument.setVisibility(View.VISIBLE);
            btnUploadDocument.setVisibility(View.VISIBLE);
            editTextDoctorLocation.setVisibility(View.VISIBLE);

//            hide views
            textViewSelectGender.setVisibility(View.GONE);
            radioGroup.setVisibility(View.GONE);
            relativeLayout.setVisibility(View.GONE);
//            imageViewDocument.setVisibility(View.GONE);
            editTextAge.setVisibility(View.GONE);
            spinnerChooseDoctor.setVisibility(View.GONE);
            editTextIdNumber.setVisibility(View.GONE);
            textViewChooseDoctor.setVisibility(View.GONE);
            linearLayoutLocation.setVisibility(View.GONE);

//            init upload documents
            initUploadDoc("doctor_document");
//            init register as doctor
            initRegister(Common.AS_DOCTOR);

        } else if (registerTitle.equals(Common.AS_PATIENT)) {
            //retrieve location
            retrieveLocation();
//            retrieve doctor name
            //retrieveDoctorName();
            textViewTitle.setText("Registration As Patient");
//            show views
            editTextName.setVisibility(View.VISIBLE);
            editTextEmail.setVisibility(View.VISIBLE);
            editTextPhone.setVisibility(View.VISIBLE);
            editTextPass.setVisibility(View.VISIBLE);
            editTextConPass.setVisibility(View.VISIBLE);
            editTextAge.setVisibility(View.VISIBLE);
            textViewSelectGender.setVisibility(View.VISIBLE);
            radioGroup.setVisibility(View.VISIBLE);
            spinnerChooseDoctor.setVisibility(View.VISIBLE);
            editTextIdNumber.setVisibility(View.VISIBLE);
            textViewChooseDoctor.setVisibility(View.VISIBLE);
            linearLayoutLocation.setVisibility(View.VISIBLE);

//            hide views
            linearLayoutUploadDocument.setVisibility(View.GONE);
            editTextDoctorLocation.setVisibility(View.GONE);

//            init spinner choose doctor
            //initSpinnerSelect();
            initSpinnerLocation();
//            init register as patient
            initRegister(Common.AS_PATIENT);

        }

    }

    private void initSpinnerLocation() {
        spinnerLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ModelRegisterAsDoctor modelRegisterAsDoctor = (ModelRegisterAsDoctor) parent.getItemAtPosition(position);

                //showToast(modelRegisterAsDoctor.getLocation());
                retrieveDoctorName(modelRegisterAsDoctor.getLocation());

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    //    get doctor location
    private void retrieveLocation() {
        progressBarAdapter.startLoadingDialog();
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<List<ModelRegisterAsDoctor>> call = apiInterface.retrieve_doctor_location();
        call.enqueue(new Callback<List<ModelRegisterAsDoctor>>() {
            @Override
            public void onResponse(Call<List<ModelRegisterAsDoctor>> call, Response<List<ModelRegisterAsDoctor>> response) {
                if (response.isSuccessful() && response.body()!=null){
                    List<ModelRegisterAsDoctor> model = response.body();


                    AdapterSpinnerLocation adapterSpinnerLocation= new AdapterSpinnerLocation(RegisterActivity.this,model);
                    spinnerLocation.setAdapter(adapterSpinnerLocation);
                    progressBarAdapter.dismissDialog();
                }else {
                    showToast("something wrong");
                    progressBarAdapter.dismissDialog();
                }
            }

            @Override
            public void onFailure(Call<List<ModelRegisterAsDoctor>> call, Throwable t) {
                showToast(t.toString());
                progressBarAdapter.dismissDialog();

            }
        });

    }

    @SuppressLint("ClickableViewAccessibility")
    private void initPasswordShowHide() {
        editTextPass.setOnTouchListener((v, event) -> {
            final int DRAWABLE_LEFT = 0;
            final int DRAWABLE_TOP = 1;
            final int DRAWABLE_RIGHT = 2;
            final int DRAWABLE_BOTTOM = 3;

            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getRawX() >= (editTextPass.getRight() - editTextPass.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                    // your action here
                    if (!drawableETPassCheck) {
                        editTextPass.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_lock_24, 0, R.drawable.ic_baseline_visibility_24, 0);
                        editTextConPass.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_lock_24, 0, 0, 0);
                        editTextPass.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                        editTextConPass.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                        drawableETPassCheck = true;

                    } else {
                        editTextPass.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_lock_24, 0, R.drawable.ic_baseline_visibility_off_24, 0);
                        editTextConPass.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_lock_24, 0, R.drawable.ic_baseline_visibility_off_24, 0);
                        editTextPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        editTextConPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        drawableETPassCheck = false;

                    }

                    return true;
                }
            }
            return false;
        });
    }

    private void initView() {
        textViewTitle              = findViewById(R.id.tvRegisterTitle);
        textViewLogin              = findViewById(R.id.tvLogin);
        editTextName               = findViewById(R.id.etNameRegister);
        editTextEmail              = findViewById(R.id.etEmailRegister);
        editTextPhone              = findViewById(R.id.etPhoneRegister);
        editTextAge                = findViewById(R.id.etAgeRegister);
        editTextPass               = findViewById(R.id.etPasswordRegister);
        editTextConPass            = findViewById(R.id.etConfirmPasswordRegister);
        spinnerChooseDoctor        = findViewById(R.id.spinnerRegisterChooseDoctor);
        linearLayoutUploadDocument = findViewById(R.id.linearLayoutRegisterDocument);
        relativeLayout             = findViewById(R.id.relativeLayoutId);
        imageViewDocument          = findViewById(R.id.imgViewRegisterUploadDocuments);
        btnUploadDocument          = findViewById(R.id.btnRegisterUploadDocuments);
        btnRegister                = findViewById(R.id.btnRegisterSubmitId);
        imageButtonCancel          = findViewById(R.id.imageButtonCancelId);
        radioGroup                 = findViewById(R.id.radioGroupId);
        imageButtonCancelProfile   = findViewById(R.id.imageButtonCancelProfileId);
        textViewSelectGender       = findViewById(R.id.tvSelectGenderLabelId);
        imageViewProfile           = findViewById(R.id.imageViewProfileId);
        editTextIdNumber           = findViewById(R.id.etIdNumberRegister);
        textViewChooseDoctor       = findViewById(R.id.tvChooseDoctor_label);
        editTextDoctorLocation     = findViewById(R.id.etDoctorLocationRegister);
        spinnerLocation = findViewById(R.id.spinner_location);
        linearLayoutLocation = findViewById(R.id.linear_select_location_id);
    }

    //    for retrieve doctor name
    private void retrieveDoctorName(String location) {
        progressBarAdapter.startLoadingDialog();
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<List<ModelRetrieveDoctorName>> call = apiInterface.retrieve_doctor_name(location);
        call.enqueue(new Callback<List<ModelRetrieveDoctorName>>() {
            @Override
            public void onResponse(Call<List<ModelRetrieveDoctorName>> call, Response<List<ModelRetrieveDoctorName>> response) {
                if (response.isSuccessful() && response.body()!=null){
                    List<ModelRetrieveDoctorName> model = response.body();

                    AdapterSpinnerDoctorName adapterSpinnerDoctorName = new AdapterSpinnerDoctorName(RegisterActivity.this,model);
                    spinnerChooseDoctor.setAdapter(adapterSpinnerDoctorName);
                    //init spinner
                    initSpinnerSelect();

                }else {
                    showToast("something wrong to retrieve doctor name");
                }
                progressBarAdapter.dismissDialog();
            }

            @Override
            public void onFailure(Call<List<ModelRetrieveDoctorName>> call, Throwable t) {
                showToast(t.toString());
                progressBarAdapter.dismissDialog();
            }
        });
    }

    //    for register button click
    private void initRegister(String as) {
        btnRegister.setOnClickListener(v -> {
            if (as.equals(Common.AS_DOCTOR)){
//            register for doctor
                String _name = editTextName.getText().toString();
                String _email = editTextEmail.getText().toString();
                String _phone = editTextPhone.getText().toString().trim();
                String _pass = editTextPass.getText().toString().trim();
                String _conPass = editTextConPass.getText().toString().trim();
                String _location = editTextDoctorLocation.getText().toString();

                if (_name.isEmpty()){
                    editTextName.setError("Please Enter Name");
                    editTextName.requestFocus();
                }else if (_email.isEmpty()){
                    editTextEmail.setError("Please Enter Email");
                    editTextEmail.requestFocus();
                }else if (!Patterns.EMAIL_ADDRESS.matcher(_email).matches()){
                    editTextEmail.setError("Please Enter Valid Email");
                    editTextEmail.requestFocus();
                }else if (_phone.isEmpty()){
                    editTextPhone.setError("Please Enter Phone Number");
                    editTextPhone.requestFocus();
                }else if (_pass.isEmpty()){
                    editTextPass.setError("Please Enter Password");
                    editTextPass.requestFocus();
                }else if (_pass.length() < 6){
                    editTextPass.setError("Password must be >= 6");
                    editTextPass.requestFocus();
                } else if (_conPass.isEmpty()){
                    editTextConPass.setError("Please Enter Confirmation Password");
                    editTextConPass.requestFocus();
                }else if (!_conPass.equals(_pass)){
                    editTextConPass.setError("Password Not Match!");
                    editTextConPass.requestFocus();
                }else if (_location.isEmpty()){
                    editTextDoctorLocation.setError("Enter Your Location");
                    editTextDoctorLocation.requestFocus();
                } else if (imageViewDocument.getDrawable() == null){
                    showToast("Please Upload Documents");
                }else if (!profile_bool){
                    showToast("Please Upload Profile Picture");
                }else {
//                    show progress bar
                    progressBarAdapter.startLoadingDialog();
//                    convert image to string
                    String img_str = imageToString();
//                    convert profile image to string
                    String _profile_img_str = imageToString_profile();

                    //Log.d("TAG_pp", "onResponse befaur: "+_name+"\n"+_email+"\n"+_phone+"\n"+_pass+"\n"+_profile_img_str+"\n"+img_str+"\n"+_location);

//                    upload information to database
                    ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
                    Call<RegiDoctor> call = apiInterface
                            .register_doctor(_name,_email,_phone,_pass,_profile_img_str,img_str,_location);
                    call.enqueue(new Callback<RegiDoctor>() {
                        @Override
                        public void onResponse(Call<RegiDoctor> call, Response<RegiDoctor> response) {
                            if (response.isSuccessful() && response.body()!=null){
                                RegiDoctor model = response.body();
                                //Log.d("TAG_pp", "onResponse: after "+_name+"\n"+_email+"\n"+_phone+"\n"+_pass+"\n"+_profile_img_str+"\n"+img_str+"\n"+_location);
                                if (model.isSuccess()){
                                    //Log.d("TAG_pp", "onResponse: success"+_name+"\n"+_email+"\n"+_phone+"\n"+_pass+"\n"+_profile_img_str+"\n"+img_str+"\n"+_location);

                                    String lastId = model.getLastInsId();
                                    registerFirebaseForMsg(progressBarAdapter,lastId,_name,_email,_pass,Common.MODE_D);
                                    Log.d("TAG_pp", "onResponse: "+model.getMessage());
                                }else {
                                    progressBarAdapter.dismissDialog();
                                    showToast(model.getMessage());
                                    Log.d("TAG_pp", "onResponse: "+model.getMessage());
                                }
                            }else {
                                progressBarAdapter.dismissDialog();
                                showToast("something wrong to register as doctor");
                                Log.d("TAG_pp", "onResponse: "+response.toString());

                            }
                        }

                        @Override
                        public void onFailure(Call<RegiDoctor> call, Throwable t) {
                            Log.d("TAG_pp", "onResponse: "+t.toString());
                            progressBarAdapter.dismissDialog();
                            showToast(t.toString());
                        }
                    });
                }
            }else if (as.equals(Common.AS_PATIENT)){
//                register for patient
                String _name = editTextName.getText().toString();
                String _email = editTextEmail.getText().toString();
                String _phone = editTextPhone.getText().toString().trim();
                String _idNumber = editTextIdNumber.getText().toString().trim();
                String _age = editTextAge.getText().toString().trim();
                String _pass = editTextPass.getText().toString().trim();
                String _conPass = editTextConPass.getText().toString().trim();

                if (_name.isEmpty()){
                    editTextName.setError("Please Enter Name");
                    editTextName.requestFocus();
                }else if (_email.isEmpty()){
                    editTextEmail.setError("Please Enter Email");
                    editTextEmail.requestFocus();
                }else if (!Patterns.EMAIL_ADDRESS.matcher(_email).matches()){
                    editTextEmail.setError("Please Enter Valid Email");
                    editTextEmail.requestFocus();
                }else if (_phone.isEmpty()){
                    editTextPhone.setError("Please Enter Phone Number");
                    editTextPhone.requestFocus();
                }else if (_idNumber.isEmpty()){
                    editTextIdNumber.setError("Please Enter Id Number");
                    editTextIdNumber.requestFocus();
                }else if (_age.isEmpty()){
                    editTextAge.setError("Please Enter Age");
                    editTextAge.requestFocus();
                }else if (radioGroup.getCheckedRadioButtonId() == -1){
                    showToast("Please Select Gender");
                }else if (_pass.isEmpty()){
                    editTextPass.setError("Please Enter Password");
                    editTextPass.requestFocus();
                }else if (_pass.length() < 6){
                    editTextPass.setError("Password must be >= 6");
                    editTextPass.requestFocus();
                } else if (_conPass.isEmpty()){
                    editTextConPass.setError("Please Enter Confirmation Password");
                    editTextConPass.requestFocus();
                }else if (!_conPass.equals(_pass)){
                    editTextConPass.setError("Password Not Match!");
                    editTextConPass.requestFocus();
                }else if (!profile_bool){
                    showToast("Please Upload Profile Picture");
                } else {
                    String _profile_img_str = imageToString_profile();
//                    start progress bar
                    progressBarAdapter.startLoadingDialog();
                    ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
                    Call<ModelRegisterAsPatient> call = apiInterface.register_patient(_name,_email,_phone,_idNumber,_age,radioButton.getText().toString(),_pass,_doctorId,_profile_img_str);
                    call.enqueue(new Callback<ModelRegisterAsPatient>() {
                        @Override
                        public void onResponse(Call<ModelRegisterAsPatient> call, Response<ModelRegisterAsPatient> response) {
                            if (response.isSuccessful() && response.body()!=null){
                                ModelRegisterAsPatient model = response.body();
                                if (model.isSuccess()){

                                    String lastId = model.getLastInsId();
                                    registerFirebaseForMsg(progressBarAdapter,lastId,_name,_email,_pass,Common.MODE_P);
                                }else {
                                    progressBarAdapter.dismissDialog();
                                    showToast(model.getMessage());
                                }
                            }else {
                                progressBarAdapter.dismissDialog();
                                showToast("something wrong to register as patient");
                            }
                        }

                        @Override
                        public void onFailure(Call<ModelRegisterAsPatient> call, Throwable t) {
                            progressBarAdapter.dismissDialog();
                            showToast(t.toString());
                        }
                    });
                }
            }
        });

    }

    private void registerFirebaseForMsg(ProgressBarAdapter progressBarAdapter,String lstId, String name, String email, String pass,String mode) {
        FirebaseAuth auth = FirebaseAuth.getInstance();

        //register
        auth.createUserWithEmailAndPassword(email,pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            FirebaseUser firebaseUser = auth.getCurrentUser();
                            String Id = firebaseUser.getUid();

                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference(Common.FIREBASE_USER_DB).child(Id);
                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("id",Id);
                            hashMap.put("useremail",email);
                            hashMap.put("userid",lstId);
                            hashMap.put("username",name);
                            hashMap.put("usermode",mode);

                            reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    progressBarAdapter.dismissDialog();
                                    Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            });

                        }
                    }
                });
    }

    //    for radio button check
    public void chkBtn(View view){
        int radioId = radioGroup.getCheckedRadioButtonId();
        radioButton = findViewById(radioId);
    }

    //    for camera open permission
    private void cameraPermission() {
        // request for camera permission
        if (ContextCompat.checkSelfPermission(RegisterActivity.this,
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(RegisterActivity.this,
                    new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST_CODE);
        }
    }

    //    for doctors upload documents
    @SuppressLint("IntentReset")
    private void initUploadDoc(String mode) {
        if (mode.equals("doctor_document")){
            //upload documents button click listener
            btnUploadDocument.setOnClickListener(v -> {
                AlertDialog.Builder mDialog = new AlertDialog.Builder(RegisterActivity.this);
                mDialog.setIcon(R.drawable.ic_baseline_photo_camera_24);
                mDialog.setTitle("Take Photo");
                mDialog.setMessage("Take Photo or Open Gallery");
                mDialog.setCancelable(true);

                mDialog.setNeutralButton("Camera", (dialog1, which) -> {
//                        open camera
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, CAMERA_REQUEST_CODE);
                    dialog1.dismiss();

                }).setPositiveButton("Gallery", (dialog12, which) -> {
//                        open gallery
                    @SuppressLint("IntentReset") Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(Intent.createChooser(intent, "Select File"), GALLERY_REQUEST_CODE);
                    dialog12.dismiss();

                });
                AlertDialog alertDialog = mDialog.create();
                alertDialog.show();
            });

//        cancel image button click listener
            imageButtonCancel.setOnClickListener(v -> {
                imageViewDocument.setImageBitmap(null);
                imageViewDocument.setImageDrawable(null);

                relativeLayout.setVisibility(View.GONE);
                btnUploadDocument.setVisibility(View.VISIBLE);
            });
        }else if (mode.equals("profile_img")){
            AlertDialog.Builder mDialog = new AlertDialog.Builder(RegisterActivity.this);
            mDialog.setIcon(R.drawable.ic_baseline_photo_camera_24);
            mDialog.setTitle("Take Photo");
            mDialog.setMessage("Take Photo or Open Gallery");
            mDialog.setCancelable(true);

            mDialog.setNeutralButton("Camera", (dialog1, which) -> {
//                        open camera
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, CAMERA_REQUEST_CODE_PROFILE);
                dialog1.dismiss();

            }).setPositiveButton("Gallery", (dialog12, which) -> {
//                        open gallery
                @SuppressLint("IntentReset") Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Select File"), GALLERY_REQUEST_CODE_PROFILE);
                dialog12.dismiss();

            });
            AlertDialog alertDialog = mDialog.create();
            alertDialog.show();

//            cancel profile image button click listener
            imageButtonCancelProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    imageViewProfile.setImageBitmap(null);
                    imageViewProfile.setImageDrawable(null);
                    imageViewProfile.setImageResource(R.drawable.ic_upload_profile_img);

                    imageButtonCancelProfile.setVisibility(View.GONE);
                    profile_bool = false;
                }
            });
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == CAMERA_REQUEST_CODE) {
//                get capture image
                assert data != null;
                Bitmap captureImage = (Bitmap) Objects.requireNonNull(data.getExtras()).get("data");
//                set capture image to imageView
                relativeLayout.setVisibility(View.VISIBLE);
                imageViewDocument.setImageBitmap(captureImage);

                btnUploadDocument.setVisibility(View.GONE);

            } else if (requestCode == GALLERY_REQUEST_CODE) {

//                get image from gallery
                assert data != null;
                Uri selectImageUri = data.getData();
//                set image to imageView
                relativeLayout.setVisibility(View.VISIBLE);
                imageViewDocument.setImageURI(selectImageUri);

                btnUploadDocument.setVisibility(View.GONE);

            }else if (requestCode == CAMERA_REQUEST_CODE_PROFILE){
                Bitmap captureImage = (Bitmap) Objects.requireNonNull(data.getExtras()).get("data");
                imageViewProfile.setImageBitmap(captureImage);
                imageButtonCancelProfile.setVisibility(View.VISIBLE);
                profile_bool = true;
            }else if (requestCode == GALLERY_REQUEST_CODE_PROFILE){
                Uri selectImageUri = data.getData();
                imageViewProfile.setImageURI(selectImageUri);
                imageButtonCancelProfile.setVisibility(View.VISIBLE);
                profile_bool = true;
            }

        }

    }

    //    for choose doctor
    private void initSpinnerSelect() {
        spinnerChooseDoctor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ModelRetrieveDoctorName retrieveDoctorName = (ModelRetrieveDoctorName) parent.getItemAtPosition(position);

                _doctorId = retrieveDoctorName.getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

//    convert image to string
    private String imageToString(){
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//        get image from image view..................................................................
    bitmap = ((BitmapDrawable)imageViewDocument.getDrawable()).getBitmap();
    bitmap.compress(Bitmap.CompressFormat.JPEG,50,byteArrayOutputStream);
    byte[] imageByte = byteArrayOutputStream.toByteArray();

    return Base64.encodeToString(imageByte,Base64.DEFAULT);
}

    private String imageToString_profile(){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//        get image from image view..................................................................
        bitmap_profile = ((BitmapDrawable)imageViewProfile.getDrawable()).getBitmap();
        bitmap_profile.compress(Bitmap.CompressFormat.JPEG,50,byteArrayOutputStream);
        byte[] imageByte = byteArrayOutputStream.toByteArray();

        return Base64.encodeToString(imageByte,Base64.DEFAULT);
    }
}