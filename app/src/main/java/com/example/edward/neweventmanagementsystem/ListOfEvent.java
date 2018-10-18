package com.example.edward.neweventmanagementsystem;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.edward.neweventmanagementsystem.Model.EventInfo;
import com.example.edward.neweventmanagementsystem.Model.ListInfo;
//import com.example.edward.neweventmanagementsystem.ViewHolder.ItemClickListener;
import com.example.edward.neweventmanagementsystem.ViewHolder.MenuViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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

    }

    public void loadMenu(){

        FirebaseRecyclerAdapter <EventInfo, MenuViewHolder> adapter = new FirebaseRecyclerAdapter<EventInfo, MenuViewHolder>(EventInfo.class, R.layout.activity_list, MenuViewHolder.class, eventinfo) {


            @Override
            protected void populateViewHolder(MenuViewHolder viewHolder, EventInfo model, int position) {


                viewHolder.txtRegisterEventStartDate.setText(model.getRegisterEventStartDate());
                viewHolder.txtRegisterEventName.setText(model.getRegisterEventName());
                viewHolder.txtContact_number.setText(model.getRegisterContactNumber());
                viewHolder.txtRegisterEventRadiogroup.setText(model.getRegisterEventRadiogroup());
                viewHolder.txtRegisterEventLocation.setText(model.getRegisterEventLocation());

                //Picasso.with(getBaseContext()).load(model.getImageToUpload()).into(viewHolder.imageView);
                //Glide.with(getBaseContext()).load(model.getImageToUpload()).into(viewHolder.imageView);

                Glide.with(getBaseContext()).load(model.getImageToUpload()
                ).into(viewHolder.imageView);
                System.out.println(model.getRegisterEventName());
                System.out.println(model.getImageToUpload());

                final EventInfo clickItem = model;
//                viewHolder.setItemClickListener(new ItemClickListener() {
//                    @Override
//                    public void onClick(View view, int position, boolean isLongClick) {
//                        Toast.makeText(ListOfEvent.this, ""+clickItem.getRegisterEventName(),  Toast.LENGTH_SHORT).show();
//                    }
//                });

            }
        };
        recycle_menu.setAdapter(adapter);
    }



}
