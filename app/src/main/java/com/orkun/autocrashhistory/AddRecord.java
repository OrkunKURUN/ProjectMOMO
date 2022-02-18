package com.orkun.autocrashhistory;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddRecord extends AppCompatActivity {

    private String vin;
    private String url;
    MainActivity firstMenu = new MainActivity();
    carDatabase dbManager = new carDatabase(this);
    private DatabaseReference rDatabase = FirebaseDatabase.getInstance().getReference();

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
        PictureURL Precord = null;
        CarRecord Crecord = null;
        int id = 0;

        Cursor cursor = db.rawQuery("SELECT user_id FROM Users WHERE name = '" + firstMenu.getNameActive() + "'", null);
        if (cursor.moveToFirst()) {
            id = cursor.getInt(0);
        }
        cursor.close();
        cursor = db.rawQuery("SELECT * FROM CarRecords WHERE vin = '" + vin + "' AND user_id = "+id, null);
        if (cursor.getCount() == 0) {
            db.execSQL("INSERT INTO CarRecords(user_id, vin) VALUES (" + id + ",'" + vin + "')");
            //pushing to Firebase database:
            Cursor c = db.rawQuery("SELECT * FROM CarRecords WHERE vin = '" + vin + "' AND user_id = "+id,null);
            if(c.moveToFirst()){
                Crecord = new CarRecord(c.getInt(0),c.getInt(1),c.getString(2));
            }
            c.close();
        }
        cursor.close();
        rDatabase.child("carRecord").child(String.valueOf(Crecord.recordId)).setValue(Crecord);
        //...
        cursor = db.rawQuery("SELECT record_id FROM CarRecords WHERE vin = '" + vin + "'", null);
        if (cursor.moveToFirst()) {
            id = cursor.getInt(0);
        }
        cursor.close();
        cursor = db.rawQuery("SELECT * FROM PictureURL WHERE url = '" + url + "'", null);
        if (cursor.getCount() == 0) {
            db.execSQL("INSERT INTO PictureURL(record_id, url) VALUES (" + id + ",'" + url + "')");
            //pushing to Firebase database:
            Cursor c = db.rawQuery("SELECT * FROM PictureURL WHERE url = '" + url + "'",null);
            if(c.moveToFirst()){
                Precord = new PictureURL(c.getInt(0),c.getInt(1),c.getString(2));
            }
            c.close();
        }
        cursor.close();
        rDatabase.child("pictureUrlRecord").child(String.valueOf(Precord.urlId)).setValue(Precord);
        //...
        Toast.makeText(getApplicationContext(),"New record added!",Toast.LENGTH_SHORT).show();
    }
}
