package com.example.mydschoolteachersapp.Model;

public class HolidaysModel {
    private String holidayDate;
    private String holidayDescription;


    public HolidaysModel(String holidayDate, String holidayDescription ) {
        this.holidayDescription = holidayDescription;
        this.holidayDate = holidayDate;
    }

    public String getHolidayDescription() {
        return holidayDescription;
    }

    public void setHolidayDescription(String holidayDescription) {
        this.holidayDescription = holidayDescription;
    }

    public String getHolidayDate() {
        return holidayDate;
    }

    public void setHolidayDate(String holidayDate) {
        this.holidayDate = holidayDate;
    }
}
