package com.orkun.autocrashhistory;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashMap;

public class carDatabase extends SQLiteOpenHelper {
    private static final String name = "cars";
    private static final int version = 1;

    public carDatabase(@Nullable Context context) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE Users(user_id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR(50) NOT NULL, password VARCHAR(50));");
        db.execSQL("CREATE TABLE CarRecords(record_id INTEGER PRIMARY KEY AUTOINCREMENT, user_id INT NOT NULL, vin VARCHAR(25) NOT NULL, FOREIGN KEY(user_id) REFERENCES Users(user_id));");
        db.execSQL("CREATE TABLE PictureURL(url_id INTEGER PRIMARY KEY AUTOINCREMENT, record_id INT NOT NULL, url TEXT, FOREIGN KEY(record_id) REFERENCES Records(record_id));");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Users;");
        db.execSQL("DROP TABLE IF EXISTS CarRecords;");
        db.execSQL("DROP TABLE IF EXISTS PictureURL;");
    }

}
