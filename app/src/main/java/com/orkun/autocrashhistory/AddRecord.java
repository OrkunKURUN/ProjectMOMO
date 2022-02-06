package com.orkun.autocrashhistory;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddRecord extends AppCompatActivity {

    private String vin;
    private String url;
    MainActivity firstMenu = new MainActivity();
    carDatabase dbManager = new carDatabase(this);

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_record);

        EditText vinInput = (EditText) findViewById(R.id.vinInput);
        EditText urlInput = (EditText) findViewById(R.id.urlInput);
        Button submit = (Button) findViewById(R.id.submitRecord);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vin = vinInput.getText().toString();
                url = urlInput.getText().toString();
                add_record(vin, url);
            }
        });
    }
    public void add_record(String vin, String url) {
        SQLiteDatabase db = dbManager.getWritableDatabase();
        int id = 0;

        Cursor cursor = db.rawQuery("SELECT user_id FROM Users WHERE name = '" + firstMenu.getNameActive() + "'", null);
        if (cursor.moveToFirst()) {
            id = cursor.getInt(0);
        }
        cursor.close();
        cursor = db.rawQuery("SELECT * FROM CarRecords WHERE vin = '" + vin + "' AND user_id = "+id, null);
        if (cursor.getCount() == 0) {
            db.execSQL("INSERT INTO CarRecords(user_id, vin) VALUES (" + id + ",'" + vin + "')");
        }
        cursor.close();
        cursor = db.rawQuery("SELECT record_id FROM CarRecords WHERE vin = '" + vin + "'", null);
        if (cursor.moveToFirst()) {
            id = cursor.getInt(0);
        }
        cursor.close();
        cursor = db.rawQuery("SELECT * FROM PictureURL WHERE url = '" + url + "'", null);
        if (cursor.getCount() == 0) {
            db.execSQL("INSERT INTO PictureURL(record_id, url) VALUES (" + id + ",'" + url + "')");
        }
        cursor.close();
        Toast.makeText(getApplicationContext(),"New record added!",Toast.LENGTH_SHORT).show();
    }
}
