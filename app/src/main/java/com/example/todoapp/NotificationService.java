package com.example.todoapp;

import static android.content.Intent.getIntent;

import static com.example.todoapp.MainActivity.ALARM_REQ_CODE;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;

import java.util.ArrayList;
import java.util.Calendar;

@SuppressWarnings("ALL")
public class NotificationService extends Service {

    private static  final String CHANNEL_ID = "My Channel";
    private static  final int NOTIFICATION_ID = 100;
    private static  final int PI_REQ_CODE = 100;




    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        //Fetching the Data coming from MainActivity with intent iNext to show into Notifiaction
        String title = intent.getStringExtra("title");
        String desc = intent.getStringExtra("desc");







        //Notification creation

        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Notification notification;



        //Converting png icon into bitmap
        //Accessing the drawable
        Drawable drawable = ResourcesCompat.getDrawable(getResources(),R.drawable.todo6,null);
        //converting drawable into bitmapdrawable
        BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
        //converting  bitmapdrawable into bitmap
        Bitmap largeIcon = bitmapDrawable.getBitmap();

        //To open activity onclick on notification

        Intent iNotify = new Intent(getApplicationContext(),MainActivity.class);
        iNotify.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,PI_REQ_CODE,iNotify,PendingIntent.FLAG_UPDATE_CURRENT);

        //Big Picture Style(After stretching the notification)
        Notification.BigPictureStyle bigPictureStyle = new Notification.BigPictureStyle()
                .bigPicture(((BitmapDrawable) (ResourcesCompat.getDrawable(getResources(),R.drawable.todo6,null))).getBitmap())
                .bigLargeIcon(largeIcon)
                .setBigContentTitle(title)
                .setSummaryText(desc);

        //Inbox Style
        Notification.InboxStyle inboxStyle = new Notification.InboxStyle()
                .addLine("A")
                .addLine("B")
                .addLine("C")
                .addLine("D")
                .addLine("E")
                .addLine("F")
                .addLine("G")
                .addLine("H")
                .addLine("I")
                .addLine("J")
                .addLine("K")
                .addLine("L")
                .setBigContentTitle("Full Message")
                .setSummaryText("Message from Santu");


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            notification = new Notification.Builder(this)
                    .setLargeIcon(largeIcon)//always use png
                    .setSmallIcon(R.drawable.todo6)
                    .setContentText(title)//Title
                    .setSubText(desc)//SubTitle
                    .setContentIntent(pendingIntent)//To open an activity on click
                    .setChannelId(CHANNEL_ID)//This is availablr for Android 8 or grater
                    .setStyle(bigPictureStyle)//setting the style
//                    .setOngoing(true)//Or setAutoCancel(false) for older  Android
                    // it will prevent to swipe out the notification until process run
                    .build();
            //creating notification channel
            nm.createNotificationChannel(new NotificationChannel(CHANNEL_ID,"New Channel",NotificationManager.IMPORTANCE_HIGH));
        }else{
            notification = new Notification.Builder(this)
                    .setLargeIcon(largeIcon)//always use png
                    .setSmallIcon(R.drawable.todo6)
                    .setContentText(title)//Title
                    .setSubText(desc)//SubTitle
                    .setContentIntent(pendingIntent)//To open an activity on click
                    .setStyle(bigPictureStyle)//setting the style
//                    .setOngoing(true)
                    .build();
        }

        nm.notify(NOTIFICATION_ID,notification);

        return super.onStartCommand(intent, flags, startId);
    }
}
