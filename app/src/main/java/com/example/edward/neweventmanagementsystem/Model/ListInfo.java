package com.example.edward.neweventmanagementsystem.Model;

public class ListInfo {

    private String RegisterEventId;
    private String imageToUpload;
    private String RegisterEventName;
    private String RegisterContactNumber;
    private String RegisterEventStartDate;
    private String RegisterEventRadiogroup;
    private String RegisterEventLocation;

    public ListInfo(String registerEventId, String imageToUpload, String registerEventName, String registerContactNumber, String registerEventStartDate, String registerEventRadiogroup, String registerEventLocation) {
        RegisterEventId = registerEventId;
        this.imageToUpload = imageToUpload;
        RegisterEventName = registerEventName;
        RegisterContactNumber = registerContactNumber;
        RegisterEventStartDate = registerEventStartDate;
        RegisterEventRadiogroup = registerEventRadiogroup;
        RegisterEventLocation = registerEventLocation;
    }

    public ListInfo(){

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
}
