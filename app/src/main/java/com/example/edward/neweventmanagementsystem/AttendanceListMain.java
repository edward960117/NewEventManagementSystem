package com.example.edward.neweventmanagementsystem;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.edward.neweventmanagementsystem.Model.AttendanceInfo;
import com.example.edward.neweventmanagementsystem.ViewHolder.MenuViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AttendanceListMain extends AppCompatActivity {
    RecyclerView recycle_menu;
    RecyclerView.LayoutManager layoutManager;
    private DatabaseReference mDatabaseReference;
    FirebaseRecyclerAdapter<AttendanceInfo, MenuViewHolder> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_list_main);

        recycle_menu = (RecyclerView) findViewById(R.id.recycle_menu);
        recycle_menu.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recycle_menu.setLayoutManager(layoutManager);

        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("AttendanceRecord");

        Intent intent = getIntent();
        final String attendance=  intent.getStringExtra("Attendance");

        System.out.println("Test in StaffAttendanceRecordTable: "+attendance);
        loadMenu();

        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(attendance).exists()){
                    System.out.println("Test inside the datasnapshot "+attendance);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void loadMenu() {

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, StaffAttendanceRecordTable.class);
        startActivity(intent);
    }

//    public void loadMenu(){
//        Query query = mDatabaseReference.orderByKey();
//        FirebaseRecyclerOptions firebaseRecyclerOptions = new FirebaseRecyclerOptions.Builder<AttendanceInfo>().setQuery(query, AttendanceInfo.class).build();
//        adapter = new FirebaseRecyclerAdapter<AttendanceInfo, MenuViewHolder>(firebaseRecyclerOptions) {
//            @NonNull
//            @Override
//            public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
//                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_list,viewGroup,false);
//                return new MenuViewHolder(view);
//            }
//
//            @Override
//            protected void onBindViewHolder(@NonNull MenuViewHolder holder, int position, @NonNull AttendanceInfo model) {
//
//                holder.checkInName.setText(model.getCheckInName());
//
//            }
//
//        };
//        recycle_menu.setAdapter(adapter);
//    }
}
