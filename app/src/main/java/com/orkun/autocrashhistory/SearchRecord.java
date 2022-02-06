package com.orkun.autocrashhistory;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

//Example VIN: ABC123

public class SearchRecord extends AppCompatActivity {
    private carDatabase dbManager = new carDatabase(this);
    private String vin;
    private String recordId;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_record);

        EditText vinInput = (EditText) findViewById(R.id.vinInput2);
        Button recordSearch = (Button) findViewById(R.id.recordSearch);
        TextView idList = (TextView) findViewById(R.id.idList);
        EditText recordInput = (EditText) findViewById(R.id.recordIdInput);
        Button lookAtRecord = (Button) findViewById(R.id.lookAtRecord);
        WebView url = (WebView) findViewById(R.id.recordPicture);
        TextView namePlace = (TextView) findViewById(R.id.namePlace);

        url.getSettings().setJavaScriptEnabled(true);

        recordSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vin = vinInput.getText().toString();
                idList.setText(makeList(vin));
            }
        });
        lookAtRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recordId = recordInput.getText().toString();
                int id = Integer.parseInt(recordId);
                url.setWebViewClient(new WebViewClient());
                url.loadUrl(getURL(id));
                namePlace.setText("Uploaded by: "+getUserName(id));
            }
        });
    }

    public String makeList(String vin){
        String list = "";
        ArrayList<Integer> data = new ArrayList<>();
        int recordId = 0, count, i;
        SQLiteDatabase db = dbManager.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT record_id FROM CarRecords WHERE vin = '"+vin+"'",null);
        while(cursor.moveToNext()){
            data.add(cursor.getInt(0));
        }
        count = cursor.getCount();
        cursor.close();

        for(i=0;i<count;++i){
            recordId = data.get(i);
            cursor = db.rawQuery("SELECT url_id FROM PictureURL WHERE record_id = "+recordId,null);
            while (cursor.moveToNext()){
                list = list + cursor.getInt(0) + "\n";
            }
            cursor.close();
        }

        return list;
    }
    public String getURL(int id){
        SQLiteDatabase db = dbManager.getReadableDatabase();
        String url = "";

        Cursor cursor = db.rawQuery("SELECT url FROM PictureURL WHERE url_id = "+id,null);
        if(cursor.moveToFirst()){
            url = cursor.getString(0);
        }
        cursor.close();

        return url;
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
