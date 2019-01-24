package com.example.edward.neweventmanagementsystem;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class StaffAttendance extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_attendance);
    }


    public void OpenStaffCheckIn (View view){
        Intent intent = new Intent(this, StaffAttendanceCheckIn.class);
        startActivity(intent);
    }

    public void OpenStaffAttendanceRecordTable (View view){
        Intent intent = new Intent(this, StaffAttendanceRecordTable.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
