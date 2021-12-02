package com.pulps.gogreen.ViewHolder;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pulps.gogreen.Model.Item;
import com.pulps.gogreen.R;
import java.util.HashMap;
import java.util.Map;

public class RequestAdapter extends FirebaseRecyclerAdapter<Item,RequestAdapter.MyAdapter>{

    long id=0;

    //constructor
    public RequestAdapter(@NonNull FirebaseRecyclerOptions<Item> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull MyAdapter myAdapter, int position, @NonNull Item item) {

        //set retrieved values to the TextViews
        myAdapter.item.setText(item.getItem());
        myAdapter.price.setText(String.valueOf(item.getPrice()));
        myAdapter.weight.setText(String.valueOf(item.getWeight()));
        myAdapter.total.setText(String.valueOf(item.getTotal()));
        myAdapter.bank.setText(item.getBank());
        myAdapter.account.setText(String.valueOf(item.getAccount()));
        myAdapter.address.setText(item.getAddress());
        myAdapter.email.setText(item.getEmail());
        myAdapter.mobile.setText(String.valueOf(item.getMobile()));
        myAdapter.status.setText(item.getStatus());

        myAdapter.buyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TextView newItem,newPrice,newWeight,newTotal,newAddress,newBank,newAccount,newEmail,newMobile,newStatus;
                Map<String,Object> map = new HashMap<>();

                map.put("item",item.getItem());
                map.put("price",item.getPrice());
                map.put("weight",item.getWeight());
                map.put("total",item.getTotal());
                map.put("address",item.getAddress());
                map.put("bank",item.getBank());
                map.put("account",item.getAccount());
                map.put("email",item.getEmail());
                map.put("mobile",item.getMobile());
                map.put("status","perchased");

                FirebaseDatabase.getInstance().getReference().child("Item")
                        .child(getRef(myAdapter.getAdapterPosition()).getKey()).updateChildren(map)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {

                                Toast.makeText(myAdapter.item.getContext(), "perchased the item", Toast.LENGTH_SHORT).show();

                                //add perchased details into perchasedItems table
                                DatabaseReference ref;
                                ref=FirebaseDatabase.getInstance().getReference().child("PerchasedList");

                                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if(snapshot.exists()){
                                            id=(snapshot.getChildrenCount());
                                        }

                                        ref.child(String.valueOf(id+1)).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()){
                                                    Toast.makeText(myAdapter.item.getContext(), "data transfered to the perchasedList", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });

                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(myAdapter.item.getContext(), "error when perchasing", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        //delete operation
        myAdapter.rejectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(myAdapter.item.getContext());
                builder.setTitle("Are you sure?");
                builder.setMessage("Deleted data can not be undone!");

                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FirebaseDatabase.getInstance().getReference().child("Item")
                                .child(getRef(myAdapter.getAdapterPosition()).getKey()).removeValue();
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(myAdapter.item.getContext(), "cancelled", Toast.LENGTH_SHORT).show();
                    }
                });

                builder.show();
            }
        });

    }

    @NonNull
    @Override
    public MyAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //getting the RecycleView layout we crated & send the view to the MyAdapter class
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.newrequestlayout,parent,false);
        return new MyAdapter(v);
    }

    class MyAdapter extends RecyclerView.ViewHolder{

        TextView item,price,bank,account,address,email,mobile,weight,status,total;
        Button buyBtn,rejectBtn;
        public MyAdapter(@NonNull View itemView) {
            super(itemView);

            //getting the reference of the textViews of recycleView
            item=itemView.findViewById(R.id.RecyItem);
            price=itemView.findViewById(R.id.RecyPrice);
            bank=itemView.findViewById(R.id.RecyBank);
            account=itemView.findViewById(R.id.RecyAccount);
            address=itemView.findViewById(R.id.RecyAddress);
            email=itemView.findViewById(R.id.RecyEmail);
            mobile=itemView.findViewById(R.id.RecyMobile);
            weight=itemView.findViewById(R.id.RecyWeight);
            status=itemView.findViewById(R.id.RecyStatus);
            total=itemView.findViewById(R.id.RecyTotal);
            rejectBtn = (Button)itemView.findViewById(R.id.reject_btn);
            buyBtn = (Button)itemView.findViewById(R.id.buy_btn);
        }
    }

}