package com.example.edward.neweventmanagementsystem;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class StaffManagement extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_management);


    }

    public void OpenAddedSatff (View view) {
        Intent intent = new Intent(this, NewAddedStaff.class);
        startActivity(intent);
    }

    public  void OpenExistingSatff (View view){
        Intent intent = new Intent(this, ExistingStaff.class);
        startActivity(intent);
    }
}
