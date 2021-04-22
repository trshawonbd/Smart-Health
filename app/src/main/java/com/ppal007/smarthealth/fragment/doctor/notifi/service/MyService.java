package com.ppal007.smarthealth.fragment.doctor.notifi.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.gson.Gson;
import com.ppal007.smarthealth.R;
import com.ppal007.smarthealth.activity.SplashActivity;
import com.ppal007.smarthealth.adapter.sharedPref.AdapterSharedPref;
import com.ppal007.smarthealth.model.ModelAlert;
import com.ppal007.smarthealth.model.ModelRegisterAsPatient;
import com.ppal007.smarthealth.model.PatientModel;
import com.ppal007.smarthealth.retrofit.ApiClient;
import com.ppal007.smarthealth.retrofit.ApiInterface;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ppal007.smarthealth.fragment.doctor.notifi.App.CHANNEL_ID;

public class MyService extends Service {
    private static final String TAG = "MyService";

    public static final long INTERVAL = 1000 * 10;
    private Handler mHandler = new Handler();
    private Timer mTimer = null;
    private AdapterSharedPref sharedPref;
    private NotificationManagerCompat notificationManager;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        //Toast.makeText(this, "service start", Toast.LENGTH_SHORT).show();
        sharedPref = new AdapterSharedPref(getApplicationContext());
        notificationManager = NotificationManagerCompat.from(MyService.this);
    }

//    for recall send notification
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //get service start from Home activity extra
        String service_string = intent.getStringExtra("service_extra");

        if (service_string.equals("view_now")) {
            if (mTimer != null) {
                mTimer.cancel();
            } else {
                mTimer = new Timer();
                mTimer.scheduleAtFixedRate(new TimeDisplayTimerTask(), 0, INTERVAL);
            }
        }

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mTimer.cancel();
    }

//   check patient problem (call send notification)
    private class TimeDisplayTimerTask extends TimerTask {
        @Override
        public void run() {
            // run on another thread
            mHandler.post(() -> {

                //get alert
                ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
                Call<ModelAlert> call = apiInterface.get_alert(sharedPref.getUserId());
                call.enqueue(new Callback<ModelAlert>() {
                    @Override
                    public void onResponse(Call<ModelAlert> call, Response<ModelAlert> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            ModelAlert model = response.body();

                            String _patient_tbl_id = model.getPatientTblId();
                            String _bpUp           = model.getBpUp();
                            String _bpDown         = model.getBpDown();
                            String _spo2           = model.getSpo2();
                            String _bfr            = model.getGlBfr();
                            String _aftr           = model.getGlAftr();
                            String _tmpr           = model.getTmpr();

                            //check json item is null ######################
                            if (_bpUp == null){
                                _bpUp = "ok";

                            }else if (_bpDown == null){
                                _bpDown = "ok";

                            }else if (_spo2 == null){
                                _spo2 = "ok";

                            }else if (_bfr == null){
                                _bfr = "ok";

                            }else if (_aftr == null){
                                _aftr = "ok";

                            }else if (_tmpr == null){
                                _tmpr = "ok";

                            }

                            //send notification
                            init_notification(_patient_tbl_id,_bpUp,_bpDown,_spo2,_bfr,_aftr,_tmpr);


                        } else {
                            //Toast.makeText(MyService.this, "something wrong to get alert ids!", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "onResponse: "+"No Alert!");
                        }
                    }

                    @Override
                    public void onFailure(Call<ModelAlert> call, Throwable t) {
                        Log.d(TAG, "onFailure: " + t.toString());
                    }
                });
            });
        }
    }




    private void init_notification(String patientTblId, String bpUp, String bpDown, String spo2, String glBfr, String glAftr, String tmpr) {


        //init pending intent
        Intent intent = new Intent(MyService.this, SplashActivity.class);
        intent.putExtra("extra_notification_click_view", true);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(MyService.this, 0, intent, PendingIntent.FLAG_ONE_SHOT);


        //notification
        Notification builder = new NotificationCompat.Builder(MyService.this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_baseline_notification_add_24)
                .setContentTitle("New Notification")
                .setContentText(
                        "Sir your patient needs you. Id : "+patientTblId+"\n"+
                        "bp up : "+bpUp+"\n"+
                        "bp down : "+bpDown+"\n"+
                        "spo2 : "+spo2+"\n"+
                        "gl befour : "+glBfr+"\n"+
                        "gl after : "+glAftr+"\n"+
                        "temperature : "+tmpr
                )
                .setAutoCancel(true)
                .addAction(R.drawable.ic_view, "view", pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();

        notificationManager.notify(1, builder);

        //update db for stop notification double time
        update_db(patientTblId);
    }


    private void update_db(String patientTblId) {
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<ModelAlert> call = apiInterface.update_db_stop_notification_double_time(patientTblId);
        call.enqueue(new Callback<ModelAlert>() {
            @Override
            public void onResponse(Call<ModelAlert> call, Response<ModelAlert> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ModelAlert model = response.body();
                    Toast.makeText(MyService.this, "" + model.getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MyService.this, "something wrong to update db", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ModelAlert> call, Throwable t) {
                Toast.makeText(MyService.this, "" + t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}
