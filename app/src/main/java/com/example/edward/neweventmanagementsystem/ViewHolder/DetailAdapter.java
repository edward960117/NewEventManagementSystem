package com.example.edward.neweventmanagementsystem.ViewHolder;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.edward.neweventmanagementsystem.Model.AttendanceInfo;
import com.example.edward.neweventmanagementsystem.R;

import java.util.ArrayList;

public class DetailAdapter extends RecyclerView.Adapter<DetailAdapter.DetailViewHolder>{
    public class DetailViewHolder extends RecyclerView.ViewHolder {
        public TextView checkInName, txtCheckTime, txtCheckDate;


        public DetailViewHolder(@NonNull View itemView) {
            super(itemView);
            checkInName = itemView.findViewById(R.id.checkInName);
            txtCheckTime = itemView.findViewById(R.id.checkInTime);
            txtCheckDate = itemView.findViewById(R.id.checkInDate);
        }

    }
    private Context mContext;
    private ArrayList<AttendanceInfo> mArrayCourses;

    public DetailAdapter(Context context, ArrayList<AttendanceInfo> arrayCourses) {
        mContext = context;
        mArrayCourses = arrayCourses;
    }

    @Override
    public int getItemCount() {
        return mArrayCourses.size();
    }

    @Override
    public DetailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_attendance_list_main_list, parent, false);
        return new DetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DetailViewHolder holder, int position) {
        AttendanceInfo attendanceInfo = mArrayCourses.get(position);

        holder.checkInName.setText(attendanceInfo.getCheckInName());
        holder.txtCheckDate.setText(attendanceInfo.getCheckInDate());
        holder.txtCheckTime.setText(attendanceInfo.getCheckInTime());
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

}


