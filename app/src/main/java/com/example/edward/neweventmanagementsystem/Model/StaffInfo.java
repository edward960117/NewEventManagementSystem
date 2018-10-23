package com.example.edward.neweventmanagementsystem.Model;

public class StaffInfo {

    private String staffIdText;
    private String txtAddNewStaffText;

    public StaffInfo(String staffIdText, String txtAddNewSatffText) {
        this.staffIdText = staffIdText;
        this.txtAddNewStaffText = txtAddNewSatffText;
    }

    public StaffInfo() {
    }

    public String getStaffIdText() {
        return staffIdText;
    }

    public void setStaffIdText(String staffIdText) {
        this.staffIdText = staffIdText;
    }

    public String getTxtAddNewSatffText() {
        return txtAddNewStaffText;
    }

    public void setTxtAddNewSatffText(String txtAddNewSatffText) {
        this.txtAddNewStaffText = txtAddNewSatffText;
    }
}
