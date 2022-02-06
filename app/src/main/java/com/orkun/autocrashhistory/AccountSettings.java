package com.orkun.autocrashhistory;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AccountSettings extends AppCompatActivity {

    private String name;
    private String password;
    private String password2;
    private carDatabase dbManager = new carDatabase(this);
    MainActivity firstMenu = new MainActivity();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_settings);

        EditText nameInput = (EditText) findViewById(R.id.newUsername2);
        EditText passwordInput = (EditText) findViewById(R.id.newPassword2);
        EditText password2Input = (EditText) findViewById(R.id.checkPassword2);
        Button nameSubmit = (Button) findViewById(R.id.changeName);
        Button passwordSubmit = (Button) findViewById(R.id.changePassword);

        nameSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = nameInput.getText().toString();
                if(!checkUniqueness(name)){
                    Toast.makeText(getApplicationContext(),"Name not unique!",Toast.LENGTH_SHORT).show();
                }
                else{
                    changeName(name);
                    Toast.makeText(getApplicationContext(),"New user name: "+name,Toast.LENGTH_LONG).show();
                }
            }
        });

        passwordSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                password = passwordInput.getText().toString();
                password2 = password2Input.getText().toString();
                if(!password.equals(password2)){
                    Toast.makeText(getApplicationContext(),"Passwords not equal!",Toast.LENGTH_SHORT).show();
                }
                else{
                    changePassword(password);
                    Toast.makeText(getApplicationContext(),"Your password changed!",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public boolean checkUniqueness(String name){
        SQLiteDatabase db = dbManager.getReadableDatabase();
        String query = "SELECT * FROM Users WHERE name = '" + name + "';";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.getCount() != 0) {
            return false;
        }
        return true;
    }
    public void changeName(String name){
        System.out.println(firstMenu.getNameActive());
        SQLiteDatabase db = dbManager.getReadableDatabase();
        db.execSQL("UPDATE Users SET name = '"+name+"' WHERE name = '"+firstMenu.getNameActive()+"'");
        firstMenu.setNameActive(name);
    }
    public void changePassword(String password){
        SQLiteDatabase db = dbManager.getReadableDatabase();
        db.execSQL("UPDATE Users SET password = '"+password+"' WHERE name = '"+firstMenu.getNameActive()+"'");
        firstMenu.setPasswordActive(password);
    }
}
