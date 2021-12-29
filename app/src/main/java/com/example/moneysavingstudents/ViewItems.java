package com.example.moneysavingstudents;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ViewItems extends AppCompatActivity {

    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    public List<ViewItemsHelperClass> productList;
    Adapter adapter;

    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_items);

        Intent intent = getIntent();
        String user_username = intent.getStringExtra("username");
        String user_name = intent.getStringExtra("name");
        String user_email = intent.getStringExtra("email");

        initData(user_username, user_name, user_email);

    }

    // Displays items added by user
    private void initData(String user_username, String user_name, String user_email) {
        reference= FirebaseDatabase.getInstance("https://doineedit-f0389-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("users");

        productList = new ArrayList<>();
        reference.child(user_username).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                long size=snapshot.getChildrenCount()-5;
                String key="0";

                for (long i=0;i<size;i++) {
                    key=""+i;
                    productList.add(new ViewItemsHelperClass(snapshot.child(key).child("productName").getValue(String.class),
                            snapshot.child(key).child("productPrice").getValue(String.class),
                            snapshot.child(key).child("productLink").getValue(String.class),
                            snapshot.child(key).child("productCategory").getValue(String.class),
                            snapshot.child(key).child("productPurchased").getValue(Boolean.class),
                            user_username,user_name, user_email
                    ));
                }
                initRecyclerView();

                ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
                itemTouchHelper.attachToRecyclerView(recyclerView);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    // Swipe Left / Right to delete
    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        // Ignore
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            Intent intent = getIntent();
            String user_username = intent.getStringExtra("username");
            String user_name = intent.getStringExtra("name");
            int position = viewHolder.getAdapterPosition();
            switch (direction){
                case ItemTouchHelper.LEFT:
                case ItemTouchHelper.RIGHT:
                    String toDelete=""+position;
                    reference.child(user_username).child(toDelete).removeValue();

                    reference.child(user_username).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            long size=snapshot.getChildrenCount();
                            String child;

                            for (int x=position; x<size-1;x++){
                                child=""+(x+1);

                                String tempName= snapshot.child(child).child("productName").getValue(String.class);
                                String tempPrice= snapshot.child(child).child("productPrice").getValue(String.class);
                                String tempLink= snapshot.child(child).child("productLink").getValue(String.class);
                                String tempCategory= snapshot.child(child).child("productCategory").getValue(String.class);
                                Boolean tempPurchcased= snapshot.child(child).child("productPurchased").getValue(Boolean.class);

                                child=""+x;
                                AddItemsHelperClass productHelperClass = new AddItemsHelperClass(tempName, tempPrice, tempLink, tempCategory, tempPurchcased);
                                reference.child(user_username).child(child).setValue(productHelperClass);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    productList.remove(position);
                    adapter.notifyItemRemoved(position);
                    break;
            }
        }
    };

    private void initRecyclerView() {
        recyclerView=findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new Adapter(getApplication(), productList);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}