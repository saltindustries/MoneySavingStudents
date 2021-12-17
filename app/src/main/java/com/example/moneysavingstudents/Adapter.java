package com.example.moneysavingstudents;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    private List<ViewItemsHelperClass> productList;
    Context context;


    public Adapter (Context ct, List<ViewItemsHelperClass>productList){
        this.productList=productList;
        context = ct;
    }

    @NonNull
    @Override
    public Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_recyclerview,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter.ViewHolder holder, int position) {

        String pName=productList.get(position).getTv1();
        String pPrice=productList.get(position).getTv2();
        String pLink=productList.get(position).getTv3();
        String pCategory=productList.get(position).getTv4();
        Boolean pPurchased=productList.get(position).getPurchased();
        String user_username=productList.get(position).getUser_username();

        holder.setData(pName,pPrice,pLink,pCategory, pPurchased, position, user_username);

        holder.tv1.setText(pName);
        holder.tv2.setText(pPrice);
        holder.tv3.setText(pLink);
        holder.tv4.setText(pCategory);

        FirebaseDatabase rootNode = FirebaseDatabase.getInstance("https://doineedit-f0389-default-rtdb.asia-southeast1.firebasedatabase.app/");
        DatabaseReference reference = rootNode.getReference("users").child(user_username);

        // Marks an item as purchased or not purchased in DB.
        // Displays in readable format to user in setData().
        holder.purchaseIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String temp=""+position;

                   Boolean pPurchased= productList.get(position).getPurchased();

                    if(pPurchased){
                        reference.child(temp).child("productPurchased").setValue(false);
                        productList.get(position).setPurchased(false);
                        holder.purchaseIcon.setBackgroundResource(R.drawable.default_cart);

                        Toast.makeText(v.getContext(), String.valueOf(productList.get(position).getPurchased()), Toast.LENGTH_LONG);

                    }
                    else if (!pPurchased){
                       reference.child(temp).child("productPurchased").setValue(true);
                        productList.get(position).setPurchased(true);
                        holder.purchaseIcon.setBackgroundResource(R.drawable.default_cart_purchased);

                        Toast.makeText(v.getContext(), String.valueOf(productList.get(position).getPurchased()), Toast.LENGTH_LONG);
                    }
            }
        });

        // Calls the Edit Item (individual) activity for desired item and allows the user to update the data.
        holder.editIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, ViewItemsIndividual.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("ProductName",pName);
                intent.putExtra("ProductPrice",pPrice);
                intent.putExtra("ProductLink",pLink);
                intent.putExtra("ProductCategory",pCategory);

                context.startActivity(intent);

            }
        });

        // Shares the desired item in any app. User can open Message app for SMS Sharing
        holder.shareIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                String myMessage = ("I'd love this item. " + pName + " " + pPrice + " " + pLink);
                sendIntent.putExtra(Intent.EXTRA_TEXT, myMessage);
                sendIntent.setType("text/plain");
                Intent shareIntent = Intent.createChooser(sendIntent, null);
                shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(shareIntent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView tv1, tv2, tv3, tv4;
        private ImageButton purchaseIcon, editIcon, shareIcon;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // Hooks
            tv1=itemView.findViewById(R.id.tv1);
            tv2=itemView.findViewById(R.id.tv2);
            tv3=itemView.findViewById(R.id.tv3);
            tv4=itemView.findViewById(R.id.tv4);
            editIcon=itemView.findViewById(R.id.editIcon);
            purchaseIcon=itemView.findViewById(R.id.purchasedIcon);
            shareIcon=itemView.findViewById(R.id.shareIcon);
        }

        // Changes the purchased icon depending on the user. (Blue - Not Purchased / Gray - Purchased)
        public void setData(String pName, String pPrice, String pLink, String pCategory, Boolean pPurchased, int position, String user_username) {
            if(pPurchased){
                purchaseIcon.setBackgroundResource(R.drawable.default_cart_purchased);
            }
            else{
                purchaseIcon.setBackgroundResource(R.drawable.default_cart);
            }
        }
    }
}
