package com.example.mydschoolteachersapp.Model;

public class NotificationsModel {

    private String addedDate;
    private String staffId;
    private String notification;
    private String notificationId;

    public NotificationsModel(String addedDate, String staffId, String notification, String notificationId) {
        this.addedDate = addedDate;
        this.staffId = staffId;
        this.notification = notification;
        this.notificationId = notificationId;
    }

    public String getAddedDate() {
        return addedDate;
    }

    public void setAddedDate(String addedDate) {
        this.addedDate = addedDate;
    }

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    public String getNotification() {
        return notification;
    }

    public void setNotification(String notification) {
        this.notification = notification;
    }

    public String getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(String notificationId) {
        this.notificationId = notificationId;
    }
}
