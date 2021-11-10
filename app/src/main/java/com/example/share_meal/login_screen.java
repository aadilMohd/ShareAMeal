package com.example.share_meal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Pattern;

public class login_screen extends AppCompatActivity{

    private FirebaseAuth mAuth;
    private TextInputEditText email_txt,password_txt;
    private ProgressBar p_Bar;

    //SHAREDPREFERANCES
    public static final String PREFS_NAME = "loginentry";
    SharedPreferences setting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login_screen);

        //ONE-TIME LOGIC
        setting=getSharedPreferences(login_screen.PREFS_NAME,MODE_PRIVATE);
        if(setting.getBoolean("hasloggedIN",true)){
            SharedPreferences.Editor edit = setting.edit();
            edit.putBoolean("hasloggedIN",false);
            edit.apply();
        }
        else{
            startActivity(new Intent(login_screen.this,MainActivity.class));
            finish();
        }

        //Hooks
        mAuth =  FirebaseAuth.getInstance();

        email_txt = (TextInputEditText) findViewById(R.id.emailidtxt);
        password_txt = (TextInputEditText) findViewById(R.id.passwordtxt);

        p_Bar = (ProgressBar) findViewById(R.id.p_bar);

        findViewById(R.id.signup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(login_screen.this,signup_screen.class));
                finish();
            }
        });

        findViewById(R.id.forgotpass).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forgot();
            }
        });

        findViewById(R.id.signin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    private void forgot() {
        if(!email_txt.getText().toString().trim().isEmpty()){
            p_Bar.setVisibility(View.VISIBLE);

            mAuth.sendPasswordResetEmail(email_txt.toString().trim())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> passreset_task) {
                            if(passreset_task.isSuccessful()){
                                Toast.makeText(login_screen.this,"Check your Email Id for Forgot Password Link",Toast.LENGTH_LONG).show();
                            }
                            else{
                                Toast.makeText(login_screen.this, "Network Issue", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    private boolean validateinfo() {
        int numValidateCount = 0;

        if(email_txt.getText().toString().trim().isEmpty()){
            email_txt.setError("Email id required");
            email_txt.requestFocus();
            numValidateCount++;
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(email_txt.getText().toString().trim()).matches()){
            email_txt.setError("Please provide valid Email Address");
            email_txt.requestFocus();
            numValidateCount++;
        }

        if(password_txt.getText().toString().trim().isEmpty()){
            password_txt.setError("Password is required");
            password_txt.requestFocus();
            numValidateCount++;
        }

        else if(password_txt.getText().toString().trim().length()<6){
            password_txt.setError("Min. 6 character is required");
            password_txt.requestFocus();
            numValidateCount++;
        }

        return numValidateCount==0;
    }
    
    private void login() {
        if(validateinfo()){
            p_Bar.setVisibility(View.VISIBLE);

            mAuth.signInWithEmailAndPassword(
                    email_txt.getText().toString().trim(),password_txt.getText().toString().trim())
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> signin_task) {
                            if(signin_task.isSuccessful()){
                                startActivity(new Intent(login_screen.this,main_screen.class));
                                finish();
                                Toast.makeText(login_screen.this, "WELCOME !", Toast.LENGTH_LONG).show();
                            }
                            else{
                                Toast.makeText(login_screen.this, "INCORRECT EMAIL OR PASSWORD ", Toast.LENGTH_SHORT).show();
                            }
                            p_Bar.setVisibility(View.GONE);
                        }
                    });
        }
    }
}