package com.example.PawssionMobile;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

public class ReminderBroadcast extends BroadcastReceiver {

    Boolean connected = false;

    @Override
    public void onReceive(Context context, Intent intent) {

        // Get id
        int notificationId = intent.getIntExtra("notificaiontId", 0);

        Intent intent1 = new Intent(context,Schedule.class);
        intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // NotificationManager
        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,intent1,PendingIntent.FLAG_ONE_SHOT);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);


        //Prepare Notification
        NotificationCompat.Builder Builder = new NotificationCompat.Builder(context, "Pawssion");
        Builder.setSmallIcon(R.drawable.ic_launcher_background);
        Builder.setContentTitle("Pawssion");
        Builder.setContentText("The schedule feed the Pet!");
        Builder.setAutoCancel(true);
        Builder.setPriority(NotificationCompat.PRIORITY_HIGH);
        Builder.setContentIntent(pendingIntent);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            String channelId = "Pawssion";

            NotificationChannel channel = new NotificationChannel(channelId, "channel name", NotificationManager.IMPORTANCE_HIGH);;
            notificationManager.createNotificationChannel(channel);
            channel.enableVibration(true);
            Builder.setChannelId(channelId);
        }

        //Notify
        Notification notification = Builder.build();
        notificationManager.notify(1, notification);


    }
}
