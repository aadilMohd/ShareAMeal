package com.example.share_meal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class signup_screen extends AppCompatActivity{

    //Variables
    private FirebaseAuth mAuth;

    private TextInputLayout fullname_lay,phone_lay,email_lay,password_lay,confpassword_lay;
    private TextInputEditText fullname_txt,phone_txt,email_txt,password_txt,confpassword_txt;

    private ProgressBar p_bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_signup_screen);

        //Declaration
        mAuth = FirebaseAuth.getInstance();

        fullname_txt = (TextInputEditText) findViewById(R.id.fullnametxt);
        phone_txt = (TextInputEditText) findViewById(R.id.phonetxt);
        email_txt = (TextInputEditText) findViewById(R.id.emailidtxt);
        password_txt = (TextInputEditText) findViewById(R.id.passwordtxt);
        confpassword_txt = (TextInputEditText) findViewById(R.id.conf_passwordtxt);

        p_bar = (ProgressBar) findViewById(R.id.p_bar);

        findViewById(R.id.signup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        findViewById(R.id.signin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(signup_screen.this,login_screen.class));
                finish();
            }
        });
    }

    private boolean validateFields()
    {
        int numValidateCount=0;

        if(fullname_txt.getText().toString().trim().isEmpty()){
            fullname_txt.setError("Full name Required");
            fullname_txt.requestFocus();
            numValidateCount++;
        }
        if(phone_txt.getText().toString().trim().isEmpty()){
            phone_txt.setError("Phone No. Required");
            phone_txt.requestFocus();
            numValidateCount++;
        }
        else if(phone_txt.getText().toString().trim().length() != 10 ){
            phone_txt.setError("Please provide valid Phone No.");
            phone_txt.requestFocus();
            numValidateCount++;
        }
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
        else if(! confpassword_txt.getText().toString().trim().equals(password_txt.getText().toString().trim())){
            confpassword_txt.setError("Password Don't Match");
            confpassword_txt.requestFocus();
            numValidateCount++;
        }

        return numValidateCount==0;
    }

    private void signup(){
        if(validateFields()) {
            p_bar.setVisibility(View.VISIBLE);

            mAuth.createUserWithEmailAndPassword(
                    email_txt.getText().toString().trim(),password_txt.getText().toString().trim())
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> data_task) {
                            if(data_task.isSuccessful()){
                                userdata Objuserdata = new userdata(
                                        fullname_txt.getText().toString().trim(),
                                        phone_txt.getText().toString().trim(),
                                        email_txt.getText().toString().trim()
                                );

                                FirebaseDatabase.getInstance().getReference("Data")
                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .setValue(Objuserdata).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> saving_task) {
                                        if(saving_task.isSuccessful()){
                                            Toast.makeText(signup_screen.this, "Account Registered!", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(signup_screen.this,login_screen.class));
                                            finish();
                                        }
                                        else{
                                            Toast.makeText(signup_screen.this,"Failed to Create Account! TRY AGAIN", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                            else{
                                Toast.makeText(signup_screen.this, "User Already Exist", Toast.LENGTH_SHORT).show();
                            }
                            p_bar.setVisibility(View.GONE);
                        }
                    });
        }
    }
}