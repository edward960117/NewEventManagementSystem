package com.example.edward.neweventmanagementsystem;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.edward.neweventmanagementsystem.Model.StaffInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class NewAddedStaff extends AppCompatActivity {

    //private DatabaseReference mDatabaseReference;
    EditText mStaffId, mAddNewSatffText;
    Button btnAddStaff;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_added_staff);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        //Init Firebase
        FirebaseDatabase firebaseDatabase;
        final DatabaseReference table_user = FirebaseDatabase.getInstance().getReference().child("StaffInfo");

        //        //Init Firebase
//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        final DatabaseReference table_user = database.getReference("Organizer");

        btnAddStaff = findViewById(R.id.btnAddSatff);
        mStaffId = findViewById(R.id.staffIdText);
        mAddNewSatffText = findViewById(R.id.txtAddNewSatffText);

        btnAddStaff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog mDialog = new ProgressDialog(NewAddedStaff.this);
                mDialog.setMessage("Please waiting...");
                mDialog.show();

                table_user.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        //Check if already user phone
                        if(dataSnapshot.child(mStaffId.getText().toString()).exists()){
                            mDialog.dismiss();
                            Toast.makeText(NewAddedStaff.this, "Staff ID already register", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            if (mStaffId.length()== 5) {

                                mDialog.dismiss();
                                StaffInfo organizer = new StaffInfo(mStaffId.getText().toString(), mAddNewSatffText.getText().toString());
                                table_user.child(mStaffId.getText().toString()).setValue(organizer);

                                Toast.makeText(NewAddedStaff.this, "New Staff Added successfully!", Toast.LENGTH_SHORT).show();
                                Intent intent =  new Intent(NewAddedStaff.this, StaffAttendance.class);
                                startActivity(intent);

                            }else{
                                mDialog.dismiss();
                                Toast.makeText(NewAddedStaff.this, "Please enter 5 digit for staff id", Toast.LENGTH_SHORT).show();
                            }
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });

    }
}
