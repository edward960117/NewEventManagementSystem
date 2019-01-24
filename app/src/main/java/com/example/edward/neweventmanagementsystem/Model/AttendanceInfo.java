package com.example.edward.neweventmanagementsystem.Model;

public class AttendanceInfo {
    private String EventName;
    private String CheckInTime;
    private String CheckInDate;
    private String CurrentLocation;
    private String CheckInName;
    private String EventId;

    public AttendanceInfo(String eventName, String checkInTime, String checkInDate, String currentLocation, String checkInName, String eventId) {
        EventName = eventName;
        CheckInTime = checkInTime;
        CheckInDate = checkInDate;
        CurrentLocation = currentLocation;
        CheckInName = checkInName;
        EventId = eventId;
    }

    public String getEventName() {
        return EventName;
    }

    public void setEventName(String eventName) {
        EventName = eventName;
    }

    public String getCheckInTime() {
        return CheckInTime;
    }

    public void setCheckInTime(String checkInTime) {
        CheckInTime = checkInTime;
    }

    public String getCheckInDate() {
        return CheckInDate;
    }

    public void setCheckInDate(String checkInDate) {
        CheckInDate = checkInDate;
    }

    public String getCurrentLocation() {
        return CurrentLocation;
    }

    public void setCurrentLocation(String currentLocation) {
        CurrentLocation = currentLocation;
    }

    public String getCheckInName() {
        return CheckInName;
    }

    public void setCheckInName(String checkInName) {
        CheckInName = checkInName;
    }

    public String getEventId() {
        return EventId;
    }

    public void setEventId(String eventId) {
        EventId = eventId;
    }

    @Override
    public String toString() {
        return "AttendanceInfo{" +
                "EventName='" + EventName + '\'' +
                ", CheckInTime='" + CheckInTime + '\'' +
                ", CheckInDate='" + CheckInDate + '\'' +
                ", CurrentLocation='" + CurrentLocation + '\'' +
                ", CheckInName='" + CheckInName + '\'' +
                ", EventId='" + EventId + '\'' +
                '}';
    }
}
