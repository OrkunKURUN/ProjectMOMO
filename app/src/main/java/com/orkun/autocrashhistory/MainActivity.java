package com.orkun.autocrashhistory;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private String name;
    private String password;
    private final carDatabase dbManager = new carDatabase(this);
    public static String nameActive;
    public static String passwordActive;
    public static String idActive;
    private DatabaseReference rDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

                rDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(name);

                ValueEventListener userListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User user = snapshot.getValue(User.class);
                        if(user == null)
                            Toast.makeText(getApplicationContext(),"User not found!",Toast.LENGTH_SHORT).show();
                        else if(!password.equals(user.getPassword()))
                            Toast.makeText(getApplicationContext(),"Wrong password!",Toast.LENGTH_SHORT).show();
                        else
                            setNameActive(name);
                            setPasswordActive(password);
                            setIdActive(user.getUserId());
                            startActivity(i);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.w("TAG","loadPost:onCancelled", error.toException());
                    }
                };

                rDatabase.addValueEventListener(userListener);
            }
        });
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

    public static String getIdActive() {
        return idActive;
    }

    public static void setIdActive(String idActive) {
        MainActivity.idActive = idActive;
    }
}