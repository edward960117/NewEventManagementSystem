package com.example.edward.neweventmanagementsystem.Model;

public class EventInfo {
    private String RegisterEventId;
    private String imageToUpload;
    private String RegisterEventName;
    private String RegisterContactNumber;
    private String RegisterEventStartDate;
    private String RegisterEventRadiogroup;
    private String RegisterEventLocation;
    private String fileName;
    private  String EventPrice;
    private  String EventCapacity;

    public EventInfo(){

    }

    public EventInfo(String registerEventId, String imageToUpload, String registerEventName, String registerContactNumber, String registerEventStartDate, String registerEventRadiogroup, String registerEventLocation, String fileName, String eventPrice, String eventCapacity) {
        RegisterEventId = registerEventId;
        this.imageToUpload = imageToUpload;
        RegisterEventName = registerEventName;
        RegisterContactNumber = registerContactNumber;
        RegisterEventStartDate = registerEventStartDate;
        RegisterEventRadiogroup = registerEventRadiogroup;
        RegisterEventLocation = registerEventLocation;
        this.fileName = fileName;
        EventPrice = eventPrice;
        EventCapacity = eventCapacity;
    }

    public EventInfo(String registerEventName, String registerEventStartDate) {
        RegisterEventName = registerEventName;
        RegisterEventStartDate = registerEventStartDate;
    }

    public String getRegisterEventId() {
        return RegisterEventId;
    }

    public void setRegisterEventId(String registerEventId) {
        RegisterEventId = registerEventId;
    }

    public String getImageToUpload() {
        return imageToUpload;
    }

    public void setImageToUpload(String imageToUpload) {
        this.imageToUpload = imageToUpload;
    }

    public String getRegisterEventName() {
        return RegisterEventName;
    }

    public void setRegisterEventName(String registerEventName) {
        RegisterEventName = registerEventName;
    }

    public String getRegisterContactNumber() {
        return RegisterContactNumber;
    }

    public void setRegisterContactNumber(String registerContactNumber) {
        RegisterContactNumber = registerContactNumber;
    }

    public String getRegisterEventStartDate() {
        return RegisterEventStartDate;
    }

    public void setRegisterEventStartDate(String registerEventStartDate) {
        RegisterEventStartDate = registerEventStartDate;
    }

    public String getRegisterEventRadiogroup() {
        return RegisterEventRadiogroup;
    }

    public void setRegisterEventRadiogroup(String registerEventRadiogroup) {
        RegisterEventRadiogroup = registerEventRadiogroup;
    }

    public String getRegisterEventLocation() {
        return RegisterEventLocation;
    }

    public void setRegisterEventLocation(String registerEventLocation) {
        RegisterEventLocation = registerEventLocation;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getEventPrice() {
        return EventPrice;
    }

    public void setEventPrice(String eventPrice) {
        EventPrice = eventPrice;
    }

    public String getEventCapacity() {
        return EventCapacity;
    }

    public void setEventCapacity(String eventCapacity) {
        EventCapacity = eventCapacity;
    }

    @Override
    public String toString() {
        return "EventInfo{" +
                "RegisterEventId='" + RegisterEventId + '\'' +
                ", imageToUpload='" + imageToUpload + '\'' +
                ", RegisterEventName='" + RegisterEventName + '\'' +
                ", RegisterContactNumber='" + RegisterContactNumber + '\'' +
                ", RegisterEventStartDate='" + RegisterEventStartDate + '\'' +
                ", RegisterEventRadiogroup='" + RegisterEventRadiogroup + '\'' +
                ", RegisterEventLocation='" + RegisterEventLocation + '\'' +
                ", EventPrice='" + EventPrice + '\'' +
                ", EventCapacity='" + EventCapacity + '\'' +
                '}';
    }
}
