package com.example.moneysavingstudents;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ViewItemsIndividual extends AppCompatActivity {

    TextView TextViewProductName;
    TextInputLayout inputProductName, inputProductPrice, inputProductLink, inputProductCategory;
    Button btnUpdateItem;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_items_individual);

        reference= FirebaseDatabase.getInstance("https://doineedit-f0389-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("users");

        // Hooks
        TextViewProductName = findViewById(R.id.tvProductName);
        inputProductName = findViewById(R.id.editProductName);
        inputProductPrice = findViewById(R.id.editProductPrice);
        inputProductLink = findViewById(R.id.editProductLink);
        inputProductCategory = findViewById(R.id.editProductCategory);
        btnUpdateItem = findViewById(R.id.editButton);

        Intent intent = getIntent();
        String pProductName = intent.getStringExtra("ProductName");
        String pProductPrice = intent.getStringExtra("ProductPrice");
        String pProductLink = intent.getStringExtra("ProductLink");
        String pProductCategory = intent.getStringExtra("ProductCategory");

        TextViewProductName.setText(pProductName);
        inputProductName.getEditText().setText(pProductName);
        inputProductPrice.getEditText().setText(pProductPrice);
        inputProductLink.getEditText().setText(pProductLink);
        inputProductCategory.getEditText().setText(pProductCategory);

        btnUpdateItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent = new Intent(ViewItemsIndividual.this, ViewItems.class);
                    startActivity(intent);
                    Toast.makeText(getBaseContext(), "Item Updated" , Toast.LENGTH_SHORT ).show();
            }
        });
    }
}