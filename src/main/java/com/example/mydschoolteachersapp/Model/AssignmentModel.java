package com.example.mydschoolteachersapp.Model;

public class AssignmentModel {
    private String assignmentId;
    private String title;
    private String documentPath;

    public String getAssignmentId() {
        return assignmentId;
    }

    public void setAssignmentId(String assignmentId) {
        this.assignmentId = assignmentId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDocumentPath() {
        return documentPath;
    }

    public void setDocumentPath(String documentPath) {
        this.documentPath = documentPath;
    }

    public AssignmentModel(String assignment_id, String title, String documentPath) {
        this.assignmentId = assignmentId;
        this.title = title;
        this.documentPath = documentPath;
    }

}
