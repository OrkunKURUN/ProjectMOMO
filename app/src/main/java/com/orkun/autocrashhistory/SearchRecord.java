package com.orkun.autocrashhistory;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

//Example VIN: ABC123, ABC456


public class SearchRecord extends AppCompatActivity {
    private carDatabase dbManager = new carDatabase(this);
    private String vin;
    private String recordId;
    private DatabaseReference rDatabase;
    private TextView idList, namePlace;
    private WebView url;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_record);

        EditText vinInput = (EditText) findViewById(R.id.vinInput2);
        Button recordSearch = (Button) findViewById(R.id.recordSearch);
        idList = (TextView) findViewById(R.id.idList);
        EditText recordInput = (EditText) findViewById(R.id.recordIdInput);
        Button lookAtRecord = (Button) findViewById(R.id.lookAtRecord);
        url = (WebView) findViewById(R.id.recordPicture);
        namePlace = (TextView) findViewById(R.id.namePlace);

        url.getSettings().setJavaScriptEnabled(true);

        recordSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vin = vinInput.getText().toString();
                makeList();
            }
        });
        lookAtRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recordId = recordInput.getText().toString();
                //int id = Integer.parseInt(recordId);
                url.setWebViewClient(new WebViewClient());
                //url.loadUrl(getURL(id));
                getURL(recordId);
                //namePlace.setText("Uploaded by: "+getUserName(id));
            }
        });
    }

    public void makeList(){
        String list = "";

        rDatabase = FirebaseDatabase.getInstance().getReference().child("car-records").child(vin);

        ValueEventListener recordListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<String> list = new ArrayList<>();
                String listToWrite = "";
                for(DataSnapshot cSnapshot : dataSnapshot.getChildren()){
                    PictureURL record = cSnapshot.getValue(PictureURL.class);
                    list.add(record.getUrlId());
                }
                for(String id : list)
                    listToWrite = listToWrite + id + "\n";
                idList.setText(listToWrite);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("TAG", "loadPost:onCancelled", databaseError.toException());
            }
        };
        rDatabase.addValueEventListener(recordListener);

    }
    public void getURL(String id){
        /*SQLiteDatabase db = dbManager.getReadableDatabase();
        String url = "";

        Cursor cursor = db.rawQuery("SELECT url FROM PictureURL WHERE url_id = "+id,null);
        if(cursor.moveToFirst()){
            url = cursor.getString(0);
        }
        cursor.close();

        return url;*/
        rDatabase = FirebaseDatabase.getInstance().getReference().child("car-records").child(vin).child(id);
        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                PictureURL record = snapshot.getValue(PictureURL.class);
                if(record != null){
                    namePlace.setText("Uploaded by: "+record.getUserName());
                    url.loadUrl(record.getUrl());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Getting Post failed, log a message
                Log.w("TAG", "loadPost:onCancelled", error.toException());
            }
        };
        rDatabase.addValueEventListener(listener);
    }
    public String getUserName(int id){
        SQLiteDatabase db = dbManager.getReadableDatabase();
        String userName = "";
        int userId = 0;
        int recordId = 0;

        Cursor cursor = db.rawQuery("SELECT record_id FROM PictureURL WHERE url_id = "+ id,null);
        if(cursor.moveToFirst()){
            recordId = cursor.getInt(0);
        }
        cursor.close();
        cursor = db.rawQuery("SELECT user_id FROM CarRecords WHERE record_id = "+recordId,null);
        if(cursor.moveToFirst()){
            userId = cursor.getInt(0);
        }
        cursor.close();
        cursor = db.rawQuery("SELECT name FROM Users WHERE user_id = "+userId,null);
        if(cursor.moveToFirst()){
            userName = cursor.getString(0);
        }
        cursor.close();

        return userName;
    }
}
