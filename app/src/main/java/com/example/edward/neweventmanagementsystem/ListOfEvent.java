package com.example.edward.neweventmanagementsystem;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.edward.neweventmanagementsystem.Model.EventInfo;
import com.example.edward.neweventmanagementsystem.Model.ListInfo;
//import com.example.edward.neweventmanagementsystem.ViewHolder.ItemClickListener;
import com.example.edward.neweventmanagementsystem.ViewHolder.MenuViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ListOfEvent extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference eventinfo;

    RecyclerView recycle_menu;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_event);

        recycle_menu = (RecyclerView) findViewById(R.id.recycle_menu);
        recycle_menu.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);

        recycle_menu.setLayoutManager(layoutManager);
        database  = FirebaseDatabase.getInstance();
        eventinfo = database.getReference("ListOfEvent");
        loadMenu();
        recycle_menu.addOnItemTouchListener(
                new RecyclerItemClickListener(this, recycle_menu ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {

                    }

                    @Override public void onLongItemClick(View view, int position) {
                        final String id = ((TextView)view.findViewById(R.id.RegisterEventId)).getText().toString();
                        //final String filename = ((TextView)view.findViewById(R.id.fileName)).getText().toString();
                        Toast.makeText(getBaseContext(),"Deleting of event id "+id,Toast.LENGTH_SHORT).show();
                        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
                        //storageReference.child("profileImageUrl").child(filename).delete();
                        eventinfo.child(id).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(getBaseContext(),"Event id "+id+" has been deleted.",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                })
        );
    }

    public void loadMenu(){

        FirebaseRecyclerAdapter <EventInfo, MenuViewHolder> adapter = new FirebaseRecyclerAdapter<EventInfo, MenuViewHolder>(EventInfo.class, R.layout.activity_list, MenuViewHolder.class, eventinfo) {


            @Override
            protected void populateViewHolder(MenuViewHolder viewHolder, EventInfo model, int position) {

                viewHolder.txtRegisterEventId.setText(model.getRegisterEventId());
                viewHolder.txtRegisterEventStartDate.setText(model.getRegisterEventStartDate());
                viewHolder.txtRegisterEventName.setText(model.getRegisterEventName());
                viewHolder.txtContact_number.setText(model.getRegisterContactNumber());
                viewHolder.txtRegisterEventRadiogroup.setText(model.getRegisterEventRadiogroup());
                viewHolder.txtRegisterEventLocation.setText(model.getRegisterEventLocation());
//                viewHolder.fileName.setText(model.getFileName());

                //Picasso.with(getBaseContext()).load(model.getImageToUpload()).into(viewHolder.imageView);
                //Glide.with(getBaseContext()).load(model.getImageToUpload()).into(viewHolder.imageView);

                Glide.with(getBaseContext()).load(model.getImageToUpload()
                ).into(viewHolder.imageView);
                System.out.println(model.getRegisterEventName());
                System.out.println(model.getImageToUpload());
            }
        };
        recycle_menu.setAdapter(adapter);
    }


}
