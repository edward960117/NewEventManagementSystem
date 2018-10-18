package com.example.edward.neweventmanagementsystem.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.edward.neweventmanagementsystem.R;


public class MenuViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txtRegisterEventStartDate, txtRegisterEventName, txtContact_number, txtRegisterEventRadiogroup, txtRegisterEventLocation;
    public ImageView imageView;

    private ItemClickListener itemClickListener;

    public MenuViewHolder(@NonNull View itemView) {
        super(itemView);
        txtRegisterEventStartDate = (TextView) itemView.findViewById(R.id.RegisterEventStartDate);
        txtRegisterEventName = (TextView) itemView.findViewById(R.id.RegisterEventName);
        txtContact_number = (TextView) itemView.findViewById(R.id.contact_number);
        txtRegisterEventRadiogroup = (TextView) itemView.findViewById(R.id.RegisterEventRadiogroup);
        txtRegisterEventLocation = (TextView) itemView.findViewById(R.id.RegisterEventLocation);
        imageView = (ImageView) itemView.findViewById(R.id.item_image);
    }
    public void setItemClickListener (ItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view, getAdapterPosition(), false);
    }

}

