package com.example.sqlitelab;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final String CHANNEL_ID = "my_channel_01";
    private static final String CHANNEL_NAME = "my_channel_01_NAME";
    private static final String CHANNEL_DESC = "my_channel_01_DESC";
    EditText b_name, b_author, b_date;
    Button saveBtn, viewBtn;
    Intent intent;
    NotificationManager notificationManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        b_name = (EditText) findViewById(R.id.bookname);
        b_author = (EditText) findViewById(R.id.bookauthor);
        b_date = (EditText) findViewById(R.id.bookdate);

        saveBtn = (Button) findViewById(R.id.btnSave);
        viewBtn = (Button) findViewById(R.id.btnView);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String bname = b_name.getText().toString()+"\n" ;
                String bauthor = b_author.getText().toString();
                String bdate = b_date.getText().toString();
                DbHandler dbHandler = new DbHandler(MainActivity.this);
                dbHandler.insertBookDetails (bname, bauthor,bdate);
                intent = new Intent( MainActivity. this,DetailsActivity.class);
                startActivity(intent);
                Toast.makeText(getApplicationContext(),"8гегдел оруулав...", Toast. LENGTH_SHORT).show();
                createNotificationChannel(CHANNEL_ID);
                NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(),CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_launcher_background)
                        .setContentTitle(bname)
                        .setContentText(bauthor)
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(bauthor))
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);
                notificationManager.notify(0,builder.build());
            }
        });
        viewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent( MainActivity. this,DetailsActivity.class);
                startActivity(intent);
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