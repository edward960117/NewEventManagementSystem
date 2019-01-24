package com.example.edward.neweventmanagementsystem.Model;

import android.support.annotation.NonNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class OrganizerChatMessage implements Comparable<OrganizerChatMessage> {
    private String UserName;
    private String Message;
    private CharSequence Time;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a",Locale.ENGLISH);

    public OrganizerChatMessage() {

    }

    public OrganizerChatMessage(String userName, String message , CharSequence time) {
        UserName = userName;
        Message = message;
        Time = time;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }



    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }


    public CharSequence getTime() {
        return Time;
    }

    public void setTime(CharSequence time) {
        Time = time;
    }

    @Override
    public String toString() {
//        return "OrganizerChatMessage{" +
//                "UserName='" + UserName + '\'' +
//                ", Message='" + Message + '\'' +
//                '}';
        return UserName + ":" + Message +"             "+ Time ;

        //0000000000000000000000000000000000
    }


    public int compareTo(@NonNull OrganizerChatMessage that) {
        Calendar thisDate = Calendar.getInstance();
        Calendar thatDate = Calendar.getInstance();
        try {
            thisDate.setTime(sdf.parse(this.getTime().toString()));
            thatDate.setTime(sdf.parse(that.getTime().toString()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Long thisTime = thisDate.getTimeInMillis();
        Long thatTime = thatDate.getTimeInMillis();
        return ((int)(thisTime-thatTime));
    }
}