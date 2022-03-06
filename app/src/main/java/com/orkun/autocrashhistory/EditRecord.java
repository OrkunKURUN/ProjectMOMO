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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class EditRecord extends AppCompatActivity {
    private carDatabase dbManager = new carDatabase(this);
    MainActivity mainActivity = new MainActivity();
    private String vin;
    private String recordId = "0";
    private DatabaseReference rDatabase;
    private TextView vinList, idList;
    private WebView url;
    private EditText newUrl;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_record);

        vinList = (TextView) findViewById(R.id.vinList);
        vinList.setText("List of VIN numbers you recorded:\n"+getVins());
        EditText vinInput = (EditText) findViewById(R.id.vinInput3);
        Button recordSearch = (Button) findViewById(R.id.recordSearch2);
        idList = (TextView) findViewById(R.id.idList2);
        EditText recordInput = (EditText) findViewById(R.id.recordIdInput2);
        Button lookAtRecord = (Button) findViewById(R.id.lookAtRecord2);
        url = (WebView) findViewById(R.id.recordPicture);
        newUrl = (EditText) findViewById(R.id.urlInput2);
        Button change = (Button) findViewById(R.id.changeButton);
        Button delete = (Button) findViewById(R.id.deleteButton);

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
                getURL();
            }
        });
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //String url = newUrl.getText().toString();
                //int id = Integer.parseInt(recordId);
                //changeRecord(url, id);
                changeRecord();
                Toast.makeText(getApplicationContext(),"Record changed!",Toast.LENGTH_LONG).show();
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //String url = newUrl.getText().toString();
                //int id = Integer.parseInt(recordId);
                deleteRecord();
                Toast.makeText(getApplicationContext(),"Record deleted!",Toast.LENGTH_LONG).show();
                //idList.setText(makeList(vin));
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
    public void makeList(){
        /*String list = "";
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

        return list;*/
        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<String> id_nums = new ArrayList<>();
                String list = "List of ID numbers you recorded:\n";
                for(DataSnapshot cSnapshot : snapshot.getChildren()){
                    PictureURL record = cSnapshot.getValue(PictureURL.class);
                    if(record.getUserName().equals(mainActivity.getNameActive()))
                        id_nums.add(record.getUrlId());
                }
                for(String id : id_nums)
                    list = list + id + "\n";
                idList.setText(list);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("TAG", "loadPost:onCancelled", error.toException());
            }
        };
        rDatabase = FirebaseDatabase.getInstance().getReference().child("car-records").child(vin);
        rDatabase.addValueEventListener(listener);
    }
    public void getURL(){
        /*SQLiteDatabase db = dbManager.getReadableDatabase();
        String url = "";

        Cursor cursor = db.rawQuery("SELECT url FROM PictureURL WHERE url_id = "+id,null);
        if(cursor.moveToFirst()){
            url = cursor.getString(0);
        }
        cursor.close();

        return url;*/
        rDatabase = FirebaseDatabase.getInstance().getReference().child("car-records").child(vin).child(recordId);
        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                PictureURL record = snapshot.getValue(PictureURL.class);
                if((record != null)&&(record.getUserName().equals(mainActivity.getNameActive()))){
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
    public void changeRecord(){
        //SQLiteDatabase db = dbManager.getWritableDatabase();
        //db.execSQL("UPDATE PictureURL SET url = '"+url+"' WHERE url_id = "+id);
        rDatabase = FirebaseDatabase.getInstance().getReference().child("car-records").child(vin).child(recordId);

        rDatabase.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else{
                    PictureURL record = task.getResult().getValue(PictureURL.class);
                    if(record.getUserName().equals(mainActivity.getNameActive())){
                        record.setUrl(newUrl.getText().toString());
                        rDatabase.setValue(record);
                    }
                }
            }
        });
    }
    public void deleteRecord(){
        //SQLiteDatabase db = dbManager.getWritableDatabase();
        //db.execSQL("DELETE FROM PictureURL WHERE url_id ="+id);
        rDatabase = FirebaseDatabase.getInstance().getReference().child("car-records").child(vin).child(recordId);

        rDatabase.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else{
                    PictureURL record = task.getResult().getValue(PictureURL.class);
                    if(record.getUserName().equals(mainActivity.getNameActive()))
                        rDatabase.removeValue();
                }
            }
        });
    }
}
