package com.example.moneysavingstudents;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ViewItemsIndividual extends AppCompatActivity {

    TextView TextViewProductName;
    TextInputLayout inputProductName, inputProductPrice, inputProductLink, inputProductCategory;
    Button btnUpdateItem, btnDeleteItem;
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
        btnDeleteItem = findViewById(R.id.deleteButton);

        Intent intent = getIntent();
        String pProductName = intent.getStringExtra("ProductName");
        String pProductPrice = intent.getStringExtra("ProductPrice");
        String pProductLink = intent.getStringExtra("ProductLink");
        String pProductCategory = intent.getStringExtra("ProductCategory");

        String user_username=intent.getStringExtra("UserUsername");
        String user_name=intent.getStringExtra("UserName");
        String user_email=intent.getStringExtra("UserEmail");
        int key=intent.getIntExtra("key",0);

        inputProductName.getEditText().setText(pProductName);
        inputProductPrice.getEditText().setText(pProductPrice);
        inputProductLink.getEditText().setText(pProductLink);
        inputProductCategory.getEditText().setText(pProductCategory);

        btnUpdateItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = inputProductName.getEditText().getText().toString();
                String price= inputProductPrice.getEditText().getText().toString();
                String url= inputProductLink.getEditText().getText().toString();
                String category= inputProductCategory.getEditText().getText().toString();
                String child=""+key;
                if (intent.hasExtra("purchased")){
                    AddItemsHelperClass productHelperClass = new AddItemsHelperClass(name, price, url, category, true);
                    reference.child(user_username).child(child).setValue(productHelperClass);

                    Intent intent = new Intent(ViewItemsIndividual.this,ViewItems.class);
                    intent.putExtra("name",  user_name);
                    intent.putExtra("username",user_username);
                    intent.putExtra("email", user_email);
                    startActivity(intent);

                }
                else{
                    AddItemsHelperClass productHelperClass = new AddItemsHelperClass(name, price, url, category, false);
                    reference.child(user_username).child(child).setValue(productHelperClass);

                    Intent intent = new Intent(ViewItemsIndividual.this,UserProfile.class);
                    intent.putExtra("name",  user_name);//to change
                    intent.putExtra("username",user_username);
                    intent.putExtra("email", user_email);
                    startActivity(intent);
                }
                Toast.makeText(getBaseContext(), "Item Updated" , Toast.LENGTH_SHORT ).show();
            }
        });

        btnDeleteItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String toDelete=""+key;
                reference.child(user_username).child(toDelete).removeValue();

                reference.child(user_username).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        long size=snapshot.getChildrenCount();
                        String child;

                        for (int x=key; x<size-1;x++){
                            child=""+(x+1);

                            String tempName= snapshot.child(child).child("productName").getValue(String.class);
                            String tempPrice= snapshot.child(child).child("productPrice").getValue(String.class);
                            String tempLink= snapshot.child(child).child("productLink").getValue(String.class);
                            String tempCategory= snapshot.child(child).child("productCategory").getValue(String.class);
                            Boolean tempPurchcased= snapshot.child(child).child("productPurchased").getValue(Boolean.class);

                            child=""+x;
                            AddItemsHelperClass productHelperClass = new AddItemsHelperClass(tempName, tempPrice, tempLink, tempCategory, tempPurchcased);
                            reference.child(user_username).child(child).setValue(productHelperClass);

                            Intent intent = new Intent(ViewItemsIndividual.this, UserProfile.class);
                            intent.putExtra("name",  user_name);//to change
                            intent.putExtra("username",user_username);
                            intent.putExtra("email", user_email);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


                Toast.makeText(getBaseContext(), "Item Deleted" , Toast.LENGTH_SHORT ).show();
            }
        });
    }
}