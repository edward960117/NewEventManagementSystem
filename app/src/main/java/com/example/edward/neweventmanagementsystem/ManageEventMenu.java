package com.example.edward.neweventmanagementsystem;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class ManageEventMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_event_menu);


    }

    public void OpenCreateEvent (View view) {
        Intent intent = new Intent(this, CreateEvent.class);
        startActivity(intent);
    }
    public void OpenListOfEvent (View view) {
        Intent intent = new Intent(this, ListOfEvent.class);
        startActivity(intent);
    }
}
