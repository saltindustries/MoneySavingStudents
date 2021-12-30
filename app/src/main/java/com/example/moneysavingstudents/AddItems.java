package com.example.moneysavingstudents;

import android.content.Intent;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.List;

public class AddItems extends AppCompatActivity{

    Button addItem, cancelItem;
    TextInputLayout prodName, prodPrice, prodUrl, prodCategory;
    long size;
    FirebaseDatabase rootNode;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_items);

        // Hooks
        addItem = findViewById(R.id.btnAddItem);
        cancelItem = findViewById(R.id.btnCancelItem);
        prodName = findViewById(R.id.productName);
        prodPrice = findViewById(R.id.productPrice);
        prodUrl = findViewById(R.id.productLink);
        prodCategory = findViewById(R.id.productCategory);

        Intent intent = getIntent();
        String user_username = intent.getStringExtra("username");
        String user_name = intent.getStringExtra("name");
        String user_email = intent.getStringExtra("email");

        // Adds item to DB and store it.
        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rootNode = FirebaseDatabase.getInstance("https://doineedit-f0389-default-rtdb.asia-southeast1.firebasedatabase.app/");
                reference = rootNode.getReference("users");

                String name = prodName.getEditText().getText().toString();
                String price=prodPrice.getEditText().getText().toString();
                String url=prodUrl.getEditText().getText().toString();
                String category=prodCategory.getEditText().getText().toString();

                reference.child(user_username).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        size=snapshot.getChildrenCount()-5;
                        String key= ""+size;

                        AddItemsHelperClass productHelperClass = new AddItemsHelperClass(name, price, url, category, false);
                        reference.child(user_username).child(key).setValue(productHelperClass);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                Intent intent = new Intent(AddItems.this,UserProfile.class);
                intent.putExtra("name",  user_name);
                intent.putExtra("username",user_username);
                intent.putExtra("email",user_email);
                startActivity(intent);

                Toast.makeText(getBaseContext(), "Product added successfully" , Toast.LENGTH_SHORT ).show();
            }
        });

        // Cancel adding items and return to previous activity
        cancelItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddItems.this,UserProfile.class);
                intent.putExtra("name",  user_name);
                intent.putExtra("username",user_username);
                intent.putExtra("email",user_email);
                startActivity(intent);
            }
        });
    }
}