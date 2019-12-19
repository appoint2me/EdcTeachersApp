package com.example.mydschoolteachersapp.Classes;

import android.app.Application;

import net.gotev.uploadservice.BuildConfig;
import net.gotev.uploadservice.UploadService;

public class Initializer extends Application {

    @Override
    public void onCreate() {
        UploadService.NAMESPACE = BuildConfig.APPLICATION_ID;
        UploadService.NAMESPACE = "com.example.mydschoolteachersapp";

        super.onCreate();
    }
}
