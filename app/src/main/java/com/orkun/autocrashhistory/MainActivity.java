package com.orkun.autocrashhistory;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private String name;
    private String password;
    private final carDatabase dbManager = new carDatabase(this);
    public static String nameActive;
    public static String passwordActive;
    private DatabaseReference rDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rDatabase = FirebaseDatabase.getInstance().getReference();

        Button signup = (Button) findViewById(R.id.signUpButton);
        Button signin = (Button) findViewById(R.id.signInButton);
        EditText nameInput = (EditText)findViewById(R.id.usernameInput);
        EditText passwordInput = (EditText)findViewById(R.id.passwordInput);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),SignUp.class);
                startActivity(i);
            }
        });
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),MainMenu.class);

                name = nameInput.getText().toString();
                password = passwordInput.getText().toString();

                if(check(name,password)){
                    setNameActive(name);
                    setPasswordActive(password);
                    startActivity(i);
                }
                else {
                    Toast.makeText(getApplicationContext(),"Username and password may be wrong!",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private boolean check(String name, String password) {
        SQLiteDatabase db = dbManager.getReadableDatabase();
        String query = "SELECT * FROM Users WHERE name = '" + name + "' AND password = '"+password+"'";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.getCount() == 0) {
            return false;
        }
        return true;
    }

    public String getNameActive() {
        return nameActive;
    }

    public void setNameActive(String nameActive) {
        this.nameActive = nameActive;
    }

    public String getPasswordActive() {
        return passwordActive;
    }

    public void setPasswordActive(String passwordActive) {
        this.passwordActive = passwordActive;
    }
}