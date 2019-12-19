package com.example.mydschoolteachersapp.Model;

public class SubjectModel {
    private String subjectId;
    private String subjectName;

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getSubjectName(String subject_name) {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public SubjectModel(String subjectId, String subjectName) {
        this.subjectId = subjectId;
        this.subjectName = subjectName;
    }
    public SubjectModel()
    {

    }
}
