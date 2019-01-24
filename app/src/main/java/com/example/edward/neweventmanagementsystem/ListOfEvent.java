package com.example.edward.neweventmanagementsystem;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
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
import com.example.edward.neweventmanagementsystem.Model.EventInfo;
import com.example.edward.neweventmanagementsystem.ViewHolder.MenuViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ListOfEvent extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference eventInfo, eventInfo1, AttendanceRecord;
    FloatingActionButton search_item;
    RecyclerView recycle_menu;
    RecyclerView.LayoutManager layoutManager;

    private View viewTable;

    private Dialog dialogTable;
    private TextView RegisterEventId,RegisterEventName,EventCapacity,RegisterEventLocation,
            RegisterContactNumber, RegisterEventStartDate,EventPrice ;
    FirebaseRecyclerAdapter <EventInfo, MenuViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_event);

        recycle_menu = (RecyclerView) findViewById(R.id.recycle_menu);
        recycle_menu.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);

        recycle_menu.setLayoutManager(layoutManager);

        search_item = (FloatingActionButton) findViewById(R.id.search_item);

        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
        eventInfo = FirebaseDatabase.getInstance().getReference().child("ListOfEvent").child(currentFirebaseUser.getUid());
        eventInfo1 = FirebaseDatabase.getInstance().getReference().child("ListOfEvent1");
        AttendanceRecord = FirebaseDatabase.getInstance().getReference().child("AttendanceRecord");

        loadMenu();

        viewTable = getLayoutInflater().inflate(R.layout.activity_event_dialog,null);
        RegisterEventId = viewTable.findViewById(R.id.RegisterEventId);
        RegisterEventName = viewTable.findViewById(R.id.RegisterEventName);
        EventCapacity = viewTable.findViewById(R.id.EventCapacity);
        RegisterEventLocation = viewTable.findViewById(R.id.RegisterEventLocation);
        RegisterContactNumber = viewTable.findViewById(R.id.RegisterContactNumber);
        RegisterEventStartDate =  viewTable.findViewById(R.id.RegisterEventStartDate);
        EventPrice =  viewTable.findViewById(R.id.EventPrice);
        dialogTable = new Dialog(this,R.style.full_screen_dialog);
        dialogTable.setContentView(viewTable);

        recycle_menu.addOnItemTouchListener(
                new RecyclerItemClickListener(getBaseContext(), recycle_menu ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {


                        final String id = ((TextView)view.findViewById(R.id.RegisterEventId)).getText().toString();
                        RegisterEventId.setText(id);

                        final String registerEventName = ((TextView)view.findViewById(R.id.RegisterEventName)).getText().toString();
                        RegisterEventName.setText(registerEventName);

                        final String eventCapacity = ((TextView)view.findViewById(R.id.txtEventCapacity)).getText().toString();
                        EventCapacity.setText(eventCapacity);

                        final String registerEventLocation = ((TextView)view.findViewById(R.id.RegisterEventLocation)).getText().toString();
                        RegisterEventLocation.setText(registerEventLocation);
//
                        final String registerContactNumber = ((TextView)view.findViewById(R.id.contact_number)).getText().toString();
                        RegisterContactNumber.setText(registerContactNumber);

                        final String registerEventStartDate = ((TextView)view.findViewById(R.id.RegisterEventStartDate)).getText().toString();
                        RegisterEventStartDate.setText(registerEventStartDate);

                        final String eventPrice = ((TextView)view.findViewById(R.id.txtEventPrice)).getText().toString();
                        EventPrice.setText(eventPrice);
                        dialogTable.show();

                    }

                    @Override public void onLongItemClick(final View view, int position) {
                        final String name = ((TextView)view.findViewById(R.id.RegisterEventName)).getText().toString();
                        System.out.println("Testing the name: "+name);
                        new AlertDialog.Builder(ListOfEvent.this)
                                .setTitle("Alert!")
                                .setMessage("Do you want to delete event "+name+"?")
                                .setNegativeButton(android.R.string.no, null)
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                                    public void onClick(DialogInterface arg0, int arg1) {
//                                        setResult(RESULT_OK, new Intent().putExtra("EXIT", true));
//                                        finish();
                                        final String id = ((TextView)view.findViewById(R.id.RegisterEventId)).getText().toString();
                                        final String filename = ((TextView)view.findViewById(R.id.fileName)).getText().toString();
                                        Toast.makeText(getBaseContext(),"Deleting of event id "+id,Toast.LENGTH_SHORT).show();
                                        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
                                        storageReference.child("profileImageUrl").child(filename).delete();

                                        AttendanceRecord.child(id).removeValue();
                                        eventInfo.child(id).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                                eventInfo1.child(id).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        Toast.makeText(getBaseContext(),"Event id "+id+" has been deleted.",Toast.LENGTH_SHORT).show();
                                                        }
                                                        });

                                            }
                                        });
                                    }

                                }).create().show();


                    }
                })
        );
        search_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent search = new Intent(ListOfEvent.this, Find_Event.class);
                startActivity(search);
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

                Glide.with(getBaseContext()).load(model.getImageToUpload()
                ).into(holder.imageView);
                System.out.println(model.getRegisterEventName());
                System.out.println(model.getImageToUpload());
            }

        };
        recycle_menu.setAdapter(adapter);
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
