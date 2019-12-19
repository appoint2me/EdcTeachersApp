package com.example.mydschoolteachersapp;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.example.mydschoolteachersapp.Classes.Config;
import com.example.mydschoolteachersapp.Classes.SharedPrefrenceManager;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class MyFirebaseService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseService";
    String channel_id="1";
    public MyFirebaseService() {
    }

    @Override
    public void onNewToken(String s) {
        SharedPrefrenceManager.setPrefVal(this, Config.FIREBASE_TOKEN,s);
        Log.d(TAG, "onNewToken: "+s);
        System.out.println("Refreshed token: " + s);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        // ...

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());

        }

        // Check if message contains a notification payload.
        if (remoteMessage.getData() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getData().get("body"));
            sendNotification(remoteMessage);
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.

    }
    public void sendNotification(RemoteMessage remoteMessage)
    {
        NotificationCompat.Builder builder=new NotificationCompat.Builder(this,channel_id);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);//<--
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Map<String, String> data = remoteMessage.getData();

        String title = data.get("title");
        String message = data.get("body");

        Intent resultIntent = new Intent(this, NavigationActivity.class);


        resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent resultPendingIntent = PendingIntent.getActivity(
                this,
                0,
                resultIntent,
                PendingIntent.FLAG_ONE_SHOT
        );


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel(channel_id, "school", importance);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setSound(defaultSoundUri,new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE)
                    .build());
            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            assert manager != null;
            manager.createNotificationChannel(notificationChannel);
        }

        builder.setSmallIcon(R.drawable.logo)
                .setContentTitle(remoteMessage.getData().get("title"))
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(message))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setAutoCancel(true);

        int notificationId = (int) System.currentTimeMillis();
        builder.setSound(defaultSoundUri);
        builder.setContentIntent(resultPendingIntent);




        if (manager != null) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(channel_id,
                        "Human Readable channel id",
                        NotificationManager.IMPORTANCE_DEFAULT);
                channel.setShowBadge(true);
                manager.createNotificationChannel(channel);

            }

            manager.notify(notificationId, builder.build());
        }

    }
}
