package com.advance.sanatani_vivah.fcm;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

public class NotificationChannels {
    public static final String CHANNEL_PROFILE_VISITORS = "Profile Visitors";
    public static final String CHANNEL_PENDING_INTEREST = "Pending Interest";
    public static final String CHANNEL_RECEIVED_INTEREST = "Received Interest";
    public static final String CHANNEL_MESSAGES = "MessagesActivity";
    public static final String CHANNEL_COMMON_MESSAGES = "App Notifications";

    public void createNotificationChannels(Context context){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel1 = new NotificationChannel(CHANNEL_COMMON_MESSAGES, "MessagesActivity", NotificationManager.IMPORTANCE_HIGH);
            channel1.setDescription("This channel for com notification");
            NotificationChannel channel2 = new NotificationChannel(CHANNEL_PROFILE_VISITORS, "Profile Visitors", NotificationManager.IMPORTANCE_LOW);
            channel2.setDescription("This channel for profile visitors notification");
            NotificationChannel channel3 = new NotificationChannel(CHANNEL_PENDING_INTEREST, "Pending Interest", NotificationManager.IMPORTANCE_LOW);
            channel2.setDescription("This channel for pending interest notification");
            NotificationChannel channel4 = new NotificationChannel(CHANNEL_RECEIVED_INTEREST, "Received Interest", NotificationManager.IMPORTANCE_LOW);
            channel2.setDescription("This channel for received interest notification");
            NotificationChannel channel5 = new NotificationChannel(CHANNEL_MESSAGES, "MessagesActivity", NotificationManager.IMPORTANCE_HIGH);
            channel2.setDescription("This channel for chat notification");


            NotificationManager manager = context.getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel1);
//            manager.createNotificationChannel(channel2);
//            manager.createNotificationChannel(channel3);
//            manager.createNotificationChannel(channel4);
//            manager.createNotificationChannel(channel5);
        }
    }

    public void deleteChannelId(Context context,String channelId){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager manager = context.getSystemService(NotificationManager.class);
            manager.deleteNotificationChannel(channelId);
        }
    }
}
