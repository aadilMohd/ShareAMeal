package com.example.share_meal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;

public class splash_screen extends AppCompatActivity {

    //Variables
    Animation rtol;
    ImageView app_logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.splash_screen);



        //AnimationDeclration
        rtol = AnimationUtils.loadAnimation(this,R.anim.right_left);

        //Hooks
        app_logo = findViewById(R.id.app_logo);

        app_logo.setAnimation(rtol);

        //Screen_Delay Handler

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(FirebaseAuth.getInstance().getCurrentUser()==null){
                    Intent i = new Intent(splash_screen.this,login_screen.class);
                    startActivity(i);
                    finish();
                }
                else{

                    Intent i = new Intent(splash_screen.this,MainActivity.class);
                    startActivity(i);
                    finish();

                }

            }
        },3500);
    }
}