package com.example.share_meal;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.auth.User;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback {
    Location currentLocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST_CODE = 101;
    private double latitude;
    private double longitude;
    private ConstraintLayout locationb;
    private ConstraintLayout pickupb;
    private ConstraintLayout detailsb;
    PopupWindow popupWindow;

    private ConstraintLayout refresh;

    private DatabaseReference mDatabase;

    private FirebaseAuth mAuth;
    FirebaseUser user;
    String uid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        user = FirebaseAuth.getInstance().getCurrentUser();


        locationb = findViewById(R.id.locationbutton);
        pickupb = findViewById(R.id.pickupbutton);
        detailsb = findViewById(R.id.detailsbutton);
        refresh = findViewById(R.id.refresh);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fetchLocation(new LatLng(0,0));

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        detailsb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOUt();
            }
        });

        pickupb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                locationb.setVisibility(view.INVISIBLE);
                pickupb.setVisibility(view.INVISIBLE);
                detailsb.setVisibility(view.INVISIBLE);

                LayoutInflater inflater = (LayoutInflater)
                        getSystemService(LAYOUT_INFLATER_SERVICE);
                View popupView = inflater.inflate(R.layout.popup, null);

                int width = ConstraintLayout.LayoutParams.MATCH_PARENT;
                int height = ConstraintLayout.LayoutParams.WRAP_CONTENT;
                boolean focusable = false; // lets taps outside the popup also dismiss it
                popupWindow = new PopupWindow(popupView, width, height, focusable);

                popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);

                // creating new popupview
                LayoutInflater inflater1 = (LayoutInflater)
                        getSystemService(LAYOUT_INFLATER_SERVICE);
                View popupView1 = inflater.inflate(R.layout.popup_pickup_2, null);
                PopupWindow popupWindow1 = new PopupWindow(popupView1, width, height, focusable);


                popupWindow.getContentView().findViewById(R.id.cancelbutton).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        locationb.setVisibility(view.VISIBLE);
                        pickupb.setVisibility(view.VISIBLE);
                        detailsb.setVisibility(view.VISIBLE);
                        popupWindow.dismiss();

                    }
                });

                //showing second popop after clicking confirm

                popupWindow.getContentView().findViewById(R.id.confirmbutton).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        Toast.makeText(MainActivity.this, "clicked confrim", Toast.LENGTH_SHORT).show();
                            popupWindow.dismiss();
                            popupWindow1.showAtLocation(view, Gravity.BOTTOM, 0, 0);
                            popupWindow1.getContentView().findViewById(R.id.dismissbutton).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    locationb.setVisibility(view.VISIBLE);
                                    pickupb.setVisibility(view.VISIBLE);
                                    detailsb.setVisibility(view.VISIBLE);
                                    popupWindow1.dismiss();
                                }
                            });
                    }
                });
            }
        });

        locationb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                locationb.setVisibility(view.INVISIBLE);
                pickupb.setVisibility(view.INVISIBLE);
                detailsb.setVisibility(view.INVISIBLE);

                LayoutInflater inflater = (LayoutInflater)
                        getSystemService(LAYOUT_INFLATER_SERVICE);
                View popupView = inflater.inflate(R.layout.popup_location, null);

                int width = ConstraintLayout.LayoutParams.MATCH_PARENT;
                int height = ConstraintLayout.LayoutParams.WRAP_CONTENT;
                boolean focusable = false; // lets taps outside the popup also dismiss it
                popupWindow = new PopupWindow(popupView, width, height, focusable);

               TextView locationText = popupWindow.getContentView().findViewById(R.id.locationt);
               locationText.setText(new GetAddress(latitude,longitude,MainActivity.this).getAddress());

                // creating new popupview
                LayoutInflater inflater1 = (LayoutInflater)
                        getSystemService(LAYOUT_INFLATER_SERVICE);
                View popupView1 = inflater.inflate(R.layout.popup_location2, null);
                PopupWindow popupWindow1 = new PopupWindow(popupView1, width, height, true);

                popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);

                popupWindow.getContentView().findViewById(R.id.changelocationbutton).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        popupWindow.dismiss();
                        popupWindow1.showAtLocation(view, Gravity.BOTTOM, 0, 0);


                        popupWindow1.getContentView().findViewById(R.id.confirmlocationbutton).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                EditText addresstext = popupWindow1.getContentView().findViewById(R.id.newaddress);
                                String newadr = addresstext.getText().toString();



                               LatLng l2=new GetLatlong(MainActivity.this,newadr).getLatLong();

                               if(l2.latitude==0 && l2.longitude==0){
                                   Toast.makeText(MainActivity.this, "Address Invalid, Try Again", Toast.LENGTH_SHORT).show();
                               }
                               else {
                                   fetchLocation(l2);
                               }

                                locationb.setVisibility(view.VISIBLE);
                                pickupb.setVisibility(view.VISIBLE);
                                detailsb.setVisibility(view.VISIBLE);
                                popupWindow1.dismiss();




                            }
                        });

                    }
                });


//
//  Toast.makeText(MainActivity.this, new GetAddress(latitude,longitude,MainActivity.this).getAddress(), Toast.LENGTH_SHORT).show();

            }
        });





        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(MainActivity.this, "Fetching ur location", Toast.LENGTH_SHORT).show();

                fetchLocation(new LatLng(0,0));

            }
        });

    }



    private void fetchLocation( LatLng l) {
        if(l.latitude==0 && l.longitude==0) {
            if (ActivityCompat.checkSelfPermission(
                    this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);

                return;
            }
            Task<Location> task = fusedLocationProviderClient.getLastLocation();
            ((Task) task).addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        currentLocation = location;
                        latitude = currentLocation.getLatitude();
                        longitude = currentLocation.getLongitude();

                        Toast.makeText(getApplicationContext(), currentLocation.getLatitude() + " " + currentLocation.getLongitude(), Toast.LENGTH_SHORT).show();
                        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                        assert supportMapFragment != null;
                        supportMapFragment.getMapAsync(MainActivity.this::onMapReady);
                    }
                }
            });
        }
        else{

            longitude=l.latitude;
            longitude=l.longitude;

            SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            assert supportMapFragment != null;
            supportMapFragment.getMapAsync(MainActivity.this::onMapReady);

        }

        }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Toast.makeText(getApplicationContext(), latitude + " " + longitude, Toast.LENGTH_SHORT).show();

            if(latitude!=0 && longitude!=0) {
                LatLng latLng = new LatLng(latitude, longitude);
                MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("My Location");
                googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 20f));
                googleMap.addMarker(markerOptions);

                userdata Objuserdata = new userdata(
                        String.valueOf(latitude),
                        String.valueOf(longitude)
                );

                        FirebaseDatabase.getInstance().getReference(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .child("Location")
                                .setValue(Objuserdata);
            }
        }


    public void signOUt(){
        FirebaseAuth.getInstance().signOut();

        startActivity(new Intent(MainActivity.this,login_screen.class));
    }
}
