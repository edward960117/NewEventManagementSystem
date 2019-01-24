package com.example.edward.neweventmanagementsystem;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.nfc.Tag;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.edward.neweventmanagementsystem.Model.AttendanceInfo;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static android.widget.Toast.LENGTH_SHORT;

public class StaffAttendanceCheckIn extends AppCompatActivity  {

    private TextView CheckInTime, CheckInDate, CheckInName, EventId;
    private Spinner EventName;
    private Button btnCheckIn;
    RecyclerView recycle_menu;
    private static final int REQUEST_LOCATION = 1;
    TextView CurrentLocation;
    LocationManager locationManager;
    String latitude,longitude;
    MapView map1;
    private GoogleMap map;
    private DatabaseReference mDatabaseReference;
    RecyclerView.LayoutManager layoutManager;
    Map<String,String> eventIds = new HashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_attendance_check_in);

        EventName = (Spinner) findViewById(R.id.eventSpinner);
        CheckInDate = (TextView) findViewById(R.id.txtCurrentDate);
        CheckInTime = (TextView) findViewById(R.id.txtCurrentTime);
        CheckInName = (TextView) findViewById(R.id.txtDisplayName);

        map1 = (MapView) findViewById(R.id.mapview);
        btnCheckIn = (Button) findViewById(R.id.btnCheckIn);

        map1.onCreate(savedInstanceState);

//        recycle_menu = (RecyclerView) findViewById(R.id.recycle_menu);
//        recycle_menu.setHasFixedSize(true);
//        layoutManager = new LinearLayoutManager(this);

//        recycle_menu.setLayoutManager(layoutManager);

        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("AttendanceRecord");
        System.out.println("Test mDatabase: "+mDatabaseReference);


        //Display the info in the spinner
        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference myRef = database.getReference("EventName");

//        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference uidRef = rootRef.child("ListOfEvent1"); //.child(uid);
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> eventList = new ArrayList<String>();
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    String registerEventName = ds.child("registerEventName").getValue(String.class);
                    eventList.add(registerEventName);
                    eventIds.put(ds.child("registerEventId").getValue(String.class),ds.child("registerEventName").getValue(String.class));
                    System.out.println("Test Script: "+eventIds);
                }
                Spinner spinner = (Spinner) findViewById(R.id.eventSpinner);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(StaffAttendanceCheckIn.this, android.R.layout.simple_spinner_item, eventList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);

                //holder.txtRegisterEventStartDate.setText(model.getRegisterEventStartDate());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Log.d(Tag, databaseError.getMessage()); //Don't ignore errors!
            }
        };
        uidRef.addListenerForSingleValueEvent(valueEventListener);


        SimpleDateFormat date = new SimpleDateFormat("EEEE dd MMM yyyy", Locale.ENGLISH);
        String currentDate = date.format(new Date());

        SimpleDateFormat time = new SimpleDateFormat("HH:mm:ss");
        String currentTime = time.format(new Date());

        CheckInDate.setText(currentDate);
        CheckInTime.setText(currentTime);

        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
        CheckInName.setText(currentFirebaseUser.getDisplayName());

        /**
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("EventName");

        System.out.println("Test Script: " + myRef); */

        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        CurrentLocation = (TextView)findViewById(R.id.location);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();

        } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            getLocation();
        }


        btnCheckIn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final ProgressDialog mDialog = new ProgressDialog(StaffAttendanceCheckIn.this);

                mDialog.setMessage("Please waiting...");
                mDialog.show();

                mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                        String selected = EventName.getSelectedItem().toString();
                        String id = null;
                        for(Map.Entry<String,String> entry:eventIds.entrySet()){
                            if(entry.getValue().equals(selected)){
                                id = entry.getKey();
                            }
                        }
                        if(id != null) {
                            if (dataSnapshot.child(id).child(currentFirebaseUser.getUid()).exists()) {

                                mDialog.dismiss();
                                Toast.makeText(StaffAttendanceCheckIn.this, "The following account already check in on today for this event!", Toast.LENGTH_SHORT).show();
                            } else {
                                AttendanceInfo attendanceInfo = new AttendanceInfo(EventName.getSelectedItem().toString().trim(),
                                        CheckInTime.getText().toString().trim(),
                                        CheckInDate.getText().toString().trim(),
                                        CurrentLocation.getText().toString().trim(),
                                        CheckInName.getText().toString().trim(),
                                        null);

                                mDatabaseReference.child(id).child(currentFirebaseUser.getUid()).setValue(attendanceInfo);

                                Toast.makeText(getApplicationContext(), "User " + currentFirebaseUser.getDisplayName() + " check in successfully on " + CheckInDate.getText().toString(), LENGTH_SHORT).show();
                                Intent intent = new Intent(StaffAttendanceCheckIn.this, com.example.edward.neweventmanagementsystem.StaffAttendance.class);
                                startActivity(intent);
                            }
                        }
                        else
                            System.out.println("Event does not exist!");
                    }

                    @Override

                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

        });
    }

