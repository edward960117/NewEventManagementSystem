package com.example.edward.neweventmanagementsystem.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.edward.neweventmanagementsystem.R;

public class PlaceListMenuViewHolder extends RecyclerView.ViewHolder {

    public TextView name_text_view, address_text_view;

    public PlaceListMenuViewHolder(@NonNull View itemView) {
        super(itemView);
        name_text_view = itemView.findViewById(R.id.name_text_view);
        address_text_view = itemView.findViewById(R.id.address_text_view);

    }
}
