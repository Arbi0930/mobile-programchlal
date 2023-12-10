package com.example.imageload;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.os.AsyncTask;
import android.graphics.Bitmap;

import java.io.InputStream;

public class MainActivity extends AppCompatActivity {
    TextView titleTextView, categoryTextView;
    ProgressDialog progressDialog;
    Button displayData, displayData2;
    ImageView imageView, imageView2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //get the reference  of VIews's
        titleTextView = (TextView) findViewById(R.id.titleTextView);
        categoryTextView = (TextView) findViewById(R.id.categoryTextView);
        displayData = (Button) findViewById(R.id.displayData);
        displayData2 = (Button) findViewById(R.id.displayData2);
        imageView = (ImageView) findViewById(R.id.imageView);
        imageView2 = (ImageView) findViewById(R.id.imageView2);

        String imageUrl = "https://www.countryflags.com/wp-content/uploads/mongolia-flag-png-large.png";
        String imageUrl2 = "https://www.countryflags.com/wp-content/uploads/japan-flag-png-large.png";
        Log.d("OK", "Get an image");

        displayData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DownloadImageTask myAsyncTasks = new  DownloadImageTask(imageView);
                myAsyncTasks.execute(imageUrl);
            }
        });
        displayData2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DownloadImageTask myAsyncTasks2 = new  DownloadImageTask(imageView2);
                myAsyncTasks2.execute(imageUrl2);

            }
        });
    }
    public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Please Wait");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap myImage = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                myImage = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return myImage;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
            progressDialog.hide();
        }
    }
}