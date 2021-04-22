package com.ppal007.smarthealth.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ppal007.smarthealth.R;
import com.ppal007.smarthealth.fragment.doctor.notifi.service.MyService;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // hide title bar
        WindowManager.LayoutParams attrs = getWindow().getAttributes();
        attrs.flags = WindowManager.LayoutParams.FLAG_FULLSCREEN;
        getWindow().setAttributes(attrs);
        setContentView(R.layout.activity_splash);

        //if doctor click notification
        if (getIntent().hasExtra("extra_notification_click_view")){
            //Toast.makeText(this, "data come", Toast.LENGTH_SHORT).show();
            stopService(new Intent(SplashActivity.this,MyService.class));

        }



        //find xml
        ImageView imageView = findViewById(R.id.imageView);
        TextView textView = findViewById(R.id.textView);

        //set image
        Glide.with(this).load(R.drawable.logo_150_150).circleCrop().into(imageView);

        //add animation
        Animation topAnimation = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        Animation bottomAnimation = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);
        imageView.setAnimation(topAnimation);
        textView.setAnimation(bottomAnimation);

        //finish splash
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        },3000);


    }
}