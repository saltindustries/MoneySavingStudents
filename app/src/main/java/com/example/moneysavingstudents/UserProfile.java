package com.example.moneysavingstudents;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.seismic.ShakeDetector;

public class UserProfile extends AppCompatActivity implements  ShakeDetector.Listener{

    TextView fullNameLabel, usernameLabel, emailLabel;
    TextInputLayout fullName, email;
    Button btnAddItems, btnViewItems, btnUpdate;
    DatabaseReference reference;

    String user_username, user_name, user_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        reference = FirebaseDatabase.getInstance("https://doineedit-f0389-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("users");

        // Shake Detector
        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        ShakeDetector shakeDetector = new ShakeDetector(this);
        shakeDetector.start(sensorManager);

        //Hooks
        fullNameLabel = findViewById(R.id.profile_fullname);
        usernameLabel = findViewById(R.id.profile_username);
        emailLabel = findViewById(R.id.profile_email);

        fullName = findViewById(R.id.editable_fullname);
        email = findViewById(R.id.editable_email);

        btnAddItems = findViewById(R.id.btn_addItems);
        btnViewItems = findViewById(R.id.btn_viewItems);
        btnUpdate = findViewById(R.id.btn_Update);

        //Show All Data
        Intent intent = getIntent();
        user_username = intent.getStringExtra("username");
        user_name = intent.getStringExtra("name");
        user_email = intent.getStringExtra("email");

        fullNameLabel.setText("WELCOME BACK, "+ user_name);
        usernameLabel.setText(user_username);
        emailLabel.setText(user_email);

        fullName.getEditText().setText(user_name);
        email.getEditText().setText(user_email);

        // Call View Items activity which displays added items for currently logged in user.
        // Empty by default.
        btnViewItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserProfile.this,ViewItems.class);
                intent.putExtra("name",  user_name);
                intent.putExtra("username",user_username);
                intent.putExtra("email",user_email);
                startActivity(intent);
            }
        });

        // Call Add Items activity
        btnAddItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserProfile.this,AddItems.class);
                intent.putExtra("name",  user_name);
                intent.putExtra("username",user_username);
                intent.putExtra("email",user_email);
                startActivity(intent);
            }
        });

        // Allows the user to update their name and email
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isFullNameChanged() || isEmailChanged()) {
                    Toast.makeText(getBaseContext(), "Your info has been updated.", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getBaseContext(), "No changes made.", Toast.LENGTH_SHORT).show();
                }
            }

            private boolean isEmailChanged() {
                if (!user_email.equals(email.getEditText().getText().toString())){
                    reference.child(user_username).child("email").setValue(email.getEditText().getText().toString());
                    user_email = email.getEditText().getText().toString();
                    emailLabel.setText(user_email);
                    return true;
                }
                else {
                    return false;
                }
            }

            private boolean isFullNameChanged() {
                if (!user_name.equals(fullName.getEditText().getText().toString())){
                    reference.child(user_username).child("name").setValue(fullName.getEditText().getText().toString());
                    user_name = fullName.getEditText().getText().toString();
                    fullNameLabel.setText("WELCOME BACK, "+ user_name);
                    return true;
                }
                else {
                    return false;
                }
            }
        });
    }

    @Override
    public void hearShake() {
        Toast.makeText(this, "Device shook", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(UserProfile.this,AddItems.class);
        intent.putExtra("name",  user_name);
        intent.putExtra("username",user_username);
        intent.putExtra("email",user_email);
        startActivity(intent);
    }
}