//    @Override
//    public void onClick(View view) {
//
//    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(com.example.edward.neweventmanagementsystem.StaffAttendanceCheckIn.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (com.example.edward.neweventmanagementsystem.StaffAttendanceCheckIn.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(com.example.edward.neweventmanagementsystem.StaffAttendanceCheckIn.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        } else {
            final Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            Location location1 = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            Location location2 = locationManager.getLastKnownLocation(LocationManager. PASSIVE_PROVIDER);

            if (location != null) {
                double latti = location.getLatitude();
                double longi = location.getLongitude();

                latitude = String.valueOf(latti);
                longitude = String.valueOf(longi);

                CurrentLocation.setText("Lat = " + latitude + "\n" + "Lon = " + longitude);
//                CurrentLocation.setText("latitude"+latitude);
//                CurrentLocation.setText("longitude"+longitude);

                final float ZOOM_MAP = 17.0f;
                final LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                map1.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(GoogleMap googleMap) {

                        CameraUpdate myLocation = CameraUpdateFactory.newLatLngZoom(latLng, ZOOM_MAP);
                        googleMap.animateCamera(myLocation);
                        googleMap.getUiSettings().setAllGesturesEnabled(false);
                        googleMap.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude())).title("It's Me!"));
                        googleMap.addCircle(new CircleOptions()
                                .center(new LatLng(location.getLatitude(),location.getLongitude()))
                                .radius(100)
                                .strokeWidth(0f)
                                .fillColor(0x880000FF));
                    }
                });



//                CameraUpdate myLocation = CameraUpdateFactory.newLatLngZoom(latLng, ZOOM_MAP);
//                map.animateCamera(myLocation);


//                CameraUpdate myLocation = CameraUpdateFactory.newLatLngZoom(latLng, ZOOM_MAP);
//                System.out.println("Test Script" + myLocation);
//                map.animateCamera(myLocation);


            } else  if (location1 != null) {
                double latti = location1.getLatitude();
                double longi = location1.getLongitude();
                latitude = String.valueOf(latti);
                longitude = String.valueOf(longi);
//                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
//                System.out.println("Test Script: " +latLng);

                CurrentLocation.setText("Lat = " + latitude + "\n" + "Lon = " + longitude);

            } else  if (location2 != null) {
                double latti = location2.getLatitude();
                double longi = location2.getLongitude();
                latitude = String.valueOf(latti);
                longitude = String.valueOf(longi);

                CurrentLocation.setText("Lat = " + latitude + "\n" + "Lon = " + longitude);

            }else{

                Toast.makeText(this,"Unble to Trace your location",Toast.LENGTH_SHORT).show();

            }
        }
    }

    protected void buildAlertMessageNoGps() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Please Turn ON your GPS Connection")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        map1.onDestroy();
    }

    @Override
    public void onLowMemory(){
        super.onLowMemory();
        map1.onLowMemory();
    }

    @Override
    protected void onPause(){
        super.onPause();
        map1.onPause();
    }
    @Override
    protected void onResume(){
        super.onResume();
        map1.onResume();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        map1.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, StaffAttendance.class);
        startActivity(intent);
    }
}
