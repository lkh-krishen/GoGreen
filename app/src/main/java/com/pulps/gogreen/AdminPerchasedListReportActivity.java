package com.pulps.gogreen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.pulps.gogreen.Model.Item;
import com.pulps.gogreen.ViewHolder.PerchasedListAdapter;
import com.pulps.gogreen.ViewHolder.RequestAdapter;

public class AdminPerchasedListReportActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    PerchasedListAdapter myAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_perchased_list_report);

        recyclerView=(RecyclerView) findViewById(R.id.perchased_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<Item> options =
                new FirebaseRecyclerOptions.Builder<Item>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("PerchasedList"),Item.class)
                        .build();

        myAdapter = new PerchasedListAdapter(options);
        recyclerView.setAdapter(myAdapter);
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