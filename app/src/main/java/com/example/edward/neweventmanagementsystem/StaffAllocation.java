package com.example.edward.neweventmanagementsystem;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class StaffAllocation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_allocation);
    }
    public void OpenStafffManagement (View view) {
        Intent intent = new Intent(this, StaffManagement.class);
        startActivity(intent);
    }
}
