package com.pulps.gogreen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pulps.gogreen.Model.Item;
import com.pulps.gogreen.ViewHolder.RequestAdapter;

import java.util.ArrayList;

public class AdminNewRequestsActivity extends AppCompatActivity {

    private RecyclerView requestList;
    private RequestAdapter myAdapter;
    private Button rejectBtn,buyBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_new_requests);
        requestList=(RecyclerView) findViewById(R.id.requests_list);
        requestList.setHasFixedSize(true);
        requestList.setLayoutManager(new LinearLayoutManager(this));

        //fetch data from the firebase
        FirebaseRecyclerOptions<Item> options =
                new FirebaseRecyclerOptions.Builder<Item>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Item"),Item.class)
                        .build();
        //send fetch data to the adapter class
        myAdapter = new RequestAdapter(options);
        requestList.setAdapter(myAdapter);

    }

    @Override
    protected void onStart() {
        super.onStart();
        myAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        myAdapter.stopListening();
    }
}