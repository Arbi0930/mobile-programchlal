package com.example.mynotification;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private static final String CHANNEL_ID = "my_channel_01";
    private static final String CHANNEL_ID2 = "my_channel_02";
    private static final String CHANNEL_ID3 = "my_channel_03";
    private static final String CHANNEL_NAME = "my_channel_01_NAME";
    private static final String CHANNEL_DESC = "my_channel_01_DESC";
    NotificationManager notificationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn = (Button) findViewById(R.id.Notify);
        Button btn2 = (Button) findViewById(R.id.Notify2);


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNotificationChannel(CHANNEL_ID);
                NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(),CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_launcher_background)
                        .setContentTitle("Миний сэтгэгдэл")
                        .setContentText("Энэ хэсэгт олон тооны мэдэгдэл")
                        .setStyle(new NotificationCompat.BigTextStyle().bigText("Энэ хэсэгт олон тооны мэдэгдлийг үүсгэж болно"))
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);
                notificationManager.notify(0,builder.build());
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNotificationChannel(CHANNEL_ID2);
                Intent intent = new Intent(getApplicationContext(), AlertDetails.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                PendingIntent  pendingIntent  = PendingIntent.getActivity(getApplicationContext(),
                        0 ,intent,  PendingIntent.FLAG_IMMUTABLE);
                NotificationCompat.Builder builder2 = new NotificationCompat.Builder(getApplicationContext(),CHANNEL_ID2)
                        .setSmallIcon(R.drawable.ic_launcher_background)
                        .setContentTitle("My notification")
                        .setContentText("Hello World")
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);
                notificationManager.notify(0,builder2.build());
            }
        });
    }
    private void createNotificationChannel(String CHANNEL_ID) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = CHANNEL_NAME;
            String description = CHANNEL_DESC;
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            notificationManager  = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}