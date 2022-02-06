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
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class EditRecord extends AppCompatActivity {
    private carDatabase dbManager = new carDatabase(this);
    private String vins;
    MainActivity mainActivity = new MainActivity();
    private String vin;
    private String recordId = "0";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_record);

        TextView vinList = (TextView) findViewById(R.id.vinList);
        vinList.setText("List of VIN numbers you recorded:\n"+getVins());
        EditText vinInput = (EditText) findViewById(R.id.vinInput3);
        Button recordSearch = (Button) findViewById(R.id.recordSearch2);
        TextView idList = (TextView) findViewById(R.id.idList2);
        EditText recordInput = (EditText) findViewById(R.id.recordIdInput2);
        Button lookAtRecord = (Button) findViewById(R.id.lookAtRecord2);
        WebView url = (WebView) findViewById(R.id.recordPicture);
        EditText newUrl = (EditText) findViewById(R.id.urlInput2);
        Button change = (Button) findViewById(R.id.changeButton);
        Button delete = (Button) findViewById(R.id.deleteButton);

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
            }
        });
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = newUrl.getText().toString();
                int id = Integer.parseInt(recordId);
                changeRecord(url, id);
                Toast.makeText(getApplicationContext(),"Record changed!",Toast.LENGTH_LONG).show();
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = newUrl.getText().toString();
                int id = Integer.parseInt(recordId);
                deleteRecord(id);
                Toast.makeText(getApplicationContext(),"Record deleted!",Toast.LENGTH_LONG).show();
                idList.setText(makeList(vin));
            }
        });
    }

    public String getVins(){
        String vins = "";
        SQLiteDatabase db = dbManager.getReadableDatabase();
        int userId = 0;

        Cursor cursor = db.rawQuery("SELECT user_id FROM Users WHERE name = '"+mainActivity.getNameActive()+"'",null);
        if(cursor.moveToFirst()){
            userId = cursor.getInt(0);
        }
        cursor.close();
        cursor = db.rawQuery("SELECT vin FROM CarRecords WHERE user_id = "+userId,null);
        while(cursor.moveToNext()){
            vins = vins+cursor.getString(0)+"\n";
        }
        cursor.close();

        return vins;
    }
    public String makeList(String vin){
        String list = "";
        int recordId = 0;
        int userId = 0;
        SQLiteDatabase db = dbManager.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT user_id FROM Users WHERE name = '"+mainActivity.getNameActive()+"'",null);
        if(cursor.moveToFirst()){
            userId = cursor.getInt(0);
        }
        cursor.close();

        cursor = db.rawQuery("SELECT record_id FROM CarRecords WHERE vin = '"+vin+"' AND user_id = "+userId,null);
        if(cursor.moveToFirst()){
            recordId = cursor.getInt(0);
        }
        cursor.close();

        cursor = db.rawQuery("SELECT url_id FROM PictureURL WHERE record_id = "+recordId,null);
        while (cursor.moveToNext()){
            list = list + cursor.getInt(0) + "\n";
        }
        cursor.close();

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
    public void changeRecord(String url, int id){
        SQLiteDatabase db = dbManager.getWritableDatabase();
        db.execSQL("UPDATE PictureURL SET url = '"+url+"' WHERE url_id = "+id);
    }
    public void deleteRecord(int id){
        SQLiteDatabase db = dbManager.getWritableDatabase();
        db.execSQL("DELETE FROM PictureURL WHERE url_id ="+id);
    }
}
