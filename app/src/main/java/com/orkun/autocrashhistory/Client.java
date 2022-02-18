package com.orkun.autocrashhistory;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Client extends Activity {
    carDatabase dbManager = new carDatabase(this);
    private DatabaseReference rDatabase = FirebaseDatabase.getInstance().getReference();

    public void sendUsers(){
        List<User> userList = new ArrayList<User>();
        SQLiteDatabase db = dbManager.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM Users", null);
        if(c.moveToFirst()){
            do{
                userList.add(new User(
                        c.getString(0),
                        c.getString(1),
                        c.getString(2)
                ));
            }while (c.moveToNext());
        }
        c.close();
        if(userList.size()>0){
            for (User u : userList){
                rDatabase.push().setValue(u);
            }
        }
    }
    public void sendCarRecord(){
        List<CarRecord> recordList = new ArrayList<CarRecord>();
        SQLiteDatabase db = dbManager.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM CarRecord", null);
        if(c.moveToFirst()){
            do{
                recordList.add(new CarRecord(
                        c.getInt(0),
                        c.getInt(1),
                        c.getString(2)
                ));
            }while (c.moveToNext());
        }
        c.close();
        if(recordList.size()>0){
            for (CarRecord r : recordList){
                rDatabase.push().setValue(r);
            }
        }
    }
    public void sendPictureURL(){
        List<PictureURL> recordList = new ArrayList<PictureURL>();
        SQLiteDatabase db = dbManager.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM CarRecord", null);
        if(c.moveToFirst()){
            do{
                recordList.add(new PictureURL(
                        c.getInt(0),
                        c.getInt(1),
                        c.getString(2)
                ));
            }while (c.moveToNext());
        }
        c.close();
        if(recordList.size()>0){
            for (PictureURL r : recordList){
                rDatabase.push().setValue(r);
            }
        }
    }
    public void sendDatabase(){
        sendUsers();
        sendCarRecord();
        sendPictureURL();
    }
}
