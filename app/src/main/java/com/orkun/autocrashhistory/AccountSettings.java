package com.orkun.autocrashhistory;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class AccountSettings extends AppCompatActivity {

    private String name;
    private String password;
    private String password2;
    private carDatabase dbManager = new carDatabase(this);
    private DatabaseReference rDatabase;
    MainActivity firstMenu = new MainActivity();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_settings);

        rDatabase = FirebaseDatabase.getInstance().getReference();

        EditText nameInput = (EditText) findViewById(R.id.newUsername2);
        EditText passwordInput = (EditText) findViewById(R.id.newPassword2);
        EditText password2Input = (EditText) findViewById(R.id.checkPassword2);
        Button nameSubmit = (Button) findViewById(R.id.changeName);
        Button passwordSubmit = (Button) findViewById(R.id.changePassword);

        rDatabase = FirebaseDatabase.getInstance().getReference();

        nameSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = nameInput.getText().toString();

                rDatabase.child("users").child(name).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (!task.isSuccessful()) {
                            Log.e("firebase", "Error getting data", task.getException());
                        }
                        else {
                            User user = task.getResult().getValue(User.class);
                            if(user != null)
                                Toast.makeText(getApplicationContext(),"Name not unique!",Toast.LENGTH_SHORT).show();
                            else{
                                user = new User(firstMenu.getIdActive(),name,firstMenu.getPasswordActive());
                                rDatabase.child("users").child(name).setValue(user);
                                rDatabase.child("users").child(firstMenu.getNameActive()).removeValue();
                                changeName(name);
                                Toast.makeText(getApplicationContext(),"New user name: "+name,Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
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
                    rDatabase.child("users").child(firstMenu.getNameActive()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            if (!task.isSuccessful()) {
                                Log.e("firebase", "Error getting data", task.getException());
                            }
                            else{
                                User user = task.getResult().getValue(User.class);
                                user.setPassword(password);
                                rDatabase.child("users").child(firstMenu.getNameActive()).setValue(user);
                            }
                        }
                    });
                    Toast.makeText(getApplicationContext(),"Your password changed!",Toast.LENGTH_LONG).show();
                }
            }
        });
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
