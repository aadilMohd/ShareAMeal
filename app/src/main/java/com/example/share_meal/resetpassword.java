package com.example.share_meal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

public class resetpassword extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    /*--------------VARIABLES--------------*/
    FirebaseAuth mAuth;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    TextInputEditText email_txt;
    MaterialButton sentmail_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_resetpassword);

        /*----------HOOKS----------*/
        mAuth =  FirebaseAuth.getInstance();
        email_txt = findViewById(R.id.resetpassword_txt);
        sentmail_btn = findViewById(R.id.resetpassword_sentmailBtn);
        drawerLayout = findViewById(R.id.Restpass_drawerLayout);
        navigationView = findViewById(R.id.Resetpassword_NavView);
        toolbar = findViewById(R.id.ActionBar);

        /*----------ToolBar----------*/
        setSupportActionBar(toolbar);

        /*----------Navigation View----------*/
        navigationView.bringToFront();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,drawerLayout,toolbar,R.string.nav_drawer_open,R.string.nav_drawer_close);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        sentmail_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!email_txt.getText().toString().trim().isEmpty()){

                    mAuth.sendPasswordResetEmail(email_txt.getText().toString().trim())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> resetpassword_task) {
                                    if(resetpassword_task.isSuccessful()){
                                        Toast.makeText(getApplicationContext(), "Check Your Mail for Reset Password Link", Toast.LENGTH_SHORT).show();
                                    }
                                    else{
                                        Toast.makeText(getApplicationContext(), "Check Your Internet Connection", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
                else{
                    email_txt.setError("Email id Required");
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else{
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_btnHome:
                startActivity(new Intent(resetpassword.this,MainActivity.class));
                finish();
                break;
            case R.id.nav_btnAbout:
                startActivity(new Intent(resetpassword.this,about_screen.class));
                break;
            case R.id.nav_btnSupport:
                startActivity(new Intent(resetpassword.this,support_screen.class));
                finish();
                break;
            case R.id.nav_ProfbtnForgotpass:
                break;
            case R.id.nav_ProfbtnLogout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(resetpassword.this,login_screen.class));
                break;
        }
        return true;
    }
}