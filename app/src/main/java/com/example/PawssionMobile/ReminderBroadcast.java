package com.example.PawssionMobile;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

public class ReminderBroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        // Get id
        int notificationId = intent.getIntExtra("notificaiontId", 0);

        Intent i = new Intent(context,Schedule.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,i,0);

        // NotificationManager
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name = "PawssionRemainderChannel";
            int importance = NotificationManager.IMPORTANCE_HIGH;

            NotificationChannel channel = new NotificationChannel("Pawssion", name, importance);
            notificationManager.createNotificationChannel(channel);
        }

        //Prepare Notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "Pawssion")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("Pawssion")
                .setContentText("The 1st schedule feed the Pet!")
                //.setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent);

        //Notify
        notificationManager.notify(notificationId, builder.build());


    }
}
