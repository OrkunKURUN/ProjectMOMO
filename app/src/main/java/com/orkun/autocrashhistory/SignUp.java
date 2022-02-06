package com.orkun.autocrashhistory;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SignUp extends AppCompatActivity {
    private String name;
    private String password;
    private String password2;
    private carDatabase dbManager = new carDatabase(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);

        EditText nameInput = (EditText) findViewById(R.id.newUsername);
        EditText passwordInput = (EditText) findViewById(R.id.newPassword);
        EditText password2Input = (EditText) findViewById(R.id.checkPassword);
        Button signUpButton = (Button) findViewById(R.id.addAccount);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = nameInput.getText().toString();
                password = passwordInput.getText().toString();
                password2 = password2Input.getText().toString();
                if(password.equals(password2)){
                    if(!checkUniqueness(name)){
                        Toast.makeText(getApplicationContext(),"Name not unique!",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        addAccount(name, password);
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(),"Please make sure that both password inputs are same.",Toast.LENGTH_LONG).show();
                }
            }
        });

    }
    public void addAccount(String name, String password){
        SQLiteDatabase db = dbManager.getWritableDatabase();
        db.execSQL("INSERT INTO Users(name, password) VALUES('"+name+"','"+password+"')");
        Toast.makeText(getApplicationContext(),name+", welcome!",Toast.LENGTH_LONG).show();
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
}