package com.shiva.serviceanil;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

class MyAppsNotificationManager {
    private Context context;

    private static MyAppsNotificationManager instance;
    private NotificationManagerCompat notificationManagerCompat;
    private NotificationManager notificationManager;

    private MyAppsNotificationManager(Context context){
        this.context = context;
        notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    public static MyAppsNotificationManager getInstance(Context context){
        if(instance==null){
            instance = new MyAppsNotificationManager(context);
        }
        return instance;
    }

    public void registerNotificationChannelChannel(String channelId, String channelName, String channelDescription) {
        // Notification Channel is required in oreo and post oreo.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription(channelDescription);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

    public Notification getNotification(Class targetNotificationActivity, String title, int priority, boolean autoCancel, int notificationId){
        Intent intent = new Intent(context, targetNotificationActivity);
        // PendingIntent is required to set the intent on a notification.
        // 1st param is context
        // 2nd param is requestCode with which we can cancel the PendingIntent
        // 3rd param is intent
        // 4th param is flag defines when we update the PendingIntent with new intent.
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 1, intent, 0);

        // NotificationCompat.Builder 2nd parameter channelId will be ignore by lower version < Oreo 26.
        // NotificationCompat.Builder can be used for all the SDK versions, there will be no crash.
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,context.getString(R.string.channelId))
                .setSmallIcon(R.drawable.ic_baseline_notification_important_24)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_icon_large))
                .setContentTitle(title)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent) // setting the pendingIntent
                .setChannelId("123")
                .setAutoCancel(true);
        return builder.build();

    }

    public void cancelNotification(int notificationId){
        notificationManager.cancel(notificationId);
    }
}
