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
    public TextView  txtRegisterEventId, txtRegisterEventStartDate, txtRegisterEventName, txtContact_number, txtRegisterEventRadiogroup, txtRegisterEventLocation;
    public ImageView imageView;


    public MenuViewHolder(@NonNull View itemView) {
        super(itemView);
        txtRegisterEventId = itemView.findViewById(R.id.RegisterEventId);
        txtRegisterEventStartDate = (TextView) itemView.findViewById(R.id.RegisterEventStartDate);
        txtRegisterEventName = (TextView) itemView.findViewById(R.id.RegisterEventName);
        txtContact_number = (TextView) itemView.findViewById(R.id.contact_number);
        txtRegisterEventRadiogroup = (TextView) itemView.findViewById(R.id.RegisterEventRadiogroup);
        txtRegisterEventLocation = (TextView) itemView.findViewById(R.id.RegisterEventLocation);
        imageView = (ImageView) itemView.findViewById(R.id.item_image);
//        fileName = (TextView) itemView.findViewById(R.id.fileName);
    }

}

