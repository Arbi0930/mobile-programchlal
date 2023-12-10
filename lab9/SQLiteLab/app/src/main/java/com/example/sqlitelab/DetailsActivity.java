package com.example.sqlitelab;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class DetailsActivity extends AppCompatActivity {
    Intent intent;
    ArrayList<HashMap<String, String>> userList;
    ListView lv;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail);
        DbHandler db = new DbHandler(this);
        userList = db.GetUsers();
        lv = findViewById(R.id.user_list);
        ListAdapter adapter = new SimpleAdapter(
                DetailsActivity.this,
                userList,
                R.layout.list_row,new
                String[]{"bname", "bauthor","bdate"},
                new int[]{R.id.details_bname, R.id.details_bauthor, R.id.details_bdate});
        lv.setAdapter(adapter);
        Button back = (Button) findViewById(R.id.btnBack);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(DetailsActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
    public void onDeleteButtonClick(View view) {
        int itemPosition = lv.getPositionForView((View) view.getParent());
        HashMap<String, String> selectedItem = userList.get(itemPosition);
        String bName = selectedItem.get("bname");

        DbHandler dbHandler = new DbHandler(this);
        int rowsDeleted = dbHandler.DeleteBook(bName);

        if (rowsDeleted > 0) {
            Toast.makeText(getApplicationContext(), "Book Deleted!", Toast.LENGTH_LONG).show();
            userList.remove(itemPosition);
            ((SimpleAdapter) lv.getAdapter()).notifyDataSetChanged();
        } else {
            Toast.makeText(getApplicationContext(), "Failed to delete book!", Toast.LENGTH_LONG).show();
        }
    }


}
