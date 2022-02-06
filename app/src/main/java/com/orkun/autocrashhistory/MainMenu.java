package com.orkun.autocrashhistory;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

//Example username: example, example password: 123

public class MainMenu extends AppCompatActivity {
    MainActivity firstMenu = new MainActivity();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);

        Button settings = (Button)findViewById(R.id.settingsMenu);
        Button quit = (Button)findViewById(R.id.quitAccount);
        Button addRecord = (Button)findViewById(R.id.addRecordMenu);
        Button searchRecord = (Button)findViewById(R.id.recordSearchMenu);
        Button editRecord = (Button)findViewById(R.id.editRecordMenu);

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),Settings.class);
                startActivity(i);
            }
        });

        quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),MainActivity.class);
                firstMenu.setNameActive(null);
                firstMenu.setPasswordActive(null);
                startActivity(i);
            }
        });

        addRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),AddRecord.class);
                startActivity(i);
            }
        });

        searchRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),SearchRecord.class);
                startActivity(i);
            }
        });
        editRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),EditRecord.class);
                startActivity(i);
            }
        });
    }
}
