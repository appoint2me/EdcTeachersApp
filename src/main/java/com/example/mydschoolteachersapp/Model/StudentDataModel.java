package com.example.mydschoolteachersapp.Model;

public class StudentDataModel {
    private String attendance;
    private String firstName;
    private String classId;
    private String sectionId;
    private String rollNo;
    private String applicationId;
    private String schoolId;

    public StudentDataModel(String attendance, String firstName, String classId, String sectionId, String rollNo, String applicationId, String schoolId) {
        this.attendance = attendance;
        this.firstName = firstName;
        this.classId = classId;
        this.sectionId = sectionId;
        this.rollNo = rollNo;
        this.applicationId = applicationId;
        this.schoolId = schoolId;
    }

    public String getAttendance() {
        return attendance;
    }

    public void setAttendance(String attendance) {
        this.attendance = attendance;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getSectionId() {
        return sectionId;
    }

    public void setSectionId(String sectionId) {
        this.sectionId = sectionId;
    }

    public String getRollNo() {
        return rollNo;
    }

    public void setRollNo(String rollNo) {
        this.rollNo = rollNo;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }
}
