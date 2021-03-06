package com.example.edward.neweventmanagementsystem.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.edward.neweventmanagementsystem.R;


public class MenuViewHolder extends RecyclerView.ViewHolder {
    public TextView checkInName, txtEventPrice, txtRegisterContactNumber, txtEventCapacity, fileName, txtRegisterEventId, txtRegisterEventStartDate, txtRegisterEventName, txtContact_number, txtRegisterEventRadiogroup, txtRegisterEventLocation;
    public ImageView imageView;


    public MenuViewHolder(@NonNull View itemView) {
        super(itemView);
        txtRegisterEventId = itemView.findViewById(R.id.RegisterEventId);
        txtRegisterEventStartDate = itemView.findViewById(R.id.RegisterEventStartDate);
        txtRegisterEventName = itemView.findViewById(R.id.RegisterEventName);
        txtRegisterContactNumber = itemView.findViewById(R.id.contact_number);
        txtRegisterEventRadiogroup = itemView.findViewById(R.id.RegisterEventRadiogroup);
        txtRegisterEventLocation = itemView.findViewById(R.id.RegisterEventLocation);
        txtEventCapacity = itemView.findViewById(R.id.txtEventCapacity);
        txtEventPrice = itemView.findViewById(R.id.txtEventPrice);
        imageView = itemView.findViewById(R.id.item_image);
        fileName = itemView.findViewById(R.id.fileName);
        checkInName = itemView.findViewById(R.id.checkInName);
    }

}

