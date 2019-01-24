package com.example.edward.neweventmanagementsystem;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.edward.neweventmanagementsystem.Model.AttendanceInfo;
import com.example.edward.neweventmanagementsystem.Model.EventInfo;
import com.example.edward.neweventmanagementsystem.ViewHolder.DetailAdapter;
import com.example.edward.neweventmanagementsystem.ViewHolder.MenuViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

public class StaffAttendanceRecordTable extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference eventInfo;
    FloatingActionButton search_item;
    RecyclerView recycle_menu;
    RecyclerView recycle_detail;
    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter <EventInfo, MenuViewHolder> adapter;
    private DatabaseReference mDatabaseReference, mDatabaseReference1;
    private TextView EventName, attendance;
    private View viewTable;
    private Dialog dialogTable;
    private ArrayList<AttendanceInfo> attendanceInfoList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_attendance_record_table);

//        final String attendance = ((TextView) findViewById(R.id.RegisterEventName)).getText().toString();
        attendance = (TextView) findViewById(R.id.RegisterEventName);
//        RegisterEventName = viewTable.findViewById(R.id.RegisterEventName);
//        final String location = ((TextView) findViewById(R.id.RegisterEventLocation)).getText().toString();

        recycle_menu = (RecyclerView) findViewById(R.id.recycle_menu);
        recycle_menu.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recycle_menu.setLayoutManager(layoutManager);

        viewTable = getLayoutInflater().inflate(R.layout.activity_attendance_list_detailed,null);
        recycle_detail = viewTable.findViewById(R.id.detailed_recycle_menu);
        recycle_detail.setLayoutManager(new LinearLayoutManager(this));
        recycle_detail.setHasFixedSize(true);
        dialogTable = new Dialog(this,R.style.Theme_AppCompat_Dialog);
        dialogTable.setContentView(viewTable);
        dialogTable.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                attendanceInfoList.clear();
            }
        });

        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("AttendanceRecord");

        System.out.println("Test mDatabaseReference: "+mDatabaseReference);

        recycle_menu.setLayoutManager(layoutManager);

        search_item = (FloatingActionButton) findViewById(R.id.search_item);

        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
        eventInfo = FirebaseDatabase.getInstance().getReference().child("ListOfEvent").child(currentFirebaseUser.getUid());

        loadMenu();


        recycle_menu.addOnItemTouchListener(
                new RecyclerItemClickListener(getBaseContext(), recycle_menu ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(final View view, int position) {

                        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                ArrayList<String> eventNames = new ArrayList<>();

                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                                    eventNames.add(snapshot.getKey());

                                    //Now you have all Event Names in List
                                    // you can show them in a Custom Alert Dialog
                                    //Or pass it to next Activity.

                                    final String EventId = ((TextView)view.findViewById(R.id.RegisterEventId)).getText().toString();
                                    System.out.println(EventId);
                                    if(dataSnapshot.child(EventId).exists()){
//

                                        mDatabaseReference1 = FirebaseDatabase.getInstance().getReference().child("AttendanceRecord").child(EventId);
                                        mDatabaseReference1.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                if(dataSnapshot.getValue()!=null){
                                                    addDetailList((Map<String,Object>)dataSnapshot.getValue());
                                                    DetailAdapter detailAdapter = new DetailAdapter(getBaseContext(),attendanceInfoList);
                                                    recycle_detail.setAdapter(detailAdapter);
                                                    dialogTable.show();

                                                }
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });

//                                        Intent intent =  new Intent(StaffAttendanceRecordTable.this, com.example.edward.neweventmanagementsystem.AttendanceListMain.class);
//                                        intent.putExtra("Attendance", EventName);
//                                        startActivity(intent);

                                    }else{
                                        Toast.makeText(StaffAttendanceRecordTable.this, "No Result", Toast.LENGTH_SHORT).show();
                                    }

                                }


                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });





                    }

                    @Override public void onLongItemClick(final View view, int position) {



                    }
                })
        );



        search_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent search = new Intent(StaffAttendanceRecordTable.this, Find_Event.class);
                startActivity(search);
                System.out.println("Testing search Item");
            }
        });
    }

    public void loadMenu(){
        Query query = eventInfo.orderByKey();
        FirebaseRecyclerOptions firebaseRecyclerOptions = new FirebaseRecyclerOptions.Builder<EventInfo>().setQuery(query, EventInfo.class).build();
        adapter = new FirebaseRecyclerAdapter<EventInfo, MenuViewHolder>(firebaseRecyclerOptions) {
            @NonNull
            @Override
            public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_list,viewGroup,false);
                return new MenuViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull MenuViewHolder holder, int position, @NonNull EventInfo model) {

                holder.txtRegisterEventId.setText(model.getRegisterEventId());
                holder.txtRegisterEventStartDate.setText(model.getRegisterEventStartDate());
                holder.txtRegisterEventName.setText(model.getRegisterEventName());
                holder.txtRegisterContactNumber.setText(model.getRegisterContactNumber());
                holder.txtRegisterEventRadiogroup.setText(model.getRegisterEventRadiogroup());
                holder.txtRegisterEventLocation.setText(model.getRegisterEventLocation());


                holder.txtEventPrice.setText(model.getEventPrice());
                holder.txtEventCapacity.setText(model.getEventCapacity());

                holder.fileName.setText(model.getFileName());

                //Picasso.with(getBaseContext()).load(model.getImageToUpload()).into(viewHolder.imageView);
                //Glide.with(getBaseContext()).load(model.getImageToUpload()).into(viewHolder.imageView);

                Glide.with(getBaseContext()).load(model.getImageToUpload()
                ).into(holder.imageView);
                System.out.println(model.getRegisterEventName());
                System.out.println(model.getImageToUpload());
            }

        };
        recycle_menu.setAdapter(adapter);
    }

    private void addDetailList(Map<String,Object> map){
        boolean exist = false;
        for(Map.Entry<String,Object> entry : map.entrySet()){

            Map tempmap = (Map)entry.getValue();
            for(AttendanceInfo attendanceInfo : attendanceInfoList){
                if(attendanceInfo.getCheckInName().equals((String)tempmap.get("checkInName"))){
                    exist = true;
                }
            }
            if(!exist){
                AttendanceInfo temp = new AttendanceInfo((String)tempmap.get("eventName"),(String)tempmap.get("checkInTime"),(String)tempmap.get("checkInDate"),(String)tempmap.get("currentLocation"),(String)tempmap.get("checkInName"),null);

                attendanceInfoList.add(temp);
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }


}
