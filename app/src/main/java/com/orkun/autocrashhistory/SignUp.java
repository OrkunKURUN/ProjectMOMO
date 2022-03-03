package com.orkun.autocrashhistory;

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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignUp extends AppCompatActivity {
    private String name;
    private String password;
    private String password2;
    private carDatabase dbManager = new carDatabase(this);
    private DatabaseReference rDatabase;
    private User user;

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
                rDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(name);
                password = passwordInput.getText().toString();
                password2 = password2Input.getText().toString();
                if(password.equals(password2)){
                    addAccount(name, password);
                }
                else{
                    Toast.makeText(getApplicationContext(),"Please make sure that both password inputs are same.",Toast.LENGTH_LONG).show();
                }
            }
        });

    }
    public void addAccount(String name, String password){
        SQLiteDatabase db = dbManager.getWritableDatabase();

        String id = rDatabase.push().getKey();//getting unique id
        ValueEventListener userListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = null;
                User u2 = snapshot.getValue(User.class);
                if(u2 == null){
                    System.out.println("NULL");
                    user = new User(id,name,password);
                    rDatabase.setValue(user);

                    db.execSQL("INSERT INTO Users(name, password) VALUES('"+name+"','"+password+"')");
                    Toast.makeText(getApplicationContext(),name+", welcome!",Toast.LENGTH_LONG).show();
                }
                else{
                    System.out.println("Username: "+u2.getName());//test line
                    Toast.makeText(getApplicationContext(),"Name not unique!",Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("TAG","loadPost:onCancelled", error.toException());
            }
        };

        rDatabase.addValueEventListener(userListener);
        //...

    }

}
