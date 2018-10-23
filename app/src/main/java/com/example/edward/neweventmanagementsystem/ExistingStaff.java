package com.example.edward.neweventmanagementsystem;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ExistingStaff extends AppCompatActivity {
    Spinner spinner;
    ArrayAdapter<String>arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_existing_staff);

        spinner = findViewById(R.id.spinner1);
//        arrayAdapter=new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, name);
//
//        spinner.setAdapter(arrayAdapter);
//        DatabaseReference database = FirebaseDatabase.getInstance().getReference();

                //FirebaseDatabase.getInstance().getReference().child("StaffInfo");

    }
}